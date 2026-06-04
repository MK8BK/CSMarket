package io.mk8bk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryBaseTest {

    @Test
    void addCategory() {
        CategoryBase categoryBase = new CategoryBase();
        assertDoesNotThrow(() -> {
            categoryBase.addCategory("DAIRY");
        });
        assertThrows(CategoryBase.CategoryAlreadyRegisteredException.class, ()->{
            categoryBase.addCategory("DAIRY");

        });
        assertTrue(categoryBase.hasCategory("DAIRY"));
        assertDoesNotThrow(()->{
            ItemCategory itemCategory = categoryBase.getCategory("DAIRY");
            assertEquals("DAIRY", itemCategory.categoryName());
        });
    }

    @Test
    void setCategoryDiscount() {
        CategoryBase categoryBase = new CategoryBase();
        assertDoesNotThrow(()->{
            categoryBase.addCategory("DAIRY");
            categoryBase.setCategoryDiscount("DAIRY", 30);
        });
        assertThrows(CategoryBase.InvalidCategoryDiscountPercent.class, ()->{
            categoryBase.setCategoryDiscount("DAIRY", -1);
        });
        assertThrows(CategoryBase.InvalidCategoryDiscountPercent.class, ()->{
            categoryBase.setCategoryDiscount("DAIRY", 101);
        });
        assertThrows(CategoryBase.NoSuchCategoryException.class, ()->{
            categoryBase.setCategoryDiscount("DAIRy", 50);
        });
    }
}