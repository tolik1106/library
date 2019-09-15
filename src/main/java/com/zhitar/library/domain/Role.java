package com.zhitar.library.domain;


import com.zhitar.library.validation.annotation.Length;

import java.util.Objects;

public class Role extends AbstractEntity<Integer> {

    private static final long serialVersionUID = 1L;

    @Length(min = 3, max = 30, message = "Length must be between 3 and 30")
    private String role;

    public Role() {
    }

    public Role(Integer id, String role) {
        super(id);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return Objects.equals(role, role1.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), role);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                '}';
    }

}
