package safro.apocalypse;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import safro.apocalypse.api.ApocalypseData;
import safro.apocalypse.command.StartApocalypseCommand;

@Mod.EventBusSubscriber(modid = Apocalypse.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ServerLevel level = event.getServer().getLevel(Level.OVERWORLD);
        if (level != null) {
            ApocalypseData.INSTANCE = level.getDataStorage().computeIfAbsent(ApocalypseData::load, ApocalypseData::new, ApocalypseData.KEY);
        }
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (ApocalypseData.INSTANCE != null) {
                ApocalypseData.INSTANCE.tick();
            }
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        StartApocalypseCommand.register(event.getDispatcher());
    }
}
