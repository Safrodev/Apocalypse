package safro.apocalypse.mixin.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import safro.apocalypse.util.SolarUtil;

@Mixin(value = FluidType.class, remap = false)
public class FluidTypeMixin {

    @Inject(method = "isVaporizedOnPlacement", at = @At("HEAD"), cancellable = true)
    private void solarIsVaporized(Level level, BlockPos pos, FluidStack stack, CallbackInfoReturnable<Boolean> cir) {
        FluidType type = (FluidType) (Object) this;
        if (level instanceof ServerLevel serverLevel) {
            if (type == ForgeMod.WATER_TYPE.get() || type.getStateForPlacement(level, pos, stack).is(FluidTags.WATER)) {
                if (SolarUtil.isBlockExposed(serverLevel, pos.below(), 2)) {
                    level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
