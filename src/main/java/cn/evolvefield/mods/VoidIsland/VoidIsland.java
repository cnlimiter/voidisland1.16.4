package cn.evolvefield.mods.VoidIsland;

import cn.evolvefield.mods.VoidIsland.commands.SkyblockCommand;
import cn.evolvefield.mods.VoidIsland.config.ConfigHandler;
import cn.evolvefield.mods.VoidIsland.network.PacketHandler;
import cn.evolvefield.mods.VoidIsland.world.ModFeatures;
import cn.evolvefield.mods.VoidIsland.world.SkyblockChunkGenerator;
import cn.evolvefield.mods.VoidIsland.world.SkyblockWorldEvents;


import net.minecraft.block.Block;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



// The value here should match an entry in the META-INF/mods.toml file
@Mod("voidisland")
public class VoidIsland
{

    public static String MOD_ID = "voidisland";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger(VoidIsland.MOD_ID);
    public static volatile boolean configLoaded = false;


    public VoidIsland() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        //
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);
        modBus.addGenericListener(ForgeWorldType.class, ModFeatures::registerWorldType);
        modBus.addListener((ModConfig.Loading e) -> ConfigHandler.onConfigLoad());
        modBus.addListener((ModConfig.Reloading e) -> ConfigHandler.onConfigLoad());

        //
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(this::registerCommands);



        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);


    }

    private void commonSetup(FMLCommonSetupEvent event)
    {

        PacketHandler.init();



            MinecraftForge.EVENT_BUS.addListener(SkyblockWorldEvents::syncGogStatus);
            MinecraftForge.EVENT_BUS.addListener(SkyblockWorldEvents::onPlayerJoin);
            MinecraftForge.EVENT_BUS.addListener(SkyblockWorldEvents::onPlayerInteract);


        event.enqueueWork(() -> {
            SkyblockChunkGenerator.init();
//            GlobalEntityTypeAttributes.put(ModEntities.DOPPLEGANGER, MobEntity.func_233666_p_()
//                    .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.4)
//                    .createMutableAttribute(Attributes.MAX_HEALTH, EntityDoppleganger.MAX_HP)
//                    .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0)
//                    .create());
//            GlobalEntityTypeAttributes.put(ModEntities.PIXIE, MobEntity.func_233666_p_()
//                    .createMutableAttribute(Attributes.MAX_HEALTH, 2.0)
//                    .create());
//            GlobalEntityTypeAttributes.put(ModEntities.PINK_WITHER, WitherEntity.registerAttributes().create());
//            ModBanners.init();

//            PatchouliAPI.instance.registerMultiblock(Registry.BLOCK.getKey(ModBlocks.alfPortal), TileAlfPortal.MULTIBLOCK.getValue());
//            PatchouliAPI.instance.registerMultiblock(Registry.BLOCK.getKey(ModBlocks.terraPlate), TileTerraPlate.MULTIBLOCK.getValue());
//            PatchouliAPI.instance.registerMultiblock(Registry.BLOCK.getKey(ModBlocks.enchanter), TileEnchanter.MULTIBLOCK.getValue());



        });
    }
    private void registerCommands(RegisterCommandsEvent event) {

            SkyblockCommand.register(event.getDispatcher());

    }



    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
