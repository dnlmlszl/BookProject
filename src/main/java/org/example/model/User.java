package org.example.model;

public class User {
    private int id;
    private String username;
    private String password;
    private Role role;

    public User(int id, String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
