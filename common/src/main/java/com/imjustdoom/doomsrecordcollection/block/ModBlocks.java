package com.imjustdoom.doomsrecordcollection.block;

import com.imjustdoom.doomsrecordcollection.block.entity.RecordDisplayEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ModBlocks {
    public static final Map<DyeColor, RecordDisplay> RECORD_DISPLAYS = new HashMap<>(16);

    public static final BlockEntityType<RecordDisplayEntity> RECORD_DISPLAY_ENTITY;

    static {
        Arrays.stream(DyeColor.values()).forEach(colour -> {
            RECORD_DISPLAYS.put(colour, new RecordDisplay(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));
        });

        RECORD_DISPLAY_ENTITY = BlockEntityType.Builder.of(
                RecordDisplayEntity::new,
                RECORD_DISPLAYS.values().toArray(new Block[0])
        ).build(null);
    }
}
