package io.mk8bk;

public interface DiscountPlan {
    String getName();
    void discount(Checkout checkout);
    int getPlanInitialPrice();
}
