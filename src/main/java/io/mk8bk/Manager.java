package io.mk8bk;

public final class Manager extends User {
    // bare minimum
    public Manager(String username, String password) {
        super(username, password);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "username='" + username + '\'' +
                '}';
    }
}
