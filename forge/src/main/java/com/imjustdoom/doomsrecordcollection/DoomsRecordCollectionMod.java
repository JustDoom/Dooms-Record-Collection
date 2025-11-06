package com.imjustdoom.doomsrecordcollection;

import net.minecraftforge.fml.common.Mod;

@Mod(DoomsRecordCollection.MOD_ID)
public class DoomsRecordCollectionMod {
    
    public DoomsRecordCollectionMod() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        DoomsRecordCollection.LOG.info("Hello Forge world!");
        DoomsRecordCollection.init();
    }
}