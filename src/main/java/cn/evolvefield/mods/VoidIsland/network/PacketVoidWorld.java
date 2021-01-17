package cn.evolvefield.mods.VoidIsland.network;

import cn.evolvefield.mods.VoidIsland.client.SkyblockWorldInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketVoidWorld {
    public static void encode(PacketVoidWorld pkt, PacketBuffer buf) {}

    public static PacketVoidWorld decode(PacketBuffer buf) {
        return new PacketVoidWorld();
    }

    public static void handle(PacketVoidWorld pkt, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                ClientWorld.ClientWorldInfo info = Minecraft.getInstance().world.getWorldInfo();
                if (info instanceof SkyblockWorldInfo) {
                    ((SkyblockWorldInfo) info).markVoidIsland();
                }
            });
        }
    }
}
