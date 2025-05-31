package de.ixn075.smash.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GameInventory {

    private final Inventory inventory;
    private final Component name;
    private final InventoryEditor editor;
    private final int slots;

    public GameInventory(Component name) {
        this.slots = 9; // Default value
        this.name = name;
        this.inventory = Bukkit.createInventory(null, slots, name);
        this.editor = new InventoryEditor(this);
    }

    public GameInventory(Component name, int factor) {
        // Factor 6 is the highest valid number available to create an inventory in Minecraft.
        // This means the inventory will have 9 * 6 (= 54) slots
        // If factor is greater than 6, use 6 as factor.
        this.slots = factor > 6 ? 54 : 9 * factor;
        this.name = name;
        this.inventory = Bukkit.createInventory(null, slots, name);
        this.editor = new InventoryEditor(this);
    }

    public void accept(@NotNull Consumer<Inventory> consumer) {
        consumer.accept(inventory);
    }

    public GameInventory edit(@NotNull Consumer<InventoryEditor> consumer) {
        consumer.accept(editor);
        return this;
    }

    public Component getName() {
        return name;
    }

    public int getSlots() {
        return slots;
    }

    /**
     * A record class for an inventory edit instance.
     *
     * @param creator The instance of the inventory creator => {@link GameInventory}
     */
    public record InventoryEditor(GameInventory creator) {

        public void add(ItemStack itemStack) {
            creator.accept(inventory -> inventory.addItem(itemStack));
        }

        public void add(ItemStack... itemStack) {
            creator.accept(inventory -> inventory.addItem(itemStack));
        }

        public void set(int index, ItemStack itemStack) {
            creator.accept(inventory -> inventory.setItem(index, itemStack));
        }

        public void clear() {
            creator.accept(Inventory::clear);
        }

        public void remove(Material material) {
            creator.accept(inventory -> inventory.remove(material));
        }

        public void remove(ItemStack itemStack) {
            creator.accept(inventory -> inventory.remove(itemStack));
        }
    }
}
