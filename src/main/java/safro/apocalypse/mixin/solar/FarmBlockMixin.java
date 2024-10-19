package safro.apocalypse.mixin.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.apocalypse.util.SolarUtil;

@Mixin(FarmBlock.class)
public class FarmBlockMixin {

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void solarDestroyFarmland(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, CallbackInfo ci) {
        if (SolarUtil.isBlockExposed(pLevel, pPos, 0)) {
            FarmBlock.turnToDirt(null, pState, pLevel, pPos);
            ci.cancel();
        }
    }
}
