package com.example.restdemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PersonRepository personRepository;

    @GetMapping
    public Iterable<Message> getMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Message> findMessageById(@PathVariable int id) {
        return messageRepository.findById(id);
    }

    @PostMapping
    public ResponseEntity<Message> addMessage(@RequestBody Message message, @RequestParam int personId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            message.setPerson(person);
            messageRepository.save(message);
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable int id, @RequestBody Message message) {
        if (messageRepository.existsById(id)) {
            message.setId(id);
            return new ResponseEntity<>(messageRepository.save(message), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(messageRepository.save(message), HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable int id) {
        messageRepository.deleteById(id);
    }
}
