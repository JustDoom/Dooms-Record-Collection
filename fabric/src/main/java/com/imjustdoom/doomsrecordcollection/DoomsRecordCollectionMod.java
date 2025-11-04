package com.imjustdoom.doomsrecordcollection;

import com.imjustdoom.doomsrecordcollection.block.ModBlocks;
import com.imjustdoom.doomsrecordcollection.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DoomsRecordCollectionMod implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();

        CommonClass.DOOMS_RECORD_COLLECTION_TAB = FabricItemGroup.builder()
                .icon(() -> new ItemStack(ModItems.WALKMOB))
                .title(Component.translatable("category.doomsrecordcollection.tab"))
                .displayItems((params, output) -> {
                    output.accept(ModItems.BASE_NEEDLE);
                    output.accept(ModItems.DIAMOND_NEEDLE);
                    output.accept(ModItems.WALKMOB);
                    ModBlocks.RECORD_DISPLAYS.forEach((colour, block) -> output.accept(block));
                })
                .build();

        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CommonClass.MOD_ID, "base_needle"), ModItems.BASE_NEEDLE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CommonClass.MOD_ID, "diamond_needle"), ModItems.DIAMOND_NEEDLE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CommonClass.MOD_ID, "walkmob"), ModItems.WALKMOB);

        ModBlocks.RECORD_DISPLAYS.forEach((colour, block) -> {
            Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(CommonClass.MOD_ID, colour.getName() + "_record_display"), block);
            Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CommonClass.MOD_ID, colour.getName() + "_record_display"), new BlockItem(block, new Item.Properties()));
        });

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(CommonClass.MOD_ID, "tab"), CommonClass.DOOMS_RECORD_COLLECTION_TAB);
    }
}
