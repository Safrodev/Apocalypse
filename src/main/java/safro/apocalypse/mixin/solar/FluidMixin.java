package safro.apocalypse.mixin.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.ForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.apocalypse.util.SolarUtil;

@Mixin(Fluid.class)
public class FluidMixin {

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void solarEvaporateWater(Level pLevel, BlockPos pPos, FluidState pState, RandomSource pRandom, CallbackInfo ci) {
        if (((Fluid) (Object) this).getFluidType() == ForgeMod.WATER_TYPE.get() || pState.is(FluidTags.WATER)) {
            if (pLevel instanceof ServerLevel serverLevel && SolarUtil.isBlockExposed(serverLevel, pPos, 2)) {
                serverLevel.removeBlock(pPos, false);
            }
        }
    }
}
