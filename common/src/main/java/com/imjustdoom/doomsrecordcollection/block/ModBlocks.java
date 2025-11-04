package com.imjustdoom.doomsrecordcollection.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ModBlocks {
    public static final Map<DyeColor, RecordDisplay> RECORD_DISPLAYS = new HashMap<>(16);

    static {
        Arrays.stream(DyeColor.values()).forEach(colour -> {
            RECORD_DISPLAYS.put(colour, new RecordDisplay(BlockBehaviour.Properties.of().strength(4.0f).noOcclusion()));
        });
    }
}
