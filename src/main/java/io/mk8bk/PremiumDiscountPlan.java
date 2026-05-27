package io.mk8bk;

public class PremiumDiscountPlan implements DiscountPlan{
    @Override
    public void discount(Cart cart) {
        // TODO: implement this plan
    }
    @Override
    public String toString(){
        return "PREMIUM";
    }
}
