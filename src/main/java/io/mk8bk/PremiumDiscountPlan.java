package io.mk8bk;

public class PremiumDiscountPlan implements DiscountPlan {
    @Override
    public void discount(Checkout checkout) {
        // TODO: implement this plan
    }

    @Override
    public String toString() {
        return "PREMIUM";
    }
}
