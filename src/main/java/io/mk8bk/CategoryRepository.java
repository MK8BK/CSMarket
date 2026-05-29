package io.mk8bk;

import java.util.HashMap;

public class CategoryRepository {
    // for R6b
    private final HashMap<String, ItemCategory> categories;

    public CategoryRepository() {
        categories = new HashMap<>();
        try {
            addCategory("FRUIT_AND_VEGETABLES");
            addCategory("DAIRY");
            addCategory("MEAT");
        } catch (CategoryAlreadyRegisteredException e) {
            // dead branch
        }
    }

    public void addCategory(String categoryName) throws CategoryAlreadyRegisteredException {
        if (categories.containsKey(categoryName))
            throw new CategoryAlreadyRegisteredException(categoryName);
        categories.put(categoryName, new ItemCategory(categoryName));
    }

    public boolean hasCategory(String categoryName) {
        return categories.containsKey(categoryName);
    }

    public ItemCategory getCategory(String categoryName) throws NoSuchCategoryException {
        if (!hasCategory(categoryName))
            throw new NoSuchCategoryException(categoryName);
        return categories.get(categoryName);
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
}
