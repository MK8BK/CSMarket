package io.mk8bk;

import java.util.Objects;

public class Item {
    private final String name;
    private final ItemCategory itemCategory;
    private final int unitPriceCentimes;
    private final int weightPerUnit;

    public Item(String name, String categoryName, int unitPriceCentimes, int weightPerUnit) {
        this.name = name;
        this.unitPriceCentimes = unitPriceCentimes;
        this.weightPerUnit = weightPerUnit;
        itemCategory = new ItemCategory(categoryName);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return unitPriceCentimes == item.unitPriceCentimes && weightPerUnit == item.weightPerUnit && Objects.equals(name, item.name) && Objects.equals(itemCategory, item.itemCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, itemCategory, unitPriceCentimes, weightPerUnit);
    }

    public String getName() {
        return name;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public int getUnitPriceCentimes() {
        return unitPriceCentimes;
    }

    public int getWeightPerUnit() {
        return weightPerUnit;
    }
}
