package io.mk8bk;

import java.util.HashMap;

public class Inventory {
    private final HashMap<String, Item> items;
    private final HashMap<String, Integer> quantities;

    public Inventory() {
        items = new HashMap<>();
        quantities = new HashMap<>();
    }
    public void addItem(Item i, int initialStock) throws ItemAlreadyPresentException {
        if(items.containsKey(i.getName())){
            throw new ItemAlreadyPresentException(i.getName());
        }
        items.put(i.getName(), i);
        quantities.put(i.getName(), initialStock);
    }
    public Item getItem(String itemName) throws NoSuchItemException {
        if(!items.containsKey(itemName))
            throw new NoSuchItemException(itemName);
        return items.get(itemName);
    }
    public int getItemStock(String itemName) throws NoSuchItemException {
        // they are always synchronized, no worries
        if(!items.containsKey(itemName))
            throw new NoSuchItemException(itemName);
        return quantities.get(itemName);
    }
    public void restock(String itemName, int quantity) throws NoSuchItemException, NegativeRestockingQuantity {
        if(!items.containsKey(itemName))
            throw new NoSuchItemException(itemName);
        if(quantity<0)
            throw new NegativeRestockingQuantity();
        quantities.put(itemName, quantities.get(itemName)+quantity);
    }

    public void destock(String itemName, int consumedQuantity) throws NoSuchItemException, NegativeDestockingQuantity, NegativeStockQuantity {
        if(!items.containsKey(itemName))
            throw new NoSuchItemException(itemName);
        if(consumedQuantity<0)
            throw new NegativeDestockingQuantity();
        if(quantities.get(itemName)<consumedQuantity)
            throw new NegativeStockQuantity();
        quantities.put(itemName, quantities.get(itemName)+consumedQuantity);
    }

    public static class ItemAlreadyPresentException extends Throwable {
        public ItemAlreadyPresentException(String itemName) {
            super("An item named `"+itemName+"` is already registered.");
        }
    }

    public static class NoSuchItemException extends Throwable {
        public NoSuchItemException(String itemName) {
            super("No item named `"+itemName+"` registered.");
        }
    }

    public static class NegativeRestockingQuantity extends Throwable {
        public NegativeRestockingQuantity() {
            super("Restocking with negative quantity is not allowed.");
        }
    }

    public static class NegativeDestockingQuantity extends Throwable {
        public NegativeDestockingQuantity(){
            super("Destocking with negative quantity is not allowed");
        }
    }

    public static class NegativeStockQuantity extends Throwable {
        public NegativeStockQuantity(){
            super("Negative stock is not allowed, destocking with quantity larger than inventory stock");
        }
    }
}
