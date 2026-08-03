package com.imjustdoom.doomsrecordcollection.platform.services;

import com.imjustdoom.doomsrecordcollection.item.RecordDisplayItem;
import com.imjustdoom.doomsrecordcollection.platform.RegistryWrapper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    <T extends Item> RegistryWrapper<T> registerItem(String id, Supplier<T> item);
    <T extends Block> RegistryWrapper<T> registerBlock(String id, Supplier<T> block);
    <T extends BlockEntity> RegistryWrapper<BlockEntityType<T>> registerBlockEntity(String id, Supplier<BlockEntityType<T>> block);
    <T extends RecipeSerializer<?>> RegistryWrapper<T> registerRecipeSerializer(String id, Supplier<T> serializer);
    void registerTab(String id, CreativeModeTab tab);

    /**
     * The record display item draws itself from NBT, and Forge can only be told that through the item
     * class, so each loader supplies its own {@link RecordDisplayItem}.
     */
    RecordDisplayItem recordDisplayItem(Block block, Item.Properties properties);
}