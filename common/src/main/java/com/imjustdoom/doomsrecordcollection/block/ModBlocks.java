package com.imjustdoom.doomsrecordcollection.block;

import com.imjustdoom.doomsrecordcollection.block.entity.RecordDisplayEntity;
import com.imjustdoom.doomsrecordcollection.item.ModItems;
import com.imjustdoom.doomsrecordcollection.platform.RegistryWrapper;
import com.imjustdoom.doomsrecordcollection.platform.Services;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.*;
import java.util.function.Supplier;

public class ModBlocks {
    public static final List<RegistryWrapper<RecordDisplay>> RECORD_DISPLAYS = new ArrayList<>(16);
    public static final RegistryWrapper<RecordDisplay> BAMBOO_RECORD_DISPLAY = registerBlock("bamboo_record_display",
            () -> new RecordDisplay(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final RegistryWrapper<BlockEntityType<RecordDisplayEntity>> RECORD_DISPLAY_ENTITY;

    private static <T extends Block> RegistryWrapper<T> registerBlock(String id, Supplier<T> block) {
        RegistryWrapper<T> registeredBlock = Services.PLATFORM.registerBlock(id, block);
        registerBlockItem(id, registeredBlock);
        return registeredBlock;
    }

    private static <T extends Block> RegistryWrapper<Item> registerBlockItem(String name, RegistryWrapper<T> block) {
        return ModItems.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends BlockEntity> RegistryWrapper<BlockEntityType<T>> registerBlockEntity(String id, Supplier<BlockEntityType<T>> blockEntity) {
        return Services.PLATFORM.registerBlockEntity(id, blockEntity);
    }

    public static void init() {
    }

    static {
        Arrays.stream(DyeColor.values()).forEach(colour -> {
            RECORD_DISPLAYS.add(registerBlock(colour.getName() + "_record_display",
                    () -> new RecordDisplay(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion())
            ));
        });
        RECORD_DISPLAYS.add(BAMBOO_RECORD_DISPLAY);

        RECORD_DISPLAY_ENTITY = registerBlockEntity("record_display", () -> BlockEntityType.Builder.of(
                RecordDisplayEntity::new,
                RECORD_DISPLAYS.stream().map(RegistryWrapper::get).toArray(Block[]::new)
        ).build(null));
    }
}
