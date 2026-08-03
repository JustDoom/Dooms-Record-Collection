package com.imjustdoom.doomsrecordcollection.block;

import com.imjustdoom.doomsrecordcollection.block.entity.RecordDisplayEntity;
import com.imjustdoom.doomsrecordcollection.item.ModItems;
import com.imjustdoom.doomsrecordcollection.item.RecordDisplayItem;
import com.imjustdoom.doomsrecordcollection.platform.RegistryWrapper;
import com.imjustdoom.doomsrecordcollection.platform.Services;
import com.imjustdoom.doomsrecordcollection.recipe.RecordDisplayRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {
    public static final RegistryWrapper<RecordDisplay> RECORD_DISPLAY = registerRecordDisplay("record_display",
            () -> new RecordDisplay(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final RegistryWrapper<BlockEntityType<RecordDisplayEntity>> RECORD_DISPLAY_ENTITY =
            Services.PLATFORM.registerBlockEntity("record_display",
                    () -> BlockEntityType.Builder.of(RecordDisplayEntity::new, RECORD_DISPLAY.get()).build(null));

    public static final RegistryWrapper<RecipeSerializer<RecordDisplayRecipe>> RECORD_DISPLAY_RECIPE =
            Services.PLATFORM.registerRecipeSerializer("record_display", RecordDisplayRecipe.Serializer::new);

    /**
     * Every craftable pairing, so the creative tab lists what a player can actually make. Any other
     * pairing still renders and works, it just has to come from a command or another mod.
     */
    public static void appendDisplays(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
        HolderLookup.RegistryLookup<Item> items = params.holders().lookupOrThrow(Registries.ITEM);
        List<Block> bases = blocksIn(items, RecordDisplayRecipe.BASES);
        blocksIn(items, RecordDisplayRecipe.WOOLS)
                .forEach(wool -> bases.forEach(base -> output.accept(RecordDisplayItem.of(base, wool))));
    }

    private static List<Block> blocksIn(HolderLookup.RegistryLookup<Item> items, TagKey<Item> tag) {
        return items.get(tag).stream().flatMap(HolderSet::stream).map(Holder::value)
                .filter(BlockItem.class::isInstance).map(item -> ((BlockItem) item).getBlock()).toList();
    }

    private static RegistryWrapper<RecordDisplay> registerRecordDisplay(String id, Supplier<RecordDisplay> block) {
        RegistryWrapper<RecordDisplay> registered = Services.PLATFORM.registerBlock(id, block);
        ModItems.register(id, () -> Services.PLATFORM.recordDisplayItem(registered.get(), new Item.Properties()));
        return registered;
    }

    public static void init() {
    }
}
