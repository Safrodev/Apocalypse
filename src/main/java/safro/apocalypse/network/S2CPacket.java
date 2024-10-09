package safro.apocalypse.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class S2CPacket {

    protected abstract void execute();

    public abstract void write(FriendlyByteBuf data);

    public static void onReceive(S2CPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(packet::execute);
        context.get().setPacketHandled(true);
    }
}
