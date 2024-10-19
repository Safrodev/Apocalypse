package safro.apocalypse.event;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import safro.apocalypse.Apocalypse;
import safro.apocalypse.api.ApocalypseData;
import safro.apocalypse.command.StartApocalypseCommand;

@Mod.EventBusSubscriber(modid = Apocalypse.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ApocalypseData.get(event.getServer()).tick();
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        StartApocalypseCommand.register(event.getDispatcher());
    }
}
