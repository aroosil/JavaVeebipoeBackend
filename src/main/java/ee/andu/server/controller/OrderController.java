package ee.andu.server.controller;

import ee.andu.server.entity.Order;
import ee.andu.server.entity.Product;
import ee.andu.server.model.OrderPaid;
import ee.andu.server.model.ParcelMachine;
import ee.andu.server.model.PaymentLink;
import ee.andu.server.repository.OrderRepository;
import ee.andu.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("orders")
    public Iterable<Order> findAll(){
        return orderRepository.findAll();
    }

    @GetMapping("my-orders")
    public Iterable<Order> getMyOrdes(){
        Long personId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        System.out.println("personId:"+personId);
        return orderRepository.findByPerson_Id(personId);
    }

    @GetMapping("orders/{id}")
    public Order findById(@PathVariable Long id){
        return orderRepository.findById(id).orElseThrow();
    }

    @PostMapping("orders")
    public PaymentLink createOrder(@RequestParam String pmName, @RequestBody List<Product> products){
        Long personId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Order order = orderService.createOrder(personId, pmName, products);

        // miks vaja orderi salvestamist enne maksmist?
        // 1. vaja order id-d
        // 2. tehnilise vea korral on v2hemalt tellimus alles

        return orderService.makePayment(order.getId(), order.getTotal());
    }

    @GetMapping("orders-by-date")
    public List<Order> findByDate(@RequestParam Date startDate, @RequestParam Date endDate){
        return orderRepository.findAllByCreatedBetween(startDate, endDate);
    }

    @GetMapping("parcel-machines")
    public List<ParcelMachine> getParcelMachines() {
        return orderService.getParcelMachines();
    }

    @GetMapping("check-payment")
    public OrderPaid checkPayment(@RequestParam String order_reference, @RequestParam String payment_reference) {
        return orderService.checkPayment(order_reference, payment_reference);
    }

//    @GetMapping("payment")
//    public PaymentLink makePayment(@RequestParam Long order_reference, @RequestParam double total) {
//        return orderService.makePayment(order_reference, total);
//    }
}
