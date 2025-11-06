package com.imjustdoom.doomsrecordcollection;

import com.imjustdoom.doomsrecordcollection.block.ModBlocks;
import com.imjustdoom.doomsrecordcollection.block.renderer.RecordDisplayRenderer;
import com.imjustdoom.doomsrecordcollection.item.ModItems;
import com.imjustdoom.doomsrecordcollection.platform.ForgePlatformHelper;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DoomsRecordCollection.MOD_ID)
public class DoomsRecordCollectionForge {

    public DoomsRecordCollectionForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> {
            BlockEntityRenderers.register(ModBlocks.RECORD_DISPLAY_ENTITY.get(), RecordDisplayRenderer::new);
        });

        DoomsRecordCollection.DOOMS_RECORD_COLLECTION_TAB = CreativeModeTab.builder()
                .title(Component.translatable("category.doomsrecordcollection.tab"))
                .icon(() -> new ItemStack(ModItems.WALKMOB.get()))
                .displayItems((params, output) -> {
                    output.accept(ModItems.BASE_NEEDLE.get());
                    output.accept(ModItems.DIAMOND_NEEDLE.get());
                    output.accept(ModItems.WALKMOB.get());
                    ModBlocks.RECORD_DISPLAYS.forEach(block -> output.accept(block.get()));
                })
                .build();
        ForgePlatformHelper.TABS.register("tab", () -> DoomsRecordCollection.DOOMS_RECORD_COLLECTION_TAB);

        ForgePlatformHelper.ITEMS.register(modBus);
        ForgePlatformHelper.BLOCKS.register(modBus);
        ForgePlatformHelper.BLOCK_ENTITIES.register(modBus);
        ForgePlatformHelper.TABS.register(modBus);

        DoomsRecordCollection.init();
    }
}