package de.ixn075.smash.api.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

/**
 * This class is a wrapper and provides a fluent API for creating {@link ItemStack} classes.
 */
public class Item {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    private Item() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new Item with the given {@link Material}, amount and display name.
     *
     * @param material    The material of the item.
     * @param amount      The amount of the item.
     * @param displayName The display name of the item.
     */
    public Item(Material material, int amount, Component displayName) {
        this(material, amount, displayName, null);
    }

    /**
     * Creates a new Item with the given {@link Material}, amount, display name and lore.
     *
     * @param material    The material of the item.
     * @param amount      The amount of the item.
     * @param displayName The display name of the item.
     * @param lore        The lore of them item.
     */
    public Item(Material material, int amount, Component displayName, List<Component> lore) {
        if (material == null) throw new NullPointerException("Material can not be null.");
        else {
            this.itemStack = new ItemStack(material, amount);
            this.itemMeta = itemStack.getItemMeta();
            if (displayName != null) this.itemMeta.displayName(displayName);
            if (lore != null) this.itemMeta.lore(lore);
        }
    }

    /**
     * Gets the display name of the item.
     *
     * @return The display name of the item.
     */
    public Component name() {
        return itemMeta.displayName();
    }

    /**
     * Sets the display name of the item.
     *
     * @param name The name of the item.
     * @return The Item instance.
     */
    public Item name(Component name) {
        itemMeta.displayName(name);
        return this;
    }

    /**
     * Sets the displayed lore of the item.
     *
     * @param lore The lore of the item.
     * @return The Item instance.
     */
    public Item lore(List<Component> lore) {
        itemMeta.lore(lore);
        return this;
    }

    /**
     * Sets the amount of the item.
     *
     * @param amount The amount of the item.
     * @return The amount of the item.
     */
    public Item amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * Sets the unbreakable state of the item.
     *
     * @param unbreakable The unbreakable state.
     * @return The Item instance.
     */
    public Item unbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Sets the enchantment of the item.
     *
     * @param enchantment The enchantment of the item.
     * @return The Item instance.
     */
    public Item enchant(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Sets the item flags of the item.
     *
     * @param itemFlags The item flags of the item.
     * @return The Item instance.
     */
    public Item itemFlags(ItemFlag... itemFlags) {
        itemMeta.addItemFlags(itemFlags);
        return this;
    }

    /**
     * Builds the item and returns it with a {@link Consumer<ItemStack>}.
     */
    public void build(@NotNull Consumer<ItemStack> item) {
        item.accept(build());
    }

    /**
     * Builds the item and returns it as a {@link ItemStack}.
     *
     * @return The final ItemStack result.
     */
    public @Nullable ItemStack build() {
        if (itemStack != null && itemMeta != null) {
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        return null;
    }
}
