package io.mk8bk;

import java.util.HashMap;
import java.util.Map;

public class CategoryBase {
    // for R6b
    private final Map<String, ItemCategory> categories;
    // 30% discount is registered as 30
    private final Map<String, Integer> categoryDiscounts;

    public CategoryBase() {
        categories = new HashMap<>();
        categoryDiscounts = new HashMap<>();
    }

    public void addCategory(String categoryName) throws CategoryAlreadyRegisteredException {
        if (hasCategory(categoryName)) throw new CategoryAlreadyRegisteredException(categoryName);
        categories.put(categoryName, new ItemCategory(categoryName));
        categoryDiscounts.put(categoryName, 0);
    }

    public boolean hasCategory(String categoryName) {
        return categories.containsKey(categoryName);
    }

    public ItemCategory getCategory(String categoryName) throws NoSuchCategoryException {
        if (!hasCategory(categoryName)) throw new NoSuchCategoryException(categoryName);
        return categories.get(categoryName);
    }

    public int getCategoryDiscount(String categoryName) throws NoSuchCategoryException {
        if (!hasCategory(categoryName)) throw new NoSuchCategoryException(categoryName);
        return categoryDiscounts.get(categoryName);
    }

    public void setCategoryDiscount(String categoryName, int discount) throws NoSuchCategoryException, InvalidCategoryDiscountPercent {
        if (!hasCategory(categoryName)) throw new NoSuchCategoryException(categoryName);
        if (discount < 0 || discount > 100) throw new InvalidCategoryDiscountPercent(discount);
        categoryDiscounts.put(categoryName, discount);
    }

    public static class NoSuchCategoryException extends Throwable {
        public final String categoryName;

        public NoSuchCategoryException(String categoryName) {
            super("No category named `" + categoryName + "` registered in inventory.");
            this.categoryName = categoryName;
        }
    }

    public static class CategoryAlreadyRegisteredException extends Throwable {
        public final String categoryName;

        public CategoryAlreadyRegisteredException(String categoryName) {
            super("No category named `" + categoryName + "` registered in inventory.");
            this.categoryName = categoryName;
        }
    }

    public static class InvalidCategoryDiscountPercent extends Throwable {
        public final int discount;

        public InvalidCategoryDiscountPercent(int discount) {
            super("Discount: " + discount + " is invalid; valid range is [0, 100].");
            this.discount = discount;
        }
    }
}
