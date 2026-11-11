package com.properties.real_estate_spring_boot_app.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "properties")
public class Property {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String title;
private String description;
private String location;
private Long price; // store in smallest currency unit or use long
private String type; // BUY/RENT

// ✅ NEW FIELDS
private Integer bedrooms;
private Integer bathrooms;
private Integer squareFeet;

// ✅ MULTIPLE IMAGE URLs
@ElementCollection
@CollectionTable(
    name = "property_images",
    joinColumns = @JoinColumn(name = "property_id")
)
@Column(name = "image_url", length = 1000)
private List<String> imageUrls = new ArrayList<>();

private boolean featured = false;


// getters/setters
public Property(){}
public Long getId(){return id;} public void setId(Long id){this.id=id;}
public String getTitle(){return title;} public void setTitle(String t){this.title=t;}
public String getDescription(){return description;} public void setDescription(String d){this.description=d;}
public String getLocation(){return location;} public void setLocation(String l){this.location=l;}
public Long getPrice(){return price;} public void setPrice(Long p){this.price=p;}
public String getType(){return type;} public void setType(String type){this.type=type;}
public List<String> getImageUrls() {
    return imageUrls;
}
public void setImageUrls(List<String> imageUrls) {
    this.imageUrls = imageUrls;
}

public boolean isFeatured(){return featured;} public void setFeatured(boolean f){this.featured=f;}

public Integer getBedrooms() {
    return bedrooms;
}

public void setBedrooms(Integer bedrooms) {
    this.bedrooms = bedrooms;
}

public Integer getBathrooms() {
    return bathrooms;
}

public void setBathrooms(Integer bathrooms) {
    this.bathrooms = bathrooms;
}

public Integer getSquareFeet() {
    return squareFeet;
}

public void setSquareFeet(Integer squareFeet) {
    this.squareFeet = squareFeet;}}

