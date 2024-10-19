package safro.apocalypse.mixin.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import safro.apocalypse.util.SolarUtil;

@Mixin(BambooStalkBlock.class)
public class BambooStalkBlockMixin {

    /*
    We must always update randomTick or the decay will not work on fully grown bamboo
    This method uses setReturnValue instead of actually modifying the return value for compat purposes
     */
    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    private void alwaysTickSolar(BlockState pState, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void solarDestroyBamboo(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, CallbackInfo ci) {
        if (SolarUtil.isBlockExposed(pLevel, pPos, 1)) {
            pLevel.destroyBlock(pPos, false);
            ci.cancel();
        }
    }
}
