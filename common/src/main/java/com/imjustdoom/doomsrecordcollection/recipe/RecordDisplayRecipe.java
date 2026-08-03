package com.imjustdoom.doomsrecordcollection.recipe;

import com.google.gson.JsonObject;
import com.imjustdoom.doomsrecordcollection.DoomsRecordCollection;
import com.imjustdoom.doomsrecordcollection.block.ModBlocks;
import com.imjustdoom.doomsrecordcollection.item.RecordDisplayItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * An ordinary shaped recipe (so it still shows up in the recipe book) whose result picks up the wool
 * and plank blocks that were actually used, instead of being one fixed item per combination.
 */
public class RecordDisplayRecipe extends ShapedRecipe {
    /**
     * Which blocks a display can be built out of. Any block renders fine, so these only gate crafting
     * and the creative tab, and a data pack can widen them without touching the mod.
     */
    public static final TagKey<Item> BASES = TagKey.create(Registries.ITEM, new ResourceLocation(DoomsRecordCollection.MOD_ID, "record_display_bases"));
    public static final TagKey<Item> WOOLS = TagKey.create(Registries.ITEM, new ResourceLocation(DoomsRecordCollection.MOD_ID, "record_display_wools"));

    public RecordDisplayRecipe(ShapedRecipe shaped) {
        super(shaped.getId(), shaped.getGroup(), shaped.category(), shaped.getWidth(), shaped.getHeight(),
                shaped.getIngredients(), shaped.getResultItem(RegistryAccess.EMPTY), shaped.showNotification());
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        Block base = Blocks.OAK_PLANKS;
        Block wool = Blocks.WHITE_WOOL;
        for (ItemStack stack : container.getItems()) {
            if (!(stack.getItem() instanceof BlockItem blockItem)) {
                continue;
            }
            if (stack.is(WOOLS)) {
                wool = blockItem.getBlock();
            } else if (stack.is(BASES)) {
                base = blockItem.getBlock();
            }
        }

        return RecordDisplayItem.of(base, wool);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModBlocks.RECORD_DISPLAY_RECIPE.get();
    }

    public static class Serializer implements RecipeSerializer<RecordDisplayRecipe> {
        private static final RecipeSerializer<ShapedRecipe> SHAPED = RecipeSerializer.SHAPED_RECIPE;

        @Override
        public RecordDisplayRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new RecordDisplayRecipe(SHAPED.fromJson(id, json));
        }

        @Override
        public RecordDisplayRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new RecordDisplayRecipe(SHAPED.fromNetwork(id, buffer));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RecordDisplayRecipe recipe) {
            SHAPED.toNetwork(buffer, recipe);
        }
    }
}
