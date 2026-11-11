package com.properties.real_estate_spring_boot_app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.properties.real_estate_spring_boot_app.model.Contact;
import com.properties.real_estate_spring_boot_app.repository.ContactRepository;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    // ✅ CREATE
    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    // ✅ READ
    public List<Contact> getAllContacts() {
        List<Contact> list = new ArrayList<>();
        contactRepository.findAll().forEach(list::add);
        return list;
    }

    // ✅ DELETE
    public void deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new RuntimeException("Contact not found");
        }
        contactRepository.deleteById(id);
    }
}
