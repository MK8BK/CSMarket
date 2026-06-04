package io.mk8bk;

import java.util.Objects;

public class Customer {
    final public String firstname;
    // DONE
    final public String lastname;
    final public int id;
    final public String username;
    final public String address;
    final public String password;
    private DiscountPlan plan;
    private boolean paidPlanFee;
    Customer(int id, String firstname, String lastname, String username, String address, String password, DiscountPlan plan) {
        this.lastname = lastname;
        this.id = id;
        this.firstname = firstname;
        this.username = username;
        this.address = address;
        this.password = password;
        this.plan = plan;
        if(Objects.equals(plan.getClass(), NormalDiscountPlan.class))
            paidPlanFee = true;
    }

    Customer(int id, String firstname, String lastname, String username, String address, String password) {
        this(id, firstname, lastname, username, address, password, new NormalDiscountPlan());
    }

    @Override
    public String toString() {
        return "Customer{" + "firstname='" + firstname + '\'' + ", lastname='" + lastname + '\'' + ", id=" + id + ", username='" + username + '\'' + ", plan=" + plan + ", address='" + address + '\'' + '}';
    }

    public DiscountPlan getPlan() {
        return plan;
    }

    public void setPlan(DiscountPlan plan) {
        this.plan = plan;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id && Objects.equals(firstname, customer.firstname)
                && Objects.equals(lastname, customer.lastname) && Objects.equals(username, customer.username)
                && Objects.equals(address, customer.address) && Objects.equals(password, customer.password) &&
                Objects.equals(plan.getClass(), customer.plan.getClass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, id, username, address, password, plan);
    }

    public boolean hasPaidPlanFee() {
        return paidPlanFee;
    }

    public void payPlanFee() {
        this.paidPlanFee = true;
    }

    public void resetPlanPayment() {
        this.paidPlanFee = false;
    }


}
