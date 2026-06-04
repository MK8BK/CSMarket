package io.mk8bk;

public class PrimeDiscountPlan implements DiscountPlan {
    @Override
    public String getName() {
        return "PRIME";
    }

    @Override
    public void discount(Checkout checkout) {
        if(checkout.getTotalInCentimes()>=5000)
            checkout.setTotalInCentimes((int)(80.0*checkout.getTotalInCentimes()/100.0));
        if(!checkout.getCustomer().hasPaidPlanFee())
            checkout.setTotalInCentimes(checkout.getTotalInCentimes()+5000);
    }

    @Override
    public int getPlanInitialPrice() {
        return 5000;
    }

    @Override
    public String toString(){
       return getName();
    }
}
