package io.mk8bk;

public class NormalDiscountPlan implements DiscountPlan {
    @Override
    public void discount(Checkout checkout) {
        // do nothing
        return;
    }

    @Override
    public String toString() {
        return "NORMAL";
    }
}
