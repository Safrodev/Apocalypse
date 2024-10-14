package safro.apocalypse.mixin.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.apocalypse.Apocalypse;
import safro.apocalypse.api.ApocalypseType;

@Mixin(SpreadingSnowyDirtBlock.class)
public class SpreadingSnowDirtBlockMixin {

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void preventGrass(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, CallbackInfo ci) {
        if (Apocalypse.is(ApocalypseType.SOLAR_APOCALYPSE, pLevel, 0)) {
            if (pLevel.isDay() && pLevel.canSeeSky(pPos)) {
                if (pLevel.isAreaLoaded(pPos, 1)) {
                    pLevel.setBlockAndUpdate(pPos, Blocks.DIRT.defaultBlockState());
                }
            }
        }
    }
}
