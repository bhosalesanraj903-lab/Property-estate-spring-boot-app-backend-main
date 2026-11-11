package com.properties.real_estate_spring_boot_app.service;

import com.properties.real_estate_spring_boot_app.model.Property;
import com.properties.real_estate_spring_boot_app.repository.PropertyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    /* ---------------- CREATE ---------------- */

    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    /* ---------------- UPDATE ---------------- */

    public Property setFeatured(Long id, boolean featured) {
        Property existing = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        existing.setFeatured(featured);
        return propertyRepository.save(existing);
    }

    public Property updateProperty(Long id, Property updatedProperty) {

        Property existing = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        existing.setTitle(updatedProperty.getTitle());
        existing.setDescription(updatedProperty.getDescription());
        existing.setLocation(updatedProperty.getLocation());
        existing.setPrice(updatedProperty.getPrice());
        existing.setType(updatedProperty.getType());
        existing.setImageUrls(updatedProperty.getImageUrls());
        existing.setFeatured(updatedProperty.isFeatured());
        existing.setBedrooms(updatedProperty.getBedrooms());
        existing.setBathrooms(updatedProperty.getBathrooms());
        existing.setSquareFeet(updatedProperty.getSquareFeet());

        
        return propertyRepository.save(existing);
    }

    /* ---------------- DELETE ---------------- */

    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }

    /* ---------------- READ ---------------- */

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    public Page<Property> getAllProperties(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return propertyRepository.findAll(pageable);
    }

    public Page<Property> getPropertiesByType(String type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return propertyRepository.findByType(type.toUpperCase(), pageable);
    }

    public Page<Property> getFeaturedProperties(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return propertyRepository.findByFeaturedTrue(pageable);
    }
    
    public Page<Property> search(
            String location,
            Long minPrice,
            Long maxPrice,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return propertyRepository
                .findByLocationContainingIgnoreCaseAndPriceBetween(
                        location,
                        minPrice,
                        maxPrice,
                        pageable
                );
    }
}
