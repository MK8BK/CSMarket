package io.mk8bk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CustomerTest {

    @Test
    public void testToString() {
        Customer customer = new Customer(1, "firstname", "lastname", "username",
                "address", "password", new PremiumDiscountPlan());
        assertEquals("Customer{firstname='firstname', lastname='lastname', id=1, username='username', plan=PREMIUM, address='address'}", customer.toString());
    }

    @Test
    public void testEquals() {
        Customer customer = new Customer(1, "firstname", "lastname", "username",
                "address", "password", new PremiumDiscountPlan());
        Customer customer2 = new Customer(2, "firstname", "lastname", "username",
                "address", "password", new PremiumDiscountPlan());
        assertNotEquals(customer2, customer);
        Customer customer3 = new Customer(1, "firstnamE", "lastname", "username",
                "address", "password", new PremiumDiscountPlan());
        assertNotEquals(customer3, customer);
        Customer customer4 = new Customer(1, "firstname", "lastname", "username",
                "address", "password", new PremiumDiscountPlan());
        assertEquals(customer4, customer);
    }

    @Test
    public void testHashCode() {
    }

    @Test
    public void testGetPlan() {
        Customer customer = new Customer(1, "firstname", "lastname", "username",
                "address", "password", new PremiumDiscountPlan());
        Customer customer2 = new Customer(1, "firstname", "lastname", "username",
                "address", "password");
        assertEquals(PremiumDiscountPlan.class, customer.getPlan().getClass());
        assertEquals(NormalDiscountPlan.class, customer2.getPlan().getClass());
    }

    @Test
    public void testSetPlan() {
        Customer customer = new Customer(1, "firstname", "lastname", "username",
                "address", "password", new PremiumDiscountPlan());
        Customer customer2 = new Customer(1, "firstname", "lastname", "username",
                "address", "password");
        customer.setPlan(new PlatinumDiscountPlan());
        customer2.setPlan(new PremiumDiscountPlan());
        assertEquals(PlatinumDiscountPlan.class, customer.getPlan().getClass());
        assertEquals(PremiumDiscountPlan.class, customer2.getPlan().getClass());
    }
}