package com.properties.real_estate_spring_boot_app.controller;

import com.properties.real_estate_spring_boot_app.model.Property;
import com.properties.real_estate_spring_boot_app.service.PropertyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    /* ---------- PUBLIC ---------- */

    @GetMapping
    public Page<Property> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return propertyService.getAllProperties(page, size);
    }

    @GetMapping("/type/{type}")
    public Page<Property> byType(
            @PathVariable String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return propertyService.getPropertiesByType(type, page, size);
    }

    @GetMapping("/featured")
    public Page<Property> featured(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return propertyService.getFeaturedProperties(page, size);
    }

    @GetMapping("/{id}")
    public Property getById(@PathVariable Long id) {
        return propertyService.getPropertyById(id);
    }

    /* ---------- ADMIN ---------- */

    @PostMapping("/admin")
    public Property create(@RequestBody Property property) {
        return propertyService.createProperty(property);
    }

    @PutMapping("/admin/{id}")
    public Property update(
            @PathVariable Long id,
            @RequestBody Property property
    ) {
        return propertyService.updateProperty(id, property);
    }

    @DeleteMapping("/admin/{id}")
    public void delete(@PathVariable Long id) {
        propertyService.deleteProperty(id);
    }

    @PatchMapping("/admin/{id}/featured")
    public Property setFeatured(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, Boolean> body
    ) {
        return propertyService.setFeatured(id, Boolean.TRUE.equals(body.get("featured")));
    }
    
    @GetMapping("/search")
    public Page<Property> search(
            @RequestParam String location,
            @RequestParam Long minPrice,
            @RequestParam Long maxPrice,
            @RequestParam(defaultValue = "0") int page
    ) {
        return propertyService.search(location, minPrice, maxPrice, page, 6);
    }

}
