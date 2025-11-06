package com.imjustdoom.doomsrecordcollection;

import com.imjustdoom.doomsrecordcollection.block.ModBlocks;
import com.imjustdoom.doomsrecordcollection.item.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoomsRecordCollection {
    public static final String MOD_ID = "doomsrecordcollection";
    public static final String MOD_NAME = "Doom's Record Collection";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static CreativeModeTab DOOMS_RECORD_COLLECTION_TAB;

    public static void init() {
        ModItems.init();
        ModBlocks.init();
    }
}