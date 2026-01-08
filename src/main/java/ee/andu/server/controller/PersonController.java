package ee.andu.server.controller;

import ee.andu.server.entity.Person;
import ee.andu.server.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
//@CrossOrigin(origins = "http://localhost:5173")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("persons")
    public List<Person> getPersons(@RequestBody Person person){
        return personRepository.findAll();
    }

    @PostMapping("login")
    public String login(@RequestParam String email, @RequestParam String password){
        return "succ"; // temporary placeholder
    }

    @PostMapping("signup")
    public Person signup(@RequestBody Person person){
        // TODO: add validation

        return personRepository.save(person);
    }
}
