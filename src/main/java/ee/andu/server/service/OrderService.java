package ee.andu.server.service;

import ee.andu.server.entity.Order;
import ee.andu.server.entity.PaymentState;
import ee.andu.server.entity.Person;
import ee.andu.server.entity.Product;
import ee.andu.server.model.EveryPayBody;
import ee.andu.server.model.EveryPayResponse;
import ee.andu.server.model.ParcelMachine;
import ee.andu.server.model.PaymentLink;
import ee.andu.server.repository.OrderRepository;
import ee.andu.server.repository.PersonRepository;
import ee.andu.server.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public PaymentLink makePayment(Long orderId, double total) {
        String url = "https://igw-demo.every-pay.com/api/v4/payments/oneoff";

        EveryPayBody body = new EveryPayBody();
        body.setAccount_name("EUR3D1");
        body.setNonce("bla" + ZonedDateTime.now() + UUID.randomUUID());
        body.setTimestamp(ZonedDateTime.now().toString());
        body.setAmount(total);
        body.setOrder_reference("asdafd" + orderId);
        body.setCustomer_url("https://err.ee");
        body.setApi_username("e36eb40f5ec87fa2");

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("e36eb40f5ec87fa2", "7b91a3b9e1b74524c2e9fc282f8ac8cd");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EveryPayBody> entity = new HttpEntity<>(body, headers);

        EveryPayResponse response = restTemplate.exchange(url,  HttpMethod.POST, entity, EveryPayResponse.class).getBody();

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
}
