package cn.evolvefield.mods.VoidIsland.world;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraftforge.common.world.ForgeWorldType;

import javax.annotation.Nonnull;

public class WorldTypeSkyblock extends ForgeWorldType {
    public static final WorldTypeSkyblock INSTANCE = new WorldTypeSkyblock();

    private WorldTypeSkyblock() {
        super(WorldTypeSkyblock::getChunkGenerator);
    }

    private static ChunkGenerator getChunkGenerator(@Nonnull Registry<Biome> biomeRegistry, @Nonnull Registry<DimensionSettings> dimensionSettingsRegistry, long seed) {
        return new SkyblockChunkGenerator(new OverworldBiomeProvider(seed, false, false, biomeRegistry), seed,
                () -> dimensionSettingsRegistry.getOrThrow(DimensionSettings.field_242734_c));
    }

    @Override
    public String getTranslationKey() {
        return "generator.skyblock";
    }
}
