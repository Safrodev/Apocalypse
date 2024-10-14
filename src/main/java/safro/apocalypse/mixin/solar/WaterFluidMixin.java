package safro.apocalypse.mixin.solar;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import safro.apocalypse.Apocalypse;
import safro.apocalypse.api.ApocalypseType;

@Mixin(WaterFluid.class)
public class WaterFluidMixin {

    @Inject(method = "canConvertToSource", at = @At("HEAD"), cancellable = true)
    private void disableInfiniteWater(Level pLevel, CallbackInfoReturnable<Boolean> cir) {
        if (Apocalypse.is(ApocalypseType.SOLAR_APOCALYPSE, pLevel, -1)) {
            cir.setReturnValue(false);
        }
    }
}
