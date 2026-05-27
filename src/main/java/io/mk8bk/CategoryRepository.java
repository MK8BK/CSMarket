package io.mk8bk;

import java.util.HashSet;

public class CategoryRepository {
    // for R6b
    private final HashSet<ItemCategory> categories;
    public CategoryRepository(){
        categories = new HashSet<>();
        addCategory("FRUIT_AND_VEGETABLES");
        addCategory("DAIRY");
        addCategory("MEAT");
    }

    public void addCategory(String categoryName){
        categories.add(new ItemCategory(categoryName));
    }

    public boolean hasCategory(String category){
        return categories.contains(new ItemCategory(category));
    }
}
