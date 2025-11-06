package com.imjustdoom.doomsrecordcollection.platform;

import com.imjustdoom.doomsrecordcollection.DoomsRecordCollection;
import com.imjustdoom.doomsrecordcollection.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <T extends Item> RegistryWrapper<T> registerItem(String id, Supplier<T> item) {
        return new FabricWrapper<>(Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(DoomsRecordCollection.MOD_ID, id), item.get()));
    }

    @Override
    public <T extends Block> RegistryWrapper<T> registerBlock(String id, Supplier<T> block) {
        return new FabricWrapper<>(Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(DoomsRecordCollection.MOD_ID, id), block.get()));
    }

    @Override
    public <T extends BlockEntity> RegistryWrapper<BlockEntityType<T>> registerBlockEntity(String id, Supplier<BlockEntityType<T>> block) {
        return new FabricWrapper<>(Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(DoomsRecordCollection.MOD_ID, id), block.get()));
    }

    @Override
    public void registerTab(String id, CreativeModeTab tab) {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(DoomsRecordCollection.MOD_ID, id), tab);
    }
}
