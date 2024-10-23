package safro.apocalypse.mixin.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.apocalypse.Apocalypse;
import safro.apocalypse.api.ApocalypseType;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void solarPlayersBurnInDaylight(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (!player.level().isClientSide() && Apocalypse.is(ApocalypseType.SOLAR_APOCALYPSE, player.level(), 2)) {
            boolean flag = player.isAlive() && this.isSunBurnTick(player);
            if (flag) {
                ItemStack itemstack = player.getItemBySlot(EquipmentSlot.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        itemstack.setDamageValue(itemstack.getDamageValue() + player.getRandom().nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            player.broadcastBreakEvent(EquipmentSlot.HEAD);
                            player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    player.setSecondsOnFire(8);
                }
            }
        }
    }

    @Unique
    private boolean isSunBurnTick(Player player) {
        if (player.level().isDay() && !player.level().isClientSide) {
            float f = player.getLightLevelDependentMagicValue();
            BlockPos blockpos = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
            boolean flag = player.isInWaterRainOrBubble() || player.isInPowderSnow || player.wasInPowderSnow;
            if (f > 0.5F && player.getRandom().nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !flag && player.level().canSeeSky(blockpos)) {
                return true;
            }
        }

        return false;
    }
}
