package com.zhitar.library.domain;


import com.zhitar.library.annotation.Table;
import com.zhitar.library.validation.annotation.Email;
import com.zhitar.library.validation.annotation.Length;
import com.zhitar.library.validation.annotation.Phone;

import java.util.*;

@Table("users")
public class User extends AbstractEntity<Integer> {

    private static final long serialVersionUID = 1L;

    @Length(min = 2, max = 64, message = "message.username.error")
    private String name;
    @Email(message = "message.useremail.error")
    private String email;
    @Length(min = 5, max = 64, message = "message.userpassword.error")
    private String password;
    @Phone(pattern = "^0(95|50|66|99|67|96|97|98|63|93|73)\\d{7}", message = "message.userphone.error")
    private String phone;
    private Set<Role> roles = new HashSet<>();
    private List<Book> books = new ArrayList<>();

    public User() {
    }

    public User(Integer id, String name, String email, String password, String phone) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.roles.add(new Role(null, "USER"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public boolean hasRole(String role) {
        for (Role userRole : roles) {
            if (userRole.getRole().equals(role)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
