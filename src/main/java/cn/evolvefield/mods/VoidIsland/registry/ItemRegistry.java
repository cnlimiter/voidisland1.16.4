package cn.evolvefield.mods.VoidIsland.registry;

import cn.evolvefield.mods.VoidIsland.items.ItemPebble;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static cn.evolvefield.mods.VoidIsland.VoidIsland.MOD_ID;

public class ItemRegistry {
    //初始化注册器
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);


    //注册物品
    public static final RegistryObject<Item> itemPebble = ITEMS.register("item_pebble", ItemPebble::new);
}
