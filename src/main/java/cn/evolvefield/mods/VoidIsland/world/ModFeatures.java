package cn.evolvefield.mods.VoidIsland.world;

import cn.evolvefield.mods.VoidIsland.VoidIsland;
import cn.evolvefield.mods.VoidIsland.blocks.ModBlocks;
import cn.evolvefield.mods.VoidIsland.config.ConfigHandler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;


import static cn.evolvefield.mods.VoidIsland.lib.ResourceLocationHelper.prefix;

public class ModFeatures {
    public static void registerWorldType(RegistryEvent.Register<ForgeWorldType> event) {

            ModBlocks.register(event.getRegistry(), prefix("voidisland"), WorldTypeSkyblock.INSTANCE);

    }


}
