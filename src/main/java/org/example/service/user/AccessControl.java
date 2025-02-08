package org.example.service.user;

import org.example.model.Role;

public class AccessControl {
    public static boolean hasPermission(Role role, String action) {
        switch (role) {
            case ADMIN:
                return true;
            case USER:
                return action.equals("listBooks") ||
                        action.equals("searchBooks") ||
                        action.equals("loadFromDatabase") ||
                        action.equals("saveToFile") ||
                        action.equals("loadFromTextFile") ||
                        action.equals("saveToBinaryFile") ||
                        action.equals("loadFromBinaryFile");
            case GUEST:
                return action.equals("listBooks") ||
                        action.equals("searchBooks") ||
                        action.equals("loadFromDatabase");
            default:
                return false;
        }
    }
}
