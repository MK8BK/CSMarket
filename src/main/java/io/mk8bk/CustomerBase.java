package io.mk8bk;

import java.util.HashMap;

public class CustomerBase {
    private int idCounter;
    private final HashMap<String, Customer> usernameToCustomer;

    @Override
    public String toString() {
        return usernameToCustomer.toString();
    }

    public int getCustomerCount(){
        return idCounter;
    }

    public CustomerBase(){
        usernameToCustomer = new HashMap<>();
        idCounter = 0;
        try {
            registerCustomer("customer", "the first", "defaultCustomer", "bikini bottom", "secret");
        } catch (CustomerAlreadyRegisteredException e) {
            // dead branch, but still need to handle it
            throw new RuntimeException(e);
        }
    }
    public void registerCustomer(String firstname, String lastName, String username, String address, String password) throws CustomerAlreadyRegisteredException {
        if(firstname==null || lastName==null || address==null
            || password==null || username==null)
            throw new RuntimeException("Null argument during customer registration");
        if(usernameToCustomer.containsKey(username)){
            throw new CustomerAlreadyRegisteredException(username);
        }
        Customer n = new Customer(idCounter, firstname, lastName, username, address, password);
        usernameToCustomer.put(username, n);
        ++idCounter;
    }

    public void subscribeToPlan(String username, DiscountPlan plan) throws NoSuchCustomerException {
        if(!usernameToCustomer.containsKey(username)){
           throw new NoSuchCustomerException(username);
        }
        usernameToCustomer.get(username).setPlan(plan);
    }


    public Customer getCustomer(String username) throws NoSuchCustomerException {
        if(!usernameToCustomer.containsKey(username)){
            throw new NoSuchCustomerException(username);
        }
        return usernameToCustomer.get(username);
    }

    public static class CustomerAlreadyRegisteredException extends Throwable {
        public final String username;
        public CustomerAlreadyRegisteredException(String username){
            super("A customer with username `"+username+"` is already registered.");
            this.username = username;
        }
    }

    public static class NoSuchCustomerException extends Throwable {
        public final String username;
        public NoSuchCustomerException(String username) {
            super("No customer with the username `"+username+"` exists.");
            this.username = username;
        }
    }
}
