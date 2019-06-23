package com.test.demo.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String username;

    private String email;

    private Long address_id;

    private String phone;

    private String website;

    private Long company_id;

    public User() {
    }

    public User(String name, String username, String email, Long address_id, String phone, String website, Long company_id) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.address_id = address_id;
        this.phone = phone;
        this.website = website;
        this.company_id = company_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Long address_id) {
        this.address_id = address_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(address_id, user.address_id) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(website, user.website) &&
                Objects.equals(company_id, user.company_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, username, email, address_id, phone, website, company_id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address_id +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", company=" + company_id +
                '}';
    }
}
