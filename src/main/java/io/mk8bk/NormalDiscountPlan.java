package io.mk8bk;

public class NormalDiscountPlan implements DiscountPlan {
    @Override
    public void discount(Cart cart) {
        // do nothing
        return;
    }

    @Override
    public String toString() {
        return "NORMAL";
    }
}
