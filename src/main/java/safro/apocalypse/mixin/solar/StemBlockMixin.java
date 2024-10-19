package safro.apocalypse.mixin.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.apocalypse.util.SolarUtil;

@Mixin(StemBlock.class)
public class StemBlockMixin {

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void solarDestroyStems(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, CallbackInfo ci) {
        if (SolarUtil.isBlockExposed(pLevel, pPos, 1)) {
            pLevel.destroyBlock(pPos, false);
            ci.cancel();
        }
    }
}
