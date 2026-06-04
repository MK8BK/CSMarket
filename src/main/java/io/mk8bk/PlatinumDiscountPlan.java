package io.mk8bk;

public class PlatinumDiscountPlan implements DiscountPlan {
    @Override
    public String getName() {
        return "PLATINUM";
    }

    @Override
    public void discount(Checkout checkout) {
        checkout.setTotalInCentimes((int)(70.0*checkout.getTotalInCentimes()/100.0));
        if(!checkout.getCustomer().hasPaidPlanFee())
            checkout.setTotalInCentimes(checkout.getTotalInCentimes()+20000);
    }

    @Override
    public int getPlanInitialPrice() {
        return 20000;
    }

    @Override
    public String toString(){
        return getName();
    }
}
