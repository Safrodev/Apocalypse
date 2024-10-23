package safro.apocalypse.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import safro.apocalypse.Apocalypse;
import safro.apocalypse.api.ApocalypseData;
import safro.apocalypse.api.ApocalypseType;
import safro.apocalypse.command.StartApocalypseCommand;

@Mod.EventBusSubscriber(modid = Apocalypse.MODID)
public class CommonEvents {

    public static void startApocalypse(ApocalypseType type, ServerLevel level) {
        if (type == ApocalypseType.WITHER_STORM && ModList.get().isLoaded("witherstormmod")) {
            WitherStormEvent.spawn(level);
        }
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ApocalypseData.get(event.getServer()).tick(event.getServer().overworld());
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        StartApocalypseCommand.register(event.getDispatcher());
    }
}
