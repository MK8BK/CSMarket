package io.mk8bk;

import java.util.Objects;

public class Item {
    @Override
    public String toString() {
        return "Item[" +
                "name='" + name + '\'' +
                ", itemCategory=" + itemCategory +
                ", priceInCentimes=" + priceInCentimes +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name) && itemCategory == item.itemCategory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, itemCategory);
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    private ItemCategory itemCategory;
    private int priceInCentimes;
}
