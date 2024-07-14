package com.example.restdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping
    public Iterable<Person> getPersons() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findPersonById(@PathVariable int id) {
        Optional<Person> person = personService.findById(id);
        return person.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        return new ResponseEntity<>(personService.save(person), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable int id, @RequestBody Person person) {
        if (personService.existsById(id)) {
            person.setId(id);
            return new ResponseEntity<>(personService.save(person), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(personService.save(person), HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable int id) {
        if (personService.existsById(id)) {
            personService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{p_id}/message")
    public ResponseEntity<Person> addMessageToPerson(@PathVariable int p_id, @RequestBody Message message) {
        Optional<Person> personOptional = personService.addMessageToPerson(p_id, message);
        return personOptional.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{p_id}/message/{m_id}")
    public ResponseEntity<Void> deleteMessageFromPerson(@PathVariable int p_id, @PathVariable int m_id) {
        boolean isDeleted = personService.deleteMessageFromPerson(p_id, m_id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{p_id}/message")
    public ResponseEntity<List<Message>> getMessagesByPersonId(@PathVariable int p_id) {
        List<Message> messages = personService.getMessagesByPersonId(p_id);
        if (messages != null) {
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{p_id}/message/{m_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int p_id, @PathVariable int m_id) {
        Optional<Message> message = personService.getMessageById(p_id, m_id);
        return message.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}






