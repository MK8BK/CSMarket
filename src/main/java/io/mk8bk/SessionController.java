package io.mk8bk;

public class SessionController {
    CustomerSession customerSession;
    public SessionController(){
       customerSession = new CustomerSession();
    }

    public void login(String username, String password) {
        try {
            customerSession.login(username, password);
        } catch (CustomerSession.UserAlreadyLoggedInException e) {
            System.out.println("A user is already logged in, please logout first.");
        } catch (CustomerSession.NoSuchUsernameException e) {
            System.out.println("No user with username `" + username +
                   "` registered. Please register first (or `ceo`:`12345678`).");
        } catch (CustomerSession.IncorrectPassword e) {
            System.out.println("Invalid password.");
        }
    }

    public void logout() {
        try {
            customerSession.logout();
        } catch (CustomerSession.NoUserLoggedInException e) {
            System.out.println("No user logged in, can't logout.");
        }
    }

    public void registerCustomer(String firstname, String lastname, String username, String address, String password) {
        try {
            customerSession.registerCustomer(firstname, lastname, username, address, password);
        } catch (CustomerSession.UsernameAlreadyExistsException e) {
            System.out.println("A user with username `" + username + "` already exists.");
        }

    }
    //System.out.println("Password is empty.");
    //System.out.println("Customer "+username+" was successfully registered.");
    //System.out.println("Customer "+username+" is logged in.");
}
