package com.imjustdoom.doomsrecordcollection;

import com.imjustdoom.doomsrecordcollection.block.ModBlocks;
import com.imjustdoom.doomsrecordcollection.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class DoomsRecordCollectionFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DoomsRecordCollection.DOOMS_RECORD_COLLECTION_TAB = FabricItemGroup.builder()
                .icon(() -> new ItemStack(ModItems.WALKMOB.get()))
                .title(Component.translatable("category.doomsrecordcollection.tab"))
                .displayItems((params, output) -> {
                    output.accept(ModItems.BASE_NEEDLE.get());
                    output.accept(ModItems.DIAMOND_NEEDLE.get());
                    output.accept(ModItems.WALKMOB.get());
                    ModBlocks.RECORD_DISPLAYS.forEach(block -> output.accept(block.get()));
                })
                .build();

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(DoomsRecordCollection.MOD_ID, "tab"), DoomsRecordCollection.DOOMS_RECORD_COLLECTION_TAB);

        DoomsRecordCollection.init();
    }
}
