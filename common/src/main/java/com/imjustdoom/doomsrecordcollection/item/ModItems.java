package com.imjustdoom.doomsrecordcollection.item;

import com.imjustdoom.doomsrecordcollection.platform.RegistryWrapper;
import com.imjustdoom.doomsrecordcollection.platform.Services;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ModItems {
    // NEEDLES
    public static final RegistryWrapper<Item> BASE_NEEDLE = register("base_needle", () -> new Item(new Item.Properties()));
    public static final RegistryWrapper<Item> DIAMOND_NEEDLE = register("diamond_needle", () -> new Item(new Item.Properties()));

    // PORTABLE
    public static final RegistryWrapper<Item> WALKMOB = register("walkmob", () -> new Item(new Item.Properties()));

    public static <T extends Item> RegistryWrapper<T> register(String id, Supplier<T> item) {
        return Services.PLATFORM.registerItem(id, item);
    }

    public static void init() {}
}
