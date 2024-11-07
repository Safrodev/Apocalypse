package safro.apocalypse.mixin.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.apocalypse.Apocalypse;
import safro.apocalypse.ApocalypseTags;
import safro.apocalypse.util.SolarUtil;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void solarAdditionalRandomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, CallbackInfo ci) {
        if (pState.is(ApocalypseTags.SOLAR_BURNS)) {
            if (SolarUtil.isBlockExposed(pLevel, pPos, 0)) {
                // Special case for podzol
                if (pState.is(Blocks.PODZOL)) {
                    pLevel.setBlockAndUpdate(pPos, Blocks.DIRT.defaultBlockState());
                } else {
                    pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 3);
                }
                ci.cancel();
            }
        }
    }
}
