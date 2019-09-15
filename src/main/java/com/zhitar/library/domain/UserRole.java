package com.zhitar.library.domain;

import com.zhitar.library.annotation.Table;

import java.io.Serializable;
import java.util.Objects;

@Table("user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer roleId;
    private Integer userId;

    public UserRole() {
    }

    public UserRole(Integer roleId, Integer userId) {
        this.roleId = roleId;
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(roleId, userRole.roleId) &&
                Objects.equals(userId, userRole.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, userId);
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "roleId=" + roleId +
                ", userId=" + userId +
                '}';
    }
}
