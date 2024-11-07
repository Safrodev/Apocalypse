package safro.apocalypse.mixin.solar;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import safro.apocalypse.Apocalypse;
import safro.apocalypse.ApocalypseTags;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    private void solarShouldTick(BlockState pState, CallbackInfoReturnable<Boolean> cir) {
        if (pState.is(ApocalypseTags.SOLAR_BURNS)) {
            cir.setReturnValue(true);
        }
    }
}
