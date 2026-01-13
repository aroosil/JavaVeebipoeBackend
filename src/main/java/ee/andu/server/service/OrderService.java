package ee.andu.server.service;

import ee.andu.server.entity.Order;
import ee.andu.server.entity.PaymentState;
import ee.andu.server.entity.Person;
import ee.andu.server.entity.Product;
import ee.andu.server.model.*;
import ee.andu.server.repository.OrderRepository;
import ee.andu.server.repository.PersonRepository;
import ee.andu.server.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Value("${everypay.url}")
    private String baseUrl;

    @Value("${everypay.customerURL}")
    private String customerURL;

    private String prefix = "asdafd";

    public double calculateCartSum(List<Product> products) {
        double sum = 0;
        for (Product product : products) {
            Product dbProduct = productRepository.findById(product.getId()).orElseThrow();
            sum += dbProduct.getPrice();
        }
        return sum;
    }

    public Order createOrder(Long personId, String pmName, List<Product> products){
        Order order = new Order();
        order.setProducts(products);
        order.setCreated(new Date());
        order.setTotal(calculateCartSum(products));
        order.setParcelMachine(pmName);
        order.setPaymentState(PaymentState.INITIAL); // maksmata

        Person person = personRepository.findById(personId).orElseThrow();
        order.setPerson(person); // TODO: teeme autentimise

        return orderRepository.save(order);
    }
    // success: order_reference=asdafd20&payment_reference=701d9f3bc2b86896b48b00f2eb9387c4e1f97204d77b269c6f8a3e2ae6927d05
    // fail: order_reference=asdafd21&payment_reference=1ac71a45979b5b5b48c72d4f06f960b1b328fd37650895780143eec5a6f35432
    public PaymentLink makePayment(Long order_reference, double total) {
        String requestUrl = baseUrl + "/payments/oneoff";

        EveryPayBody body = new EveryPayBody();
        body.setAccount_name("EUR3D1");
        body.setNonce("bla" + ZonedDateTime.now() + UUID.randomUUID());
        body.setTimestamp(ZonedDateTime.now().toString());
        body.setAmount(total);
        body.setOrder_reference(prefix + order_reference);

        body.setCustomer_url(customerURL);
        body.setApi_username("e36eb40f5ec87fa2"); // TODO: saab kui teha kasutaja antud firmale, kes lehte haldab

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("e36eb40f5ec87fa2", "7b91a3b9e1b74524c2e9fc282f8ac8cd");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EveryPayBody> entity = new HttpEntity<>(body, headers);

        EveryPayResponse response = restTemplate.exchange(requestUrl,  HttpMethod.POST, entity, EveryPayResponse.class).getBody();

        if (response == null || response.getPayment_link() == null || response.getPayment_link().isEmpty()) {
            throw new RuntimeException("Payment link is null or empty");
        }

        PaymentLink paymentLink = new PaymentLink();
        paymentLink.setLink(response.getPayment_link());
        return paymentLink;
    }

    public List<ParcelMachine> getParcelMachines() {

        String url = "https://www.omniva.ee/locations.json";
        ParcelMachine[] body = restTemplate.exchange(url,  HttpMethod.GET, null, ParcelMachine[].class).getBody();
        if (body == null) {
            throw new RuntimeException("Could not get parcel machines");
        }
        return Arrays.stream(body).filter(e -> e.getA0_name().equals("EE")).toList();
    }

    public OrderPaid checkPayment(String orderReference, String paymentReference) {
        String debug_username = "e36eb40f5ec87fa2";

        String requestUrl = baseUrl + "/" + paymentReference + "?api_username=" + debug_username + "&detailed=false";
//        String url = "https://igw-demo.every-pay.com/api/v4/payments/701d9f3bc2b86896b48b00f2eb9387c4e1f97204d77b269c6f8a3e2ae6927d05?api_username=e36eb40f5ec87fa2&detailed=false";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(debug_username, "7b91a3b9e1b74524c2e9fc282f8ac8cd");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EveryPayBody> entity = new HttpEntity<>(null, headers);

        EveryPayStatus response = restTemplate.exchange(requestUrl,  HttpMethod.GET, entity, EveryPayStatus.class).getBody();

        if (response == null ) {
            throw new RuntimeException("Could not get payment status");
        }
        if (!response.getOrder_reference().equals(orderReference)) {
            throw new RuntimeException("Order reference does not match");
        }

        PaymentState paymentState = (PaymentState.valueOf(response.getPayment_state().toUpperCase()));

        String order_id = response.getOrder_reference().replace(prefix, "");
        Order order = orderRepository.findById(Long.parseLong(order_id)).orElseThrow();
        order.setPaymentState(paymentState);

        OrderPaid orderPaid = new OrderPaid();
        orderPaid.setPaid(paymentState.equals(PaymentState.SETTLED));
        return orderPaid;
    }
}
