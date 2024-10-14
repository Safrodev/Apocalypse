package safro.apocalypse.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import safro.apocalypse.api.ApocalypseData;
import safro.apocalypse.api.ApocalypseType;
import safro.apocalypse.client.CountdownOverlay;

public class SyncApocalypsePacket extends S2CPacket {
    private final ApocalypseData data;

    public SyncApocalypsePacket(ApocalypseType type, long countdown, boolean started, long tick) {
        this.data = ApocalypseData.fromData(type, countdown, started, tick);
    }

    public SyncApocalypsePacket(FriendlyByteBuf buf) {
        ApocalypseType type = ApocalypseType.parseType(buf.readUtf());
        this.data = ApocalypseData.fromData(type, buf.readLong(), buf.readBoolean(), buf.readLong());
    }

    @Override
    protected void execute() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ApocalypseData.CLIENT_INSTANCE = this.data;
            CountdownOverlay.SECONDS_LEFT = this.data.getCountdownTick();
        });
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        this.data.write(buf);
    }
}
