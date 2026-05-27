package io.mk8bk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerBaseTest {

    @Test
    void getCustomerCount() {
        CustomerBase base = new CustomerBase();
        assertEquals(1, base.getCustomerCount());
        Customer defaultCustomer = new Customer(0, "customer", "the first",
                "defaultCustomer", "bikini bottom", "secret");
        assertDoesNotThrow(()->{
            base.getCustomer("defaultCustomer");
        });
        try {
            assertEquals(defaultCustomer,
                    base.getCustomer("defaultCustomer"));
        } catch (CustomerBase.NoSuchCustomerException e) {
            // dead branch, but meh
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerCustomer() {
        CustomerBase base = new CustomerBase();
        assertDoesNotThrow(()-> {
                base.registerCustomer("jafar", "vizir", "jafar", "lsj", "yasmine");
            }
        );
        assertEquals(2, base.getCustomerCount());
        assertDoesNotThrow(()->{
                    base.getCustomer("jafar");
                }
        );
        try {
            Customer jafar = base.getCustomer("jafar");
        } catch (CustomerBase.NoSuchCustomerException e) {
            // dead branch
            throw new RuntimeException(e);
        }
    }


    @Test
    void subscribeToPlan() {
        CustomerBase base = new CustomerBase();
        assertDoesNotThrow(()-> {
                    base.registerCustomer("jafar", "vizir", "jafar", "lsj", "yasmine");
                }
        );
        assertEquals(2, base.getCustomerCount());
        assertDoesNotThrow(()->{
                    base.getCustomer("jafar");
                }
        );
        try {
            Customer jafar = base.getCustomer("jafar");
            base.subscribeToPlan("jafar", new PremiumDiscountPlan());
            assertEquals(PremiumDiscountPlan.class, jafar.getPlan().getClass());
        } catch (CustomerBase.NoSuchCustomerException e) {
            // dead branch
            throw new RuntimeException(e);
        }
    }
}