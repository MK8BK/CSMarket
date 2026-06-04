package io.mk8bk;

import java.util.HashMap;

public class Checkout {
    private final HashMap<String, Item> items;
    private final HashMap<String, Integer> itemCounts;
    private final Customer customer;
    private final CategoryBase categoryBase;
    private int totalInCentimes;
    private int totalWeightInGrams;
    private boolean billComputed;
    private final static int DELIVERY_PERCENTAGE_PRICE = 15;
    private final static int DELIVERY_PERCENTAGE_WEIGHT = 15;

    public Checkout(Customer customer, CategoryBase categoryBase) {
        itemCounts = new HashMap<>();
        items = new HashMap<>();
        this.customer = customer;
        this.categoryBase = categoryBase;
        totalInCentimes = 0;
        billComputed = false;
        totalWeightInGrams = 0;
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
        customer.resetPlanPayment();
    }

    public String computeBill(){
        // might need some refactoring, no time unfortunately
        totalInCentimes = 0;
        totalWeightInGrams = 0;
        StringBuilder r = new StringBuilder();
        r.append(String.format("Bill for customer %s\n", customer.username));
        for(String itemName : items.keySet()){
            Item i = items.get(itemName);
            ItemCategory category = i.getItemCategory();
            int quantity = itemCounts.get(itemName);
            int categoryDiscount = 100;
            try {
                categoryDiscount = categoryBase.getCategoryDiscount(category.categoryName());
            } catch (CategoryBase.NoSuchCategoryException e) {
                // catastrophic error: (let it die, let it die, let it shrivel up and die)
                throw new RuntimeException(e);
            }
            totalWeightInGrams += quantity * i.getWeightPerUnitInGrams();
            int noDiscountPrice = (i.getUnitPriceInCentimes()*quantity);
            int discount = (int)((categoryDiscount)*i.getUnitPriceInCentimes()*quantity/100);
            totalInCentimes += noDiscountPrice;
            r.append(String.format("%-30s quantity: %-5d price: %-6.2f EUR\n", itemName, quantity, noDiscountPrice/100.0));
            if(0 != discount){
                r.append(String.format("\t%-30s items have a %d%% discount applied: -%-6.2f EUR\n", category.categoryName(), categoryDiscount,
                        discount/100.0));
                totalInCentimes -= discount;
            }
        }
        r.append(String.format("Total: %-6.2f EUR\n", totalInCentimes/100.0));
        if(!customer.hasPaidPlanFee()){
            String planName = customer.getPlan().getName();
            double planPrice = customer.getPlan().getPlanInitialPrice()/100.0;
            r.append(String.format("%s one-time annual fee: %6.2f EUR\n", planName, planPrice));
        }
        customer.getPlan().discount(this);
        r.append(String.format("Total after discount plan: %-6.2f EUR\n", totalInCentimes/100.0));
        billComputed = true;
        return r.toString();
    }

    public boolean isBillComputed(){
        return billComputed;
    }
    public int getTotalInCentimes() {
        return totalInCentimes;
    }

    // only used by discountPlan
    public void setTotalInCentimes(int totalInCentimes) {
        this.totalInCentimes = totalInCentimes;
    }

    // only used by discountPlan
    public Customer getCustomer() {
        return customer;
    }

    public void decrementInventory(Inventory inventory) {
        for(String itemName : items.keySet()){
            try {
                inventory.destock(itemName, itemCounts.get(itemName));
            } catch (Inventory.NoSuchItemException |
                     Inventory.NegativeDestockingQuantity |
                     Inventory.NegativeStockQuantity e) {
                // catastrophic error, (let it die, let it die, let it shrivel up and die)
                throw new RuntimeException(e);
            }
        }
    }

    public int getTotalWeightInGrams() {
        return totalWeightInGrams;
    }

    public int computeDeliveryFee(int distance) throws ImpossibleDelivery {
        if(totalWeightInGrams>50000)
            throw new ImpossibleDelivery(totalWeightInGrams);
        if(customer.getPlan().getClass()==PlatinumDiscountPlan.class)
            return 0;
        int cost = 0;
        if(totalWeightInGrams<=10000 && distance<=30)
            cost = 1500; // centimes
        if(totalWeightInGrams>10000){
            cost += (int)(totalInCentimes*DELIVERY_PERCENTAGE_PRICE/100.0);
            // 1 euro for every extra kilogram
            cost += (int)((totalWeightInGrams-10000)/1000.0);
            // 1 euro for every extra kilometer
            cost += ((distance-30));
        }
        if(customer.getPlan().getClass()==PrimeDiscountPlan.class){
            cost /= 2;
        }
        totalInCentimes += cost;
        return cost;
    }

    public static class ImpossibleDelivery extends Throwable {
        public final int weight;
        public ImpossibleDelivery(int weight) {
            super("Delivery impossible for checkout of weight "+weight/1000+"kg > 50kg.");
            this.weight = weight;
        }
    }
}
