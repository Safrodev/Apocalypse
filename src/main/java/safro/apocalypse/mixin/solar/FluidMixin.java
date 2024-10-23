package safro.apocalypse.mixin.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraftforge.common.ForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import safro.apocalypse.Apocalypse;
import safro.apocalypse.api.ApocalypseType;

@Mixin(Fluid.class)
public class FluidMixin {

    /*
    We must always update randomTick or water evaporation will not occur
    This method uses setReturnValue instead of actually modifying the return value for compat purposes
     */
    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    private void solarTickWater(CallbackInfoReturnable<Boolean> cir) {
        if (((Fluid) (Object) this) instanceof WaterFluid) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void solarEvaporateWater(Level pLevel, BlockPos pPos, FluidState pState, RandomSource pRandom, CallbackInfo ci) {
        if ((((Fluid) (Object) this).getFluidType() == ForgeMod.WATER_TYPE.get() || pState.is(FluidTags.WATER)) && pState.isSource()) {
            if (pLevel instanceof ServerLevel serverLevel && Apocalypse.is(ApocalypseType.SOLAR_APOCALYPSE, serverLevel, 0)) {
                if (serverLevel.isLoaded(pPos) && serverLevel.isDay() && serverLevel.canSeeSky(pPos.above())) {
                    serverLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }
}
