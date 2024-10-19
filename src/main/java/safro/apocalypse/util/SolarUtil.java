package safro.apocalypse.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.IPlantable;
import safro.apocalypse.Apocalypse;
import safro.apocalypse.api.ApocalypseType;

public class SolarUtil {

    public static boolean isBlockExposed(ServerLevel level, BlockPos pos, int stage) {
        if (Apocalypse.is(ApocalypseType.SOLAR_APOCALYPSE, level, stage)) {
            if (level.isDay() && (level.canSeeSky(pos.above()) || (level.getBlockState(pos).getBlock() instanceof IPlantable && level.canSeeSky(pos.above(2))))) {
                return level.isAreaLoaded(pos, 1);
            }
        }
        return false;
    }
}
