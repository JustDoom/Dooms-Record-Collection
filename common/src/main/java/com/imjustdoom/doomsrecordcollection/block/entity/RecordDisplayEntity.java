package com.imjustdoom.doomsrecordcollection.block.entity;

import com.imjustdoom.doomsrecordcollection.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RecordDisplayEntity extends BlockEntity implements Container {
    public static final int MAX_SLOTS = 7;
    private final NonNullList<ItemStack> items = NonNullList.withSize(MAX_SLOTS, ItemStack.EMPTY);

    public RecordDisplayEntity(BlockPos pos, BlockState blockState) {
        super(ModBlocks.RECORD_DISPLAY_ENTITY.get(), pos, blockState);
    }

    @Override
    public int getContainerSize() {
        return MAX_SLOTS;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        if (index < 0 || index >= this.items.size()) {
            return ItemStack.EMPTY;
        }
        return this.items.get(index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index < 0 || index >= MAX_SLOTS) {
            return;
        }

        this.items.set(index, stack.copy());
        if (!stack.isEmpty()) {
            stack.setCount(1);
        }

        setChanged();
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (index < 0 || index >= this.items.size()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = this.items.get(index);
        setItem(index, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public boolean stillValid(Player player) {
        if (getLevel() == null || getLevel().getBlockEntity(getBlockPos()) != this) {
            return false;
        }

        return player.distanceToSqr((double) getBlockPos().getX() + 0.5f, (double) getBlockPos().getY() + 0.5f,
                (double) getBlockPos().getZ() + 0.5f) <= 64.0;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (index < 0 || index >= this.items.size()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = this.items.get(index);
        this.items.set(index, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (!tag.contains("Items", Tag.TAG_LIST)) {
            return;
        }

        ListTag list = tag.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); ++i) {
            CompoundTag itemTag = list.getCompound(i);
            int slot = itemTag.getByte("Slot") & 255;
            if (slot < this.items.size()) {
                this.items.set(slot, ItemStack.of(itemTag));
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ListTag list = new ListTag();
        for (int i = 0; i < this.items.size(); ++i) {
            CompoundTag itemTag = new CompoundTag();
            itemTag.putByte("Slot", (byte) i);
            this.items.get(i).save(itemTag);
            list.add(itemTag);
        }
        tag.put("Items", list);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = new CompoundTag();
        this.saveAdditional(compoundtag);
        return compoundtag;
    }

    @Override
    public void setChanged() {
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
        super.setChanged();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void clearContent() {
        this.items.clear();
        setChanged();
    }
}