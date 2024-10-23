package safro.apocalypse.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.AdditionalHead;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import safro.apocalypse.ApocalypseConfig;

public class WitherStormEvent {

    public static void spawn(ServerLevel world) {
        if (world.getDifficulty() != Difficulty.PEACEFUL) {
            WitherStormEntity witherStorm = WitherStormModEntityTypes.WITHER_STORM.get().create(world);
            if (witherStorm != null) {
                BlockPos blockPos = new BlockPos(ApocalypseConfig.witherStormCoords.get(0), ApocalypseConfig.witherStormCoords.get(1), ApocalypseConfig.witherStormCoords.get(2));
                Vec3 vector = new Vec3((double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.55, (double) blockPos.getZ() + 0.5);
                witherStorm.moveTo(vector.x, vector.y, vector.z, 0.0F, 0.0F);
                witherStorm.yBodyRot = 0.0F;

                for (AdditionalHead head : witherStorm.getHeadManager().getOtherHeads()) {
                    head.setHeadYRot(0.0F);
                }

                witherStorm.makeInvulnerable();
                witherStorm.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_ACTIVATES.get(), 4.0F, 1.0F);
                world.addFreshEntity(witherStorm);

                if (world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    destroyArea(witherStorm);
                }
            }
        }
    }

    private static void destroyArea(WitherStormEntity witherStorm) {
        int j1 = Mth.floor(witherStorm.getY());
        int i2 = Mth.floor(witherStorm.getX());
        int j2 = Mth.floor(witherStorm.getZ());
        boolean flag = false;

        for(int j = -1; j <= 1; ++j) {
            for(int k2 = -1; k2 <= 1; ++k2) {
                for(int k = 0; k <= 3; ++k) {
                    int l2 = i2 + j;
                    int l = j1 + k;
                    int i1 = j2 + k2;
                    BlockPos blockpos = new BlockPos(l2, l, i1);
                    BlockState blockstate = witherStorm.level().getBlockState(blockpos);
                    if ((blockstate.isAir() || WitherBoss.canDestroy(blockstate)) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(witherStorm, blockpos, blockstate)) {
                        flag = witherStorm.level().destroyBlock(blockpos, true, witherStorm) || flag;
                    }
                }
            }
        }

        if (flag) {
            witherStorm.level().levelEvent(null, 1022, witherStorm.blockPosition(), 0);
        }
    }
}
