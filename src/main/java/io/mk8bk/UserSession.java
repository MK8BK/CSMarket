package io.mk8bk;

import java.util.Objects;

public class UserSession {
    private User loggedInUser;

    public boolean isUserLoggedIn() {
        return userLoggedIn;
    }

    private boolean userLoggedIn;
    private final UserBase userBase;

    public User getLoggedInUser(){
        return loggedInUser;
    }


    public UserSession(UserBase userBase) {
        if(userBase==null){
            throw new RuntimeException("Session needs proper user base.");
        }
        this.userBase = userBase;
        loggedInUser = null;
        userLoggedIn = false;
    }

    public void login(String username, String password) throws UserAlreadyLoggedInException, UserBase.NoSuchUserException, InvalidPasswordException {
        if(userLoggedIn){
            throw new UserAlreadyLoggedInException();
        }
        User u = userBase.getUser(username);
        if(!Objects.equals(password, u.getPassword())){
            throw new InvalidPasswordException(username);
        }
        loggedInUser = u;
        userLoggedIn = true;
    }

    public void logout() throws NoUserLoggedInException {
        if(!userLoggedIn){
            throw new NoUserLoggedInException();
        }
        loggedInUser = null;
        userLoggedIn = false;
    }

    public static class UserAlreadyLoggedInException extends Throwable {
        public UserAlreadyLoggedInException() {
            super("A user is already logged in.");
        }
    }

    public static class NoUserLoggedInException extends Throwable {
        public NoUserLoggedInException(){
           super("No user is currently logged in.");
        }
    }

    public static class InvalidPasswordException extends Throwable {
        public InvalidPasswordException(String username) {
            super("Invalid password for user `"+username+"`.");
        }
    }
}
