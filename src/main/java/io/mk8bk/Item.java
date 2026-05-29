package io.mk8bk;

import java.util.Objects;

public class Item {
    private final String name;
    private final ItemCategory itemCategory;
    private final int unitPriceInCentimes;
    private final int weightPerUnitInGrams;

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", itemCategory='" + itemCategory.categoryName() +
                "', unitPriceInCentimes=" + unitPriceInCentimes +
                ", weightPerUnitInGrams=" + weightPerUnitInGrams +
                '}';
    }

    public Item(String name, ItemCategory category, int unitPriceInCentimes, int weightPerUnitInGrams) {
        this.name = name;
        this.unitPriceInCentimes = unitPriceInCentimes;
        this.weightPerUnitInGrams = weightPerUnitInGrams;
        itemCategory = category;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return unitPriceInCentimes == item.unitPriceInCentimes && weightPerUnitInGrams == item.weightPerUnitInGrams && Objects.equals(name, item.name) && Objects.equals(itemCategory, item.itemCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, itemCategory, unitPriceInCentimes, weightPerUnitInGrams);
    }

    public String getName() {
        return name;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public int getUnitPriceInCentimes() {
        return unitPriceInCentimes;
    }

    public int getWeightPerUnitInGrams() {
        return weightPerUnitInGrams;
    }
}
