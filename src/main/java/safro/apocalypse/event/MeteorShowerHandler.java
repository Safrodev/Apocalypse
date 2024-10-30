package safro.apocalypse.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import safro.apocalypse.Apocalypse;
import safro.apocalypse.ApocalypseConfig;
import safro.apocalypse.entity.ApocalypseEntities;
import safro.apocalypse.entity.MeteorEntity;

public class MeteorShowerHandler {

    public static void tick(ServerLevel world) {
        for (ServerPlayer player : world.players()) {
            if (player.getRandom().nextDouble() <= ApocalypseConfig.meteorChance) {
                int y = world.getMaxBuildHeight() - 1;
                int x = player.blockPosition().getX() + Mth.nextInt(player.getRandom(), -40, 40);
                int z = player.blockPosition().getZ() + Mth.nextInt(player.getRandom(), -40, 40);

                MeteorEntity meteor = new MeteorEntity(world, x, y, z);
                world.addFreshEntity(meteor);
            }
        }
    }
}
