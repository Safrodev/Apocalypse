package safro.apocalypse.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import safro.apocalypse.client.CountdownOverlay;

public class UpdateCountdownPacket extends S2CPacket {
    private final long seconds;

    public UpdateCountdownPacket(long seconds) {
        this.seconds = seconds;
    }

    public UpdateCountdownPacket(FriendlyByteBuf buf) {
        this.seconds = buf.readLong();
    }

    @Override
    protected void execute() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            CountdownOverlay.SECONDS_LEFT = this.seconds;
//            ClientLevel level = Minecraft.getInstance().level;
//            if (level != null) {
//                LazyOptional<CountdownData> data = level.getCapability(CountdownCapability.CAPABILITY);
//                data.ifPresent(countdownData -> {
//                    countdownData.setSeconds(this.seconds);
//                });
//            }
        });
    }

    @Override
    public void write(FriendlyByteBuf data) {
        data.writeLong(this.seconds);
    }
}
