package safro.apocalypse;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import safro.apocalypse.network.NetworkHelper;

@Mod(Apocalypse.MODID)
public class Apocalypse {
    public static final String MODID = "apocalypse";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Apocalypse() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(CommonEvents.class);

        MinecraftForge.EVENT_BUS.addListener(CommonEvents::onServerStarted);
        MinecraftForge.EVENT_BUS.addListener(CommonEvents::serverTick);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ApocalypseConfig.SPEC);

        NetworkHelper.register();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }
}
