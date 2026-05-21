package io.mk8bk;

import java.util.ArrayList;
import java.util.HashMap;


public class CustomerSession {
    // non encrypted, haha
    private ArrayList<Customer> customers;
    private ArrayList<String> customerPasswords;
    private ArrayList<String> customerAddresses;
    private HashMap<String, Long> usernameToId;
    private boolean userLoggedIn;
    private String loggedInUsername;

    public CustomerSession(){
        customerPasswords = new ArrayList<>();
        customerAddresses = new ArrayList<>();
        usernameToId = new HashMap<>();
        userLoggedIn = false;
        loggedInUsername = null; }

    public void login(String username, String password) throws UserAlreadyLoggedInException, NoSuchUsernameException, IncorrectPassword {
        if (userLoggedIn) {
            throw new UserAlreadyLoggedInException();
        }
        if (!usernameToId.containsKey(username)) {
            throw new NoSuchUsernameException(username);
        } else if (password == null) { // not necessary
            throw new RuntimeException("[CATASTROPHIC ERROR: PASSWORD IS NULL.]");
        }
        long id = usernameToId.get(username);
        // id < static id counter == customerPasswords.length()
        String goodPass = customerPasswords.get((int) id);
        if (!goodPass.equals(password)) {
            throw new IncorrectPassword(username);
        } else {
            loggedInUsername = username;
            userLoggedIn = true;
        }
    }

    public void logout() throws NoUserLoggedInException {
        if (!userLoggedIn) {
            throw new NoUserLoggedInException();
        }
        System.out.println("Logged out.");
        userLoggedIn = false;
        loggedInUsername = null;
    }

    public void registerCustomer(String firstname, String lastname, String username, String address, String password) throws UsernameAlreadyExistsException {
        if (usernameToId.containsKey(username)) {
            throw new UsernameAlreadyExistsException(username);
        }
        if (password == null) {
            throw new RuntimeException("[NULL PASSWORD]");
        }
        Customer customer = new Customer(firstname, lastname);
        customerAddresses.add(address);
        customerPasswords.add(password);
        usernameToId.put(username, customer.getID());
    }

    public class UserAlreadyLoggedInException extends Throwable {
        public UserAlreadyLoggedInException() {
            super();
        }
    }

    public class NoSuchUsernameException extends Throwable {
        public NoSuchUsernameException(String username) {
            super(username);
        }
    }

    public class IncorrectPassword extends Throwable {
        public IncorrectPassword(String username) {
            super(username);
        }
    }

    public class NoUserLoggedInException extends Throwable {
    }

    public class UsernameAlreadyExistsException extends Throwable {
        public UsernameAlreadyExistsException(String username) {
            super(username);
        }
    }
}
