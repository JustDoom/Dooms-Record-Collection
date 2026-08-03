package com.imjustdoom.doomsrecordcollection.block.renderer;

import com.imjustdoom.doomsrecordcollection.block.ModBlocks;
import com.imjustdoom.doomsrecordcollection.block.RecordDisplay;
import com.imjustdoom.doomsrecordcollection.item.RecordDisplayItem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Draws the single record display model with the base and wool texture slots swapped out for whatever
 * two blocks a given display is made of. The block model ships with placeholder textures, and every
 * quad it bakes belongs to one slot or the other, so retexturing is a UV remap onto the target sprite.
 */
public final class RecordDisplayModel {
    /** The {@code "1"} texture slot in {@code models/block/record_display.json}, i.e. the wool part. */
    private static final ResourceLocation WOOL_PLACEHOLDER = new ResourceLocation("block/white_wool");
    private static final int STRIDE = DefaultVertexFormat.BLOCK.getVertexSize() / Integer.BYTES;
    private static final int UV_OFFSET = 4;
    private static final Direction[] SIDES = {
            null, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
    };

    // ponytail: keyed on the baked model, so a resource reload orphans entries rather than freeing them.
    // Each entry is a few hundred quads; add a reload listener if that ever adds up.
    private static final Map<Key, List<BakedQuad>> CACHE = new HashMap<>();

    private RecordDisplayModel() {
    }

    public static void render(BlockState state, Block base, Block wool, PoseStack poseStack,
                              MultiBufferSource buffers, int packedLight, int packedOverlay) {
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
        VertexConsumer consumer = buffers.getBuffer(Sheets.solidBlockSheet());
        PoseStack.Pose pose = poseStack.last();
        for (BakedQuad quad : quads(model, state, base, wool)) {
            consumer.putBulkData(pose, quad, 1f, 1f, 1f, packedLight, packedOverlay);
        }
    }

    /** Item rendering has no placed block to read a facing off, so use the unrotated (south) variant. */
    public static void renderItem(ItemStack stack, PoseStack poseStack, MultiBufferSource buffers,
                                  int packedLight, int packedOverlay) {
        BlockState state = ModBlocks.RECORD_DISPLAY.get().defaultBlockState()
                .setValue(RecordDisplay.FACING, Direction.SOUTH);
        render(state, RecordDisplayItem.base(stack), RecordDisplayItem.wool(stack),
                poseStack, buffers, packedLight, packedOverlay);
    }

    private static List<BakedQuad> quads(BakedModel model, BlockState state, Block base, Block wool) {
        return CACHE.computeIfAbsent(new Key(model, base, wool), key -> {
            TextureAtlasSprite baseSprite = sprite(base);
            TextureAtlasSprite woolSprite = sprite(wool);
            RandomSource random = RandomSource.create();
            List<BakedQuad> retextured = new ArrayList<>();
            for (Direction side : SIDES) {
                for (BakedQuad quad : model.getQuads(state, side, random)) {
                    boolean isWool = quad.getSprite().contents().name().equals(WOOL_PLACEHOLDER);
                    retextured.add(retexture(quad, isWool ? woolSprite : baseSprite));
                }
            }
            return retextured;
        });
    }

    /**
     * Any block works as a texture source: its particle icon is the sprite the game itself picks to
     * represent the block, which is the full-cube texture for planks, wool, concrete and the like.
     */
    private static TextureAtlasSprite sprite(Block block) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(block.defaultBlockState()).getParticleIcon();
    }

    private static BakedQuad retexture(BakedQuad quad, TextureAtlasSprite target) {
        TextureAtlasSprite source = quad.getSprite();
        int[] vertices = quad.getVertices().clone();
        for (int vertex = 0; vertex < 4; vertex++) {
            int uv = vertex * STRIDE + UV_OFFSET;
            vertices[uv] = remap(vertices[uv], source.getU0(), source.getU1(), target.getU0(), target.getU1());
            vertices[uv + 1] = remap(vertices[uv + 1], source.getV0(), source.getV1(), target.getV0(), target.getV1());
        }
        return new BakedQuad(vertices, quad.getTintIndex(), quad.getDirection(), target, quad.isShade());
    }

    private static int remap(int bits, float sourceMin, float sourceMax, float targetMin, float targetMax) {
        float fraction = (Float.intBitsToFloat(bits) - sourceMin) / (sourceMax - sourceMin);
        return Float.floatToRawIntBits(targetMin + fraction * (targetMax - targetMin));
    }

    private record Key(BakedModel model, Block base, Block wool) {
    }
}
