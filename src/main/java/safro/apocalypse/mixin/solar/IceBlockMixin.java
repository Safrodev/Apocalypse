package safro.apocalypse.mixin.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.apocalypse.util.SolarUtil;

@Mixin(IceBlock.class)
public abstract class IceBlockMixin {

    @Shadow protected abstract void melt(BlockState pState, Level pLevel, BlockPos pPos);

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void solarMeltIce(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, CallbackInfo ci) {
        if (SolarUtil.isBlockExposed(pLevel, pPos.above(), 0)) {
            melt(pState, pLevel, pPos);
            ci.cancel();
        }
    }
}
