package io.mk8bk;

import java.util.HashMap;

public class UserBase {
    // a user base should always have at least 1 manager: named ceo here
    private final HashMap<String, User> users;

    public UserBase() {
        users = new HashMap<>();
    }

    public void registerCashier(String firstname, String lastname, String username, String password) throws UserAlreadyRegisteredException {
        Cashier c = new Cashier(username, password, firstname, lastname);
        addUser(c);
    }

    public void registerManager(String username, String password) throws UserAlreadyRegisteredException {
        Manager m = new Manager(username, password);
        addUser(m);
    }

    public User getUser(String username) throws NoSuchUserException {
        if (!users.containsKey(username)) {
            throw new NoSuchUserException(username);
        }
        return users.get(username);
    }

    private void addUser(User user) throws UserAlreadyRegisteredException {
        if (users.containsKey(user.getUsername())) {
            throw new UserAlreadyRegisteredException(user.getUsername());
        }
        users.put(user.getUsername(), user);
    }

    public int getUserCount() {
        return users.size();
    }

    public static class NoSuchUserException extends Throwable {
        public NoSuchUserException(String username) {
            super("No user with username `" + username + "` has been registered.");
        }
    }

    public static class UserAlreadyRegisteredException extends Throwable {
        public UserAlreadyRegisteredException(String username) {
            super("A user with username `" + username + "` has already been registered.");
        }
    }
}
