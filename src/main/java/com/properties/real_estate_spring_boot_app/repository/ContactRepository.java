package com.properties.real_estate_spring_boot_app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.properties.real_estate_spring_boot_app.model.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
}
