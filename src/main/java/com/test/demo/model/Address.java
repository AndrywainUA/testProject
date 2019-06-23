package com.test.demo.model;

import org.springframework.data.geo.Point;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue
    private Long id;

    private String street;

    private String suite;

    private String city;

    private String zipcode;

    private Point point;

    public Address() {
    }

    public Address(String street, String suite, String city, String zipcode, Point point) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipcode = zipcode;
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id) &&
                Objects.equals(street, address.street) &&
                Objects.equals(suite, address.suite) &&
                Objects.equals(city, address.city) &&
                Objects.equals(zipcode, address.zipcode) &&
                Objects.equals(point, address.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street, suite, city, zipcode, point);
    }

    @Override
    public String toString() {
        return "Long{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", suite='" + suite + '\'' +
                ", city='" + city + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", point=" + point +
                '}';
    }
}
