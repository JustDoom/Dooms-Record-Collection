package com.imjustdoom.doomsrecordcollection.block.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/**
 * Forge's only hook for a dynamic item renderer is the item class itself, so
 * {@link com.imjustdoom.doomsrecordcollection.platform.ForgePlatformHelper} hands this to every
 * record display item. Fabric registers {@link RecordDisplayModel#renderItem} directly instead.
 */
public final class RecordDisplayItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static final IClientItemExtensions EXTENSIONS = new IClientItemExtensions() {
        private RecordDisplayItemRenderer renderer;

        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            if (this.renderer == null) {
                this.renderer = new RecordDisplayItemRenderer();
            }
            return this.renderer;
        }
    };

    private RecordDisplayItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack,
                             MultiBufferSource buffers, int packedLight, int packedOverlay) {
        RecordDisplayModel.renderItem(stack, poseStack, buffers, packedLight, packedOverlay);
    }
}
