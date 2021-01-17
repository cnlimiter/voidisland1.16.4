package cn.evolvefield.mods.VoidIsland.config;

import cn.evolvefield.mods.VoidIsland.VoidIsland;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;



public class ConfigHandler {

    public static class Common {


        public final ForgeConfigSpec.IntValue gogIslandScaleMultiplier;
        public Common(ForgeConfigSpec.Builder builder) {


            gogIslandScaleMultiplier = builder
                    .comment("多人游戏中每个到岛之间的距离的倍率.\n" +
                            "在每个岛之间的默认距离是256格, 出生的岛通常会被放置在 256, 256.\n" +
                            "如果倍率设置为 8, 每个岛之间有 2048 的方块距离.\n" +
                            "不推荐使用小于 4 (1024 的方块距离)的倍率 ")
                    .defineInRange("voidIsland.islandScaleMultiplier", 8, 1, 512);



        }
    }
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }



    public static void onConfigLoad() {
        VoidIsland.configLoaded = true;
    }
}
