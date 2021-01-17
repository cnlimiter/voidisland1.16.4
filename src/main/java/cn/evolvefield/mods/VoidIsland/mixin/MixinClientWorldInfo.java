package cn.evolvefield.mods.VoidIsland.mixin;

import cn.evolvefield.mods.VoidIsland.client.SkyblockWorldInfo;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ClientWorld.ClientWorldInfo.class)
public abstract class MixinClientWorldInfo implements SkyblockWorldInfo {
    private boolean isVoidIsland;

    @Override
    public boolean isVoidIsland() {
        return isVoidIsland;
    }

    @Override
    public void markVoidIsland() {
        isVoidIsland= true;
    }

    @Inject(at = @At("HEAD"), method = "getVoidFogHeight", cancellable = true)
    private void viHorizon(CallbackInfoReturnable<Double> cir) {
        if (isVoidIsland) {
            cir.setReturnValue(0.0);
        }
    }

    @Inject(at = @At("HEAD"), method = "getFogDistance", cancellable = true)
    private void viFog(CallbackInfoReturnable<Double> cir) {
        if (isVoidIsland) {
            cir.setReturnValue(1.0);
        }
    }
}
