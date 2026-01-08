package ee.andu.server.controller;

import ee.andu.server.model.Supplier1Product;
import ee.andu.server.model.Supplier2Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RestController
public class SupplierControlller {

    @Autowired
    RestTemplate restTemplate;
    // tarnija
    // nt hiinast v6tame tooteid, peame manuaalselt k2ima aliaxpressis vms vaatamas
    // nende poolt on api endpoint, et saaks n2ha tooteid, mida saab osta

    @GetMapping("supplier1")
    public List<Supplier1Product> getProductsFromSupplier1() {

        String url = "https://fakestoreapi.com/products";
        Supplier1Product[] body = restTemplate.exchange(url,  HttpMethod.GET, null, Supplier1Product[].class).getBody();
        return Arrays.stream(body).filter(e -> e.getRating().getRate() > 3.0).toList();
    }

    @GetMapping("supplier2")
    public List<Supplier2Product> getProductsFromSupplier2() {

        String url = "https://api.escuelajs.co/api/v1/products";
        Supplier2Product[] body = restTemplate.exchange(url,  HttpMethod.GET, null, Supplier2Product[].class).getBody();
        return Arrays.stream(body).filter(e -> e.getPrice() > 3.0).sorted(Comparator.comparing(Supplier2Product::getPrice)).toList();
    }

}
