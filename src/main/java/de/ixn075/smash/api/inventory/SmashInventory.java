package de.ixn075.smash.api.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SmashInventory {

    private Inventory inventory;
    private Component name;
    private Editor editor;
    private int slots;

    public SmashInventory(Component name) throws IllegalArgumentException {
        new SmashInventory(name, 1);
    }

    public SmashInventory(Component name, int factor) throws IllegalArgumentException {
        // Factor 6 is the highest valid number available to create an inventory in Minecraft.
        // This means the inventory will have 9 * 6 (= 54) slots
        // If factor is greater than 6, use 6 as factor.
        this.slots = factor > 6 ? 54 : 9 * factor;
        this.name = name;
        this.inventory = Bukkit.createInventory(null, slots, name);
        this.editor = new Editor(this);
    }

    public void then(@NotNull Consumer<Inventory> consumer) {
        consumer.accept(inventory);
    }

    public SmashInventory edit(@NotNull Consumer<Editor> consumer) {
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
     * @param creator The instance of the inventory creator
     */
    public record Editor(SmashInventory creator) {

        public void add(ItemStack itemStack) {
            creator.then(inventory -> inventory.addItem(itemStack));
        }

        public void add(ItemStack... itemStack) {
            creator.then(inventory -> inventory.addItem(itemStack));
        }

        public void set(int index, ItemStack itemStack) {
            creator.then(inventory -> inventory.setItem(index, itemStack));
        }

        public void clear() {
            creator.then(Inventory::clear);
        }

        public void remove(Material material) {
            creator.then(inventory -> inventory.remove(material));
        }

        public void remove(ItemStack itemStack) {
            creator.then(inventory -> inventory.remove(itemStack));
        }
    }
}
