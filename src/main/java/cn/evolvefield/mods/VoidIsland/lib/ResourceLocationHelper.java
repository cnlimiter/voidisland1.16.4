package cn.evolvefield.mods.VoidIsland.lib;

import cn.evolvefield.mods.VoidIsland.VoidIsland;
import net.minecraft.util.ResourceLocation;

public class ResourceLocationHelper {
    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(VoidIsland.MOD_ID, path);
    }
}
