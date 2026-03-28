package ee.andu.server.controller;

import ee.andu.server.entity.Person;
import ee.andu.server.dto.PersonLoginDto;
//import ee.andu.server.entity.PersonRole;
import ee.andu.server.repository.PersonRepository;
import ee.andu.server.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("profile")
    public Person getProfile(){
        Long personId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return personRepository.findById(personId).orElseThrow();
    }

    @GetMapping("persons")
    public List<Person> getPersons(){
        return personRepository.findAll();
    }

    @PostMapping("login")
    public String login(@RequestBody PersonLoginDto personLoginDto){
        Person dbPerson = personRepository.findByEmail(personLoginDto.getEmail());
        if (dbPerson == null) {
            return "Invalid email!";
        }
        if  (!dbPerson.getPassword().equals(personLoginDto.getPassword())) {
            return "Invalid password!";
        }
        return jwtService.generateToken(dbPerson); // Algoritm.Payload.v6ti
    }

    @PostMapping("signup")
    public Person signup(@RequestBody Person person){
        if (personRepository.findByEmail(person.getEmail()) != null) {
            throw new RuntimeException("Email already exists!");
        }
//      person.setRole(PersonRole.CUSTOMER);

        return personRepository.save(person);
    }

    @PutMapping("profile")
    public Person updateProfile(@RequestBody Person person){
        Long personId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        Person sameEmailPerson = personRepository.findByEmail(person.getEmail());
        if (sameEmailPerson != null && !personId.equals(sameEmailPerson.getId())) {
            throw new RuntimeException("Email already exists!");
        } else if (!person.getId().equals(personId)) {
            throw new RuntimeException("Cannot update another profile!");
        }
        return personRepository.save(person);
    }
}
