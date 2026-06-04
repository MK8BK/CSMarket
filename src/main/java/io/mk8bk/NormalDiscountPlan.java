package io.mk8bk;

public class NormalDiscountPlan implements DiscountPlan {
    @Override
    public String getName() {
        return "NORMAL";
    }

    @Override
    public String toString() {
        return getName();
    }



    @Override
    public void discount(Checkout checkout) {
        // do nothing
        return;
    }

    @Override
    public int getPlanInitialPrice() {
        return 0;
    }

}
