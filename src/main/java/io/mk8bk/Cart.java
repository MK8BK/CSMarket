package io.mk8bk;

import java.util.HashMap;

public class Cart {
    private final HashMap<String, Item> items;
    private final HashMap<String, Integer> itemCounts;

    public Cart() {
        itemCounts = new HashMap<>();
        items = new HashMap<>();
    }

    public void scanItem(Item i, int quantity){
        if(items.containsKey(i.getName())){
            items.put(i.getName(), i);
            itemCounts.put(i.getName(), quantity+itemCounts.get(i.getName()));
        }else{
           items.put(i.getName(), i);
           itemCounts.put(i.getName(), quantity);
        }
    }
}
