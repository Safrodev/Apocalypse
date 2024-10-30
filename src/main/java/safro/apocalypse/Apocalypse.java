package safro.apocalypse;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import safro.apocalypse.api.ApocalypseData;
import safro.apocalypse.api.ApocalypseType;
import safro.apocalypse.entity.ApocalypseEntities;
import safro.apocalypse.event.CommonEvents;
import safro.apocalypse.network.NetworkHelper;

@Mod(Apocalypse.MODID)
public class Apocalypse {
    public static final String MODID = "apocalypse";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Apocalypse() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ApocalypseConfig.SPEC);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        ApocalypseEntities.REGISTRY.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(CommonEvents.class);

        NetworkHelper.register();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    /**
     * Checks if an Apocalypse of the provided type has begun, if it exists
     * @param type type to check for
     * @param stage check if the apocalypse has reached a certain stage. Use -1 for always occurring events.
     * @return true if the Apocalypse has started and is of the given type
     */
    public static boolean is(ApocalypseType type, Level level, int stage) {
        ApocalypseData data = ApocalypseData.get(level);
        if (data != null && data.hasStarted(type)) {
            if (stage < 0) {
                return true;
            } else {
                return checkStage(type, stage, data.getDaysSinceStart());
            }
        }
        return false;
    }

    private static boolean checkStage(ApocalypseType type, int stage, int days) {
        int[] map = type.getStages();
        if (map.length == 0) {
            return true;
        }
        return days >= map[stage];
    }
}
