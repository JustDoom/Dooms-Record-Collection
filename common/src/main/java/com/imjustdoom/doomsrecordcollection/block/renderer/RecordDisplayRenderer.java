package com.imjustdoom.doomsrecordcollection.block.renderer;

import com.imjustdoom.doomsrecordcollection.block.RecordDisplay;
import com.imjustdoom.doomsrecordcollection.block.entity.RecordDisplayEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class RecordDisplayRenderer implements BlockEntityRenderer<RecordDisplayEntity> {
    private final BlockEntityRendererProvider.Context context;
    private final Map<Integer, Float> itemHover = new HashMap<>(7);

    public RecordDisplayRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;

        for (int slot = 0; slot < 7; slot++) {
            this.itemHover.put(slot, 0.3125f);
        }
    }

    @Override
    public void render(RecordDisplayEntity recordDisplayEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        Direction facing = recordDisplayEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        boolean isX = facing.getAxis() == Direction.Axis.X;
        int hoverSlot = -1;
        float deltaTime = Minecraft.getInstance().getDeltaFrameTime() / 20.0f;

        // Get hovered slot
        HitResult rayTraceResult = Minecraft.getInstance().hitResult;
        if (rayTraceResult instanceof BlockHitResult blockHitResult
                && recordDisplayEntity.getLevel().getBlockState(blockHitResult.getBlockPos()).getBlock() instanceof RecordDisplay
                && rayTraceResult.getType() == BlockHitResult.Type.BLOCK) {

            Vec3 vec = blockHitResult.getLocation();
            double rawInc = (facing == Direction.NORTH || facing == Direction.SOUTH) ? vec.x % 1 : vec.z % 1;
            hoverSlot = RecordDisplay.getSlot(rawInc >= 0 ? rawInc : rawInc + 1);
        }

        // Render discs
        for (int slot = 0; slot < 7; slot++) {
            ItemStack item = recordDisplayEntity.getItem(slot);
            if (item.isEmpty()) {
                continue;
            }

            float y = this.itemHover.get(slot);
            if (slot == hoverSlot) {
                this.itemHover.put(slot, Math.min(0.45f, y + 0.75f * deltaTime));
            } else {
                this.itemHover.put(slot, Math.max(0.3125f, y - 0.75f * deltaTime));
            }
            y = this.itemHover.get(slot);

            poseStack.pushPose();
            poseStack.translate(isX ? 0.5f : 0.125f * (slot + 1), y, isX ? 0.125f * (slot + 1) : 0.5f);
            if (!isX) {
                poseStack.rotateAround(Axis.YP.rotationDegrees(90), 0, 0, 0);
            }
            Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED, packedLight,
                    packedOverlay, poseStack, multiBufferSource, recordDisplayEntity.getLevel(), 0);
            poseStack.popPose();
        }

        // Render hovered name
        if (hoverSlot != -1) {
            ItemStack stackInSlot = recordDisplayEntity.getItem(hoverSlot);
            if (!stackInSlot.isEmpty()) {
                renderNameTag(recordDisplayEntity, ((RecordItem) stackInSlot.getItem()).getDisplayName(), poseStack, multiBufferSource, packedLight);
            }
        }
    }

    protected void renderNameTag(BlockEntity entity, Component displayName, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        double distance = this.context.getBlockEntityRenderDispatcher().camera.getPosition().distanceToSqr(entity.getBlockPos().getCenter());
        if (distance > 4096) {
            return;
        }
        float yOffset = 1;
        poseStack.pushPose();
        poseStack.translate(0.5f, yOffset, 0.5f);
        poseStack.mulPose(this.context.getEntityRenderer().cameraOrientation());
        poseStack.scale(-0.025f, -0.025f, 0.025f);
        Font font = this.context.getFont();
        font.drawInBatch(displayName, -font.width(displayName) / 2f, 0, -1, false,
                poseStack.last().pose(), buffer, Font.DisplayMode.NORMAL,
                (int) (Minecraft.getInstance().options.getBackgroundOpacity(0.25f) * 255.0f) << 24, packedLight);
        poseStack.popPose();
    }
}
