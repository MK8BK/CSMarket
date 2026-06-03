package io.mk8bk;

import java.util.HashMap;

public class Checkout {
    private final HashMap<String, Item> items;
    private final HashMap<String, Integer> itemCounts;
    private final Customer customer;

    public Checkout(Customer customer) {
        itemCounts = new HashMap<>();
        items = new HashMap<>();
        this.customer = customer;
    }

    public void scanItem(Item i, int quantity) {
        if (items.containsKey(i.getName())) {
            items.put(i.getName(), i);
            itemCounts.put(i.getName(), quantity + itemCounts.get(i.getName()));
        } else {
            items.put(i.getName(), i);
            itemCounts.put(i.getName(), quantity);
        }
    }
    public void subscribeToPlan(DiscountPlan plan){
        customer.setPlan(plan);
    }
    int computeBillCentimes(){
        return 0;
    }
}
