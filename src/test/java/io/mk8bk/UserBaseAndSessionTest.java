package io.mk8bk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserBaseAndSessionTest {

    @Test
    void loginLogoutTest() {
        UserBase userBase = new UserBase();
        assertDoesNotThrow(()->{
            userBase.registerManager("ceo", "123456789");
        });
        UserSession session1 = new UserSession(userBase);
        UserSession session2 = new UserSession(userBase);
        UserSession session3 = new UserSession(userBase);
        assertDoesNotThrow(()->{
            session1.login("ceo", "123456789");
        });
        assertThrows(UserBase.NoSuchUserException.class, ()->{
            session2.login("eo", "123456789");
        });
        assertThrows(UserSession.InvalidPasswordException.class, ()->{
            session3.login("ceo", "023456789");
        });
        assertThrows(UserSession.UserAlreadyLoggedInException.class, ()->{
            session1.login("ceo", "123456789");
        });
        assertThrows(UserSession.NoUserLoggedInException.class, session2::logout);
        assertDoesNotThrow(session1::logout);
    }

    @Test
    void registerCashierTest(){
        UserBase userBase = new UserBase();
        assertDoesNotThrow(()->{
            userBase.registerManager("ceo", "123456789");
        });
        assertEquals(1, userBase.getUserCount());
        assertDoesNotThrow(()->{
            Manager ceo = (Manager)userBase.getUser("ceo");
        });
        UserSession session1 = new UserSession(userBase);
        UserSession session2 = new UserSession(userBase);
        assertDoesNotThrow(()-> {
            session1.login("ceo", "123456789");
            session1.logout();
        });
        assertDoesNotThrow(()->{
            userBase.registerCashier("jamal", "jamal", "jamal", "password");
            Cashier c = (Cashier) userBase.getUser("jamal");
        });
        assertThrows(UserBase.NoSuchUserException.class, ()->{
            userBase.getUser("j");
        });
        assertThrows(UserBase.UserAlreadyRegisteredException.class, ()-> {
            userBase.registerCashier("jamal", "jamal", "jamal", "password");
        });
        assertThrows(UserBase.UserAlreadyRegisteredException.class, ()-> {
            userBase.registerCashier("jeff", "jeff", "jamal", "jeff");
        });
        assertThrows(UserBase.UserAlreadyRegisteredException.class, ()->{
            userBase.registerCashier("p", "p", "jamal", "p");
        });


    }

    @Test
    void registerManagerTest(){
        UserBase userBase = new UserBase();
        assertDoesNotThrow(()->{
            userBase.registerManager("ceo", "123456789");
        });
        UserSession session1 = new UserSession(userBase);
        UserSession session2 = new UserSession(userBase);
        assertThrows(UserBase.UserAlreadyRegisteredException.class, ()->{
            userBase.registerManager("ceo", "password");
        });
        assertDoesNotThrow(()->{
            userBase.registerManager("jafar","password");
        });
        assertDoesNotThrow(()->{
            session1.login("jafar","password");
        });
        assertThrows(UserSession.InvalidPasswordException.class, ()->{
            session2.login("jafar","psk");
        });
        assertThrows(UserSession.UserAlreadyLoggedInException.class, ()->{
            session1.login("jafar","password");
        });
    }
}