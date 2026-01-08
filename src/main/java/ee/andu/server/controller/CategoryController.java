package ee.andu.server.controller;

import ee.andu.server.entity.Category;
import ee.andu.server.repository.CategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@CrossOrigin(origins = "*")
//@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("categories")
    public List<Category> getCategory()
    {
        return categoryRepository.findAll() ;
    }

    @PostMapping("categories")
    public ResponseEntity<List<Category>> saveCategory(@RequestBody Category category)
    {
        log.info("Lisatakse kategooriat!");

        if (category.getId() != null)
        {
            throw new RuntimeException("Cannot add with Id");
        }
        if (category.getName() == null || category.getName().isBlank())
        {
            throw new RuntimeException("Name is required");
        }
        if (categoryRepository.findByName(category.getName()) != null)
        {
            throw new RuntimeException("Category name already exists: " +  category.getName());
        }
        categoryRepository.save(category);

        return ResponseEntity.status(201).body(categoryRepository.findAll());
    }

    @DeleteMapping("categories/{id}")
    public ResponseEntity<List<Category>> deleteCategoryById(@PathVariable Long id)
    {
        try {
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Cannot delete when in use!");
        }
        return ResponseEntity.status(200).body(categoryRepository.findAll());
    }

    //    @PutMapping("categories")
    //    public List<Category> updateProduct(@RequestBody Category category)
    //    {
    //        if (category.getId() == null)
    //        {
    //            throw new RuntimeException("Cannot update product without id");
    //        }
    //        categoryRepository.save(category);
    //        return categoryRepository.findAll();
    //    }
}
