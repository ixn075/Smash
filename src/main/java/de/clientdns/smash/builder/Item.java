package de.clientdns.smash.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * This class is a wrapper and provides a fluent API for creating {@link ItemStack} classes.
 */
public class Item {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    @SuppressWarnings("unused")
    private Item() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
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
        if (material == null) throw new NullPointerException("Material cannot be null.");
        else {
            this.itemStack = new ItemStack(material, amount);
            this.itemMeta = itemStack.getItemMeta();
            if (displayName != null) this.itemMeta.displayName(displayName);
            if (lore != null && !lore.isEmpty()) this.itemMeta.lore(lore);
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

    public List<Component> lore() {
        return itemMeta.lore();
    }

    public Item lore(List<Component> lore) {
        itemMeta.lore(lore);
        return this;
    }

    /**
     * Gets the amount of the item.
     *
     * @return The amount of the item.
     */
    public int amount() {
        return itemStack.getAmount();
    }

    /**
     * Sets the amount of the item.
     *
     * @return The amount of the item.
     */
    public Item amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * Gets the material of the item.
     *
     * @return The material of the item.
     */
    public Material type() {
        return itemStack.getType();
    }

    /**
     * Sets the material of the item.
     *
     * @param material The material of the item.
     * @return The material of the item.
     */
    public Item type(Material material) {
        itemStack.setType(material);
        return this;
    }

    /**
     * Gets the unbreakable state of the item.
     *
     * @return The unbreakable state of the item.
     */
    public boolean unbreakable() {
        return itemMeta.isUnbreakable();
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
     * Gets the enchantments of the item.
     *
     * @return The enchantments of the item.
     */
    public Map<Enchantment, Integer> enchants() {
        return itemMeta.getEnchants();
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
     * Gets the item flags of the item.
     *
     * @return The item flags of the item.
     */
    public Set<ItemFlag> itemFlags() {
        return itemMeta.getItemFlags();
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
    public ItemStack build() {
        if (itemStack != null && itemMeta != null) {
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        return null;
    }
}
