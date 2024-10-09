package safro.apocalypse.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import safro.apocalypse.Apocalypse;

public class NetworkHelper {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(Apocalypse.id("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void register() {
        CHANNEL.registerMessage(0, UpdateCountdownPacket.class, UpdateCountdownPacket::write, UpdateCountdownPacket::new, UpdateCountdownPacket::onReceive);
    }

    public static void sendToAllPlayers(S2CPacket packet) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }
}
