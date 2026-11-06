package de.ixn075.smash.api.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class Skull {

    private final ItemStack itemStack;

    private final SkullMeta skullMeta;

    private Skull() {
        throw new UnsupportedOperationException("This class can not be instantiated.");
    }

    public Skull(int amount, Component component) {
        this(amount, component, null);
    }

    public Skull(int amount, Component displayName, List<Component> lore) {
        this.itemStack = new ItemStack(Material.PLAYER_HEAD, amount);
        this.skullMeta = (SkullMeta) itemStack.getItemMeta();
        if (displayName != null) this.skullMeta.displayName(displayName);
        if (lore != null) this.skullMeta.lore(lore);
    }

    public Skull lore(List<Component> lore) {
        skullMeta.lore(lore);
        return this;
    }

    /**
     * Sets the enchantment of the item.
     *
     * @param enchantment The enchantment of the item.
     * @return The Item instance.
     */
    public Skull enchant(Enchantment enchantment, int level) {
        skullMeta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Sets the item flags of the item.
     *
     * @param itemFlags The item flags of the item.
     * @return The Item instance.
     */
    public Skull itemFlags(ItemFlag... itemFlags) {
        skullMeta.addItemFlags(itemFlags);
        return this;
    }

    /**
     * Builds the item and returns it with a {@link Consumer <ItemStack>}.
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
        if (itemStack != null && skullMeta != null) {
            itemStack.setItemMeta(skullMeta);
            return itemStack;
        }
        return null;
    }
}
