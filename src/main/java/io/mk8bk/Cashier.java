package io.mk8bk;

public final class Cashier extends User {
    private final String firstname;
    private final String lastname;

    public Cashier(String username, String password, String firstname, String lastname) {
        super(username, password);
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public String toString() {
        return "Cashier{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
