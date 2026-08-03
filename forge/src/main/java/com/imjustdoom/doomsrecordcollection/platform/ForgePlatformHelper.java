package com.imjustdoom.doomsrecordcollection.platform;

import com.imjustdoom.doomsrecordcollection.DoomsRecordCollection;
import com.imjustdoom.doomsrecordcollection.block.renderer.RecordDisplayItemRenderer;
import com.imjustdoom.doomsrecordcollection.item.RecordDisplayItem;
import com.imjustdoom.doomsrecordcollection.platform.services.IPlatformHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DoomsRecordCollection.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DoomsRecordCollection.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DoomsRecordCollection.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), DoomsRecordCollection.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DoomsRecordCollection.MOD_ID);

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
    public <T extends RecipeSerializer<?>> RegistryWrapper<T> registerRecipeSerializer(String id, Supplier<T> serializer) {
        return new ForgeWrapper<>(RECIPE_SERIALIZERS.register(id, serializer));
    }

    @Override
    public void registerTab(String id, CreativeModeTab tab) {
        TABS.register(id, () -> tab);
    }

    @Override
    public RecordDisplayItem recordDisplayItem(Block block, Item.Properties properties) {
        return new RecordDisplayItem(block, properties) {
            @Override
            public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                consumer.accept(RecordDisplayItemRenderer.EXTENSIONS);
            }
        };
    }
}