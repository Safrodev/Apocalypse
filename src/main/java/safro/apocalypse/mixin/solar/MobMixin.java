package safro.apocalypse.mixin.solar;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.apocalypse.Apocalypse;
import safro.apocalypse.api.ApocalypseType;

@Mixin(Mob.class)
public abstract class MobMixin {

    @Shadow protected abstract boolean isSunBurnTick();

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void solarMobsBurnInDaylight(CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (!mob.level().isClientSide() && Apocalypse.is(ApocalypseType.SOLAR_APOCALYPSE, mob.level(), 2)) {
            boolean flag = mob.isAlive() && this.isSunBurnTick();
            if (flag) {
                ItemStack itemstack = mob.getItemBySlot(EquipmentSlot.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        itemstack.setDamageValue(itemstack.getDamageValue() + mob.getRandom().nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            mob.broadcastBreakEvent(EquipmentSlot.HEAD);
                            mob.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    mob.setSecondsOnFire(8);
                }
            }
        }
    }
}
