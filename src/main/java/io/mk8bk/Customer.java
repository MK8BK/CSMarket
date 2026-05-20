package io.mk8bk;

import java.util.Objects;

public class Customer {

    // a good enough method to have unique ids,
    // assuming no concurrent instanciations
    private static long IDCounter = 0;
    final private long ID;
    private String firstname;
    private String surname;
    private CustomerPlan plan;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return ID == customer.ID;
    }

    Customer(String firstname, String surname, CustomerPlan plan){
        this.ID = IDCounter;
        IDCounter++;
        this.surname = surname;
        this.firstname = firstname;
        this.plan = plan;
    }

    Customer(String firstname, String surname){
        this.ID = IDCounter;
        IDCounter++;
        this.surname = surname;
        this.firstname = firstname;
        this.plan = CustomerPlan.NORMAL;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }


    public long getID() {
        return ID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public CustomerPlan getPlan() {
        return plan;
    }

    public void setPlan(CustomerPlan plan) {
        this.plan = plan;
    }
}
