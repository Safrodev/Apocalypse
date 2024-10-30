package safro.apocalypse.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import safro.apocalypse.Apocalypse;
import safro.apocalypse.client.render.MeteorEntityRenderer;
import safro.apocalypse.entity.ApocalypseEntities;

@Mod.EventBusSubscriber(modid = Apocalypse.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("countdown_hud", new CountdownOverlay());
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ApocalypseEntities.METEOR.get(), MeteorEntityRenderer::new);
    }
}
