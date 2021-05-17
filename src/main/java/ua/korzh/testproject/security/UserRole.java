package ua.korzh.testproject.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum UserRole {

    ADMIN(Arrays.asList(UserPermition.MODIFY, UserPermition.WRITE, UserPermition.READ)),
    USER(Arrays.asList(UserPermition.READ, UserPermition.WRITE));

    private List<UserPermition> permitions;

    UserRole(List<UserPermition> permitions) {
        this.permitions = permitions;
    }

    public List<UserPermition> getPermitions() {
       return this.permitions;
    }
}
