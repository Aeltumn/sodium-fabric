package net.caffeinemc.mods.sodium.client.model.color;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

/**
 * Wraps around an instance of {@link ItemColor} and caches its result
 * per tint index. This is intended specifically to use to cache intermediate
 * color lookups throughout one render call. This means the {@link ItemStack}
 * instance should remain the same throughout all usages of this wrapper.
 *
 * The purpose of this wrapper is to avoid running color lookups for each quad
 * with the same tint index while rendering. It caches the color of a certain
 * tint so a model with many tinted quads doesn't need to perform repeated
 * color lookups.
 */
public class ItemColorWrapper implements ItemColor {

    private final ItemColor wrapped;
    private HashMap<Integer, Integer> cache;

    public ItemColorWrapper(ItemColor wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public int getColor(ItemStack itemStack, int tintIndex) {
        if (cache == null) {
            // Create the cache object if it doesn't exist yet
            cache = new HashMap<>();
        }

        // Use a cached value per tint index
        var value = cache.get(tintIndex);
        if (value != null) {
            return value;
        }

        // If not present, determine the value and cache it
        var result = wrapped.getColor(itemStack, tintIndex);
        cache.put(tintIndex, result);
        return result;
    }
}