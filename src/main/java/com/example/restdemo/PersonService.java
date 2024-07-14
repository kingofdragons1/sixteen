package com.example.restdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MessageRepository messageRepository;

    public Iterable<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public boolean existsById(int id) {
        return personRepository.existsById(id);
    }

    public void deleteById(int id) {
        personRepository.deleteById(id);
    }

    public Optional<Person> addMessageToPerson(int personId, Message message) {
        Optional<Person> personOptional = personRepository.findById(personId);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            message.setPerson(person);
            message.setTime(LocalDateTime.now());
            person.addMessage(message);
            messageRepository.save(message); // Сохраняем только сообщение
            return Optional.of(person);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteMessageFromPerson(int personId, int messageId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            Optional<Message> messageOptional = messageRepository.findById(messageId);
            if (messageOptional.isPresent() && messageOptional.get().getPerson().equals(person)) {
                messageRepository.deleteById(messageId);
                return true;
            }
        }
        return false;
    }

    public List<Message> getMessagesByPersonId(int personId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        return personOptional.map(Person::getMessages).orElse(null);
    }

    public Optional<Message> getMessageById(int personId, int messageId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            return person.getMessages().stream()
                    .filter(message -> message.getId() == messageId)
                    .findFirst();
        }
        return Optional.empty();
    }
}
