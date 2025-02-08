package org.example.service.user;

import org.example.model.Role;

public class AccessControl {
    public static boolean hasPermission(Role role, String action) {
        switch (role) {
            case ADMIN:
                return true;
            case USER:
                return action.equals("listBooks") || action.equals("searchBooks");
            case GUEST:
                return action.equals("listBooks");
            default:
                return false;
        }
    }
}
