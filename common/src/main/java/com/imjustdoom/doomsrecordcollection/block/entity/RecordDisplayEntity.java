package com.imjustdoom.doomsrecordcollection.block.entity;

import com.imjustdoom.doomsrecordcollection.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RecordDisplayEntity extends BlockEntity implements Container {
    public static final int MAX_SLOTS = 7;
    private static final String BASE_TAG = "Base";
    private static final String WOOL_TAG = "Wool";
    private static final Block DEFAULT_BASE = Blocks.OAK_PLANKS;
    private static final Block DEFAULT_WOOL = Blocks.WHITE_WOOL;

    private final NonNullList<ItemStack> items = NonNullList.withSize(MAX_SLOTS, ItemStack.EMPTY);
    private Block base = DEFAULT_BASE;
    private Block wool = DEFAULT_WOOL;

    public RecordDisplayEntity(BlockPos pos, BlockState blockState) {
        super(ModBlocks.RECORD_DISPLAY_ENTITY.get(), pos, blockState);
    }

    public Block getBase() {
        return this.base;
    }

    public Block getWool() {
        return this.wool;
    }

    public static void saveBlocks(CompoundTag tag, Block base, Block wool) {
        tag.putString(BASE_TAG, BuiltInRegistries.BLOCK.getKey(base).toString());
        tag.putString(WOOL_TAG, BuiltInRegistries.BLOCK.getKey(wool).toString());
    }

    public static Block readBase(CompoundTag tag) {
        return readBlock(tag, BASE_TAG, DEFAULT_BASE);
    }

    public static Block readWool(CompoundTag tag) {
        return readBlock(tag, WOOL_TAG, DEFAULT_WOOL);
    }

    private static Block readBlock(CompoundTag tag, String key, Block fallback) {
        if (tag == null || !tag.contains(key, Tag.TAG_STRING)) {
            return fallback;
        }

        ResourceLocation id = ResourceLocation.tryParse(tag.getString(key));
        // An unknown id means the block came from a mod that is no longer installed.
        Block block = id == null ? Blocks.AIR : BuiltInRegistries.BLOCK.get(id);
        return block == Blocks.AIR ? fallback : block;
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
        this.base = readBase(tag);
        this.wool = readWool(tag);
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
        saveBlocks(tag, this.base, this.wool);
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