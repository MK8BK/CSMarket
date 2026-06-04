package io.mk8bk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckoutTest {

    @BeforeEach
    void init(){
    }
    @Test
    void scanItem() {
        Customer customer = new Customer(1, "firstname", "lastname", "username", "address", "password");
        CategoryBase categoryBase = new CategoryBase();
        Checkout checkout = new Checkout(customer, categoryBase);
        Item i = new Item("milk", new ItemCategory("DAIRY"),120, 500);
        checkout.scanItem(i, 50);
    }

    @Test
    void subscribeToPlan() {
        // TODO
    }

    @Test
    void computeBill() {
        // TODO
    }

    @Test
    void decrementInventory() {
        // TODO
    }
}