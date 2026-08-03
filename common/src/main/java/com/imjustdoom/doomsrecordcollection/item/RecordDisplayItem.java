package com.imjustdoom.doomsrecordcollection.item;

import com.imjustdoom.doomsrecordcollection.block.ModBlocks;
import com.imjustdoom.doomsrecordcollection.block.entity.RecordDisplayEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * The two blocks a display is built out of live in the block entity, which the item carries around as
 * a {@code BlockEntityTag}. Vanilla copies that tag into the block entity on placement for us.
 */
public class RecordDisplayItem extends BlockItem {
    public RecordDisplayItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static ItemStack of(Block base, Block wool) {
        ItemStack stack = new ItemStack(ModBlocks.RECORD_DISPLAY.get());
        CompoundTag tag = new CompoundTag();
        RecordDisplayEntity.saveBlocks(tag, base, wool);
        BlockItem.setBlockEntityData(stack, ModBlocks.RECORD_DISPLAY_ENTITY.get(), tag);
        return stack;
    }

    public static Block base(ItemStack stack) {
        return RecordDisplayEntity.readBase(BlockItem.getBlockEntityData(stack));
    }

    public static Block wool(ItemStack stack) {
        return RecordDisplayEntity.readWool(BlockItem.getBlockEntityData(stack));
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(getDescriptionId() + ".combined", wool(stack).getName(), base(stack).getName());
    }
}
