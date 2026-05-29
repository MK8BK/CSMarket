package io.mk8bk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    @Test
    void addItem() {
        Inventory inventory = new Inventory();
        CategoryRepository categoryRepository = new CategoryRepository();
        assertDoesNotThrow(() -> {
            Item i = new Item("milk", new ItemCategory("DAIRY"), 200, 1000);
            inventory.addItem(i, 50);
        });
        assertThrows(Inventory.ItemAlreadyPresentException.class, () -> {
            Item i = new Item("milk", new ItemCategory("DAIRY"), 200, 1000);
            inventory.addItem(i, 20);
        });
        try {
            assertEquals("milk", inventory.getItem("milk").getName());
        } catch (Inventory.NoSuchItemException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getItem() {
        Inventory inventory = new Inventory();
        assertThrows(Inventory.NoSuchItemException.class, () -> {
            inventory.getItem("unavailable");
        });
        assertDoesNotThrow(() -> {
                    Item i = new Item("milk", new ItemCategory("DAIRY"), 200, 1000);
                    inventory.addItem(i, 20);
                    inventory.getItem("milk");
                }
        );
    }

    @Test
    void restockDestock() {
        Inventory inventory = new Inventory();
        Item i = new Item("milk", new ItemCategory("DAIRY"), 200, 1000);
        try {
            inventory.addItem(i, 500);
        } catch (Inventory.ItemAlreadyPresentException e) {
            // dead branch normally
            fail(e.getMessage());
        }
        assertThrows(Inventory.NegativeRestockingQuantity.class, ()->{
            inventory.restock("milk", -2);
        });
        assertDoesNotThrow(()->{
            assertEquals(500, inventory.getItemStock("milk"));
            inventory.restock("milk", 200);
            assertEquals(700, inventory.getItemStock("milk"));
        });
        assertThrows(Inventory.NoSuchItemException.class, ()->{
            inventory.restock("cheese", 2);
        });
        assertThrows(Inventory.NoSuchItemException.class, ()->{
            inventory.destock("cheese", 2);
        });
        assertDoesNotThrow(()->{
            assertEquals(700, inventory.getItemStock("milk"));
            inventory.destock("milk", 200);
            assertEquals(500, inventory.getItemStock("milk"));
        });
        assertThrows(Inventory.NegativeDestockingQuantity.class, ()->{
            inventory.destock("milk", -1);
        });
        assertDoesNotThrow(()->{
            assertEquals(500, inventory.getItemStock("milk"));
            inventory.destock("milk", 500);
            assertEquals(0, inventory.getItemStock("milk"));
        });
        assertThrows(Inventory.NegativeStockQuantity.class, ()->{
            assertEquals(0, inventory.getItemStock("milk"));
            inventory.destock("milk", 1);
        });
    }
}