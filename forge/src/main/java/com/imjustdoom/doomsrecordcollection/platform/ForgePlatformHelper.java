package com.imjustdoom.doomsrecordcollection.platform;

import com.imjustdoom.doomsrecordcollection.DoomsRecordCollection;
import com.imjustdoom.doomsrecordcollection.platform.services.IPlatformHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DoomsRecordCollection.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DoomsRecordCollection.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DoomsRecordCollection.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), DoomsRecordCollection.MOD_ID);

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public <T extends Item> RegistryWrapper<T> registerItem(String id, Supplier<T> item) {
        ForgeWrapper<T> wrapper = new ForgeWrapper<>(ITEMS.register(id, item));
//        DoomsRecordCollectionForge.registerItem(wrapper);
        return wrapper;
    }

    @Override
    public <T extends Block> RegistryWrapper<T> registerBlock(String id, Supplier<T> block) {
        return new ForgeWrapper<>(BLOCKS.register(id, block));
    }

    @Override
    public <T extends BlockEntity> RegistryWrapper<BlockEntityType<T>> registerBlockEntity(String id, Supplier<BlockEntityType<T>> block) {
        return new ForgeWrapper<>(BLOCK_ENTITIES.register(id, block));
    }

    @Override
    public void registerTab(String id, CreativeModeTab tab) {
        TABS.register(id, () -> tab);
    }
}