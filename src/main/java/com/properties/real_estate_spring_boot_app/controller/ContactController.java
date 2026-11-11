package com.properties.real_estate_spring_boot_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.properties.real_estate_spring_boot_app.model.Contact;
import com.properties.real_estate_spring_boot_app.service.ContactService;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    // 🔓 PUBLIC: Submit contact form
    @PostMapping("/contacts")
    public Contact createContact(@RequestBody Contact contact) {
        return contactService.saveContact(contact);
    }

    // 🔐 ADMIN: View all contacts
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/contacts")
    public List<Contact> getAll() {
        return contactService.getAllContacts();
    }

    // 🔐 ADMIN: Delete contact
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/contacts/{id}")
    public void delete(@PathVariable Long id) {
        contactService.deleteContact(id);
    }
}
