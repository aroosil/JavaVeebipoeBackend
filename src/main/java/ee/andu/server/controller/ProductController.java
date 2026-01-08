package ee.andu.server.controller;

import ee.andu.server.entity.Product;
import ee.andu.server.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@CrossOrigin(origins = "*")
//@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // BASE URL + API endpoint
    // localhost:8080/hi
    //    @GetMapping("hi")
    //    public String hello()
    //    {
    //        return "Hello World";
    //    }

    @GetMapping("products")
    public Page<Product> getProducts(@RequestParam Long categoryId, Pageable pageable)
    {
        if (categoryId==0) {
            return productRepository.findByActiveTrue(pageable);
        }
        return productRepository.findByActiveTrueAndCategory_Id(pageable, categoryId);
    }

    @GetMapping("admin-products")
    public List<Product> getAdminProducts()
    {
        return productRepository.findByOrderByIdAsc();
    }

    @PostMapping("products")
    public List<Product> saveProduct(@RequestBody Product product)
    {
        if (product.getId() != null)
        {
            throw new RuntimeException("Cannot add with Id");
        }
        if (product.getCategory() == null || product.getCategory().getId() == null) {
            throw new RuntimeException("Cannot add without Category or Category Id being null");
        }
        productRepository.save(product);
        return productRepository.findByOrderByIdAsc();
    }

    // DELETE localhost:8080/products/id
    @DeleteMapping("products/{id}")
    public List<Product> deleteProductById(@PathVariable Long id)
    {
        productRepository.deleteById(id);
        return productRepository.findAll(); // SELECT * FROM products
    }

    // DELETE localhost:8080/products?id=2
    //    @DeleteMapping("products")
    //    public List<Product> deleteProduct(@RequestParam Long id)
    //    {
    //        productRepository.deleteById(id);
    //        return productRepository.findAll(); // SELECT * FROM products
    //    }

    @GetMapping("products/{id}")
    public Product getProductById(@PathVariable Long id)
    {
        return productRepository.findById(id).orElseThrow();
    }

    @PutMapping("products")
    public Product updateProduct(@RequestBody Product product)
    {
        if (product.getId() == null)
        {
            throw new RuntimeException("Cannot update product without Id");
        }
        return productRepository.save(product);
    }

    // erinev lähenemine
    //    @RequestMapping(value = "hi2", method = RequestMethod.GET)
    //    public String hello2()
    //    {
    //        return "Hello World 2";
    //    }
}
