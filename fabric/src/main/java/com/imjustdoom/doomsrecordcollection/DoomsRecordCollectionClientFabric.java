package com.imjustdoom.doomsrecordcollection;

import com.imjustdoom.doomsrecordcollection.block.ModBlocks;
import com.imjustdoom.doomsrecordcollection.block.renderer.RecordDisplayModel;
import com.imjustdoom.doomsrecordcollection.block.renderer.RecordDisplayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class DoomsRecordCollectionClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(ModBlocks.RECORD_DISPLAY_ENTITY.get(), RecordDisplayRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.RECORD_DISPLAY.get(),
                (stack, context, poseStack, buffers, light, overlay) ->
                        RecordDisplayModel.renderItem(stack, poseStack, buffers, light, overlay));
    }
}
