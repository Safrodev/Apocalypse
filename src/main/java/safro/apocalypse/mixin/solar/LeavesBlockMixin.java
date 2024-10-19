package safro.apocalypse.mixin.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import safro.apocalypse.util.SolarUtil;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {

    /*
    We must always update randomTick or the decay will not work on player placed leaves
    This method uses setReturnValue instead of actually modifying the return value for compat purposes
     */
    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    private void alwaysTickSolar(BlockState pState, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }


    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void solarDestroyLeaves(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, CallbackInfo ci) {
        if (SolarUtil.isBlockExposed(pLevel, pPos, 1)) {
            Block.dropResources(pState, pLevel, pPos);
            pLevel.destroyBlock(pPos, false);
            ci.cancel();
        }
    }
}
