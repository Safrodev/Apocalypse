package safro.apocalypse.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import safro.apocalypse.event.CommonEvents;
import safro.apocalypse.event.MeteorShowerHandler;
import safro.apocalypse.network.NetworkHelper;
import safro.apocalypse.network.SyncApocalypsePacket;

public class ApocalypseData extends SavedData {
    public static final String KEY = "apocalypse_data";
    public static ApocalypseData CLIENT_INSTANCE;
    private ApocalypseType type;
    private long countdownTick;
    private boolean started;
    private long tick;

    public ApocalypseData() {
        this.countdownTick = -1;
        this.tick = 0;
        this.setDirty();
    }

    public static ApocalypseData get(Level world) {
        if (!world.isClientSide()) {
            return get(((ServerLevel)world).getServer());
        } else {
            return CLIENT_INSTANCE;
        }
    }

    public static ApocalypseData get(MinecraftServer server) {
        ServerLevel serverLevel = server.overworld();
        return serverLevel.getDataStorage().computeIfAbsent(ApocalypseData::load, ApocalypseData::new, KEY);
    }

    public static boolean queue(MinecraftServer server, ApocalypseType type, long ticksToStart) {
        ApocalypseData data = get(server);
        if (!data.started) {
            data.type = type;
            data.countdownTick = ticksToStart;
            data.setDirty();
            return true;
        }
        return false;
    }

    public void clear() {
        this.tick = 0;
        this.countdownTick = -1;
        this.started = false;
        this.type = null;
        this.setDirty();
        NetworkHelper.sendToAllPlayers(new SyncApocalypsePacket(this.type, this.countdownTick, this.started, this.tick));
    }

    public void tick(ServerLevel overworld) {
        if (!this.started) {
            if (this.countdownTick > 0) {
                --this.countdownTick;

                if (this.countdownTick == 0) {
                    this.countdownTick = -1;
                    this.started = true;
                    CommonEvents.startApocalypse(this.type, overworld);
                    this.setDirty();
                }
            }

            if (this.countdownTick % 20 == 0) {
                this.setDirty();
            }
        } else {
            this.tick++;

            if (this.tick % 20 == 0) {
                this.tickEvent(overworld); // tick events every second to not overstress the server
                this.setDirty();
            }
        }
        NetworkHelper.sendToAllPlayers(new SyncApocalypsePacket(this.type, this.countdownTick, this.started, this.tick));
    }

    private void tickEvent(ServerLevel level) {
        if (this.type == ApocalypseType.METEOR_SHOWER) {
            MeteorShowerHandler.tick(level);
        }
    }

    public boolean hasStarted(ApocalypseType match) {
        return this.started && this.type != null && match == this.type;
    }

    public long getCountdownTick() {
        return this.countdownTick;
    }

    public int getDaysSinceStart() {
        return this.started ? (int)(this.tick / 24000L) : -1;
    }

    public static ApocalypseData load(CompoundTag tag) {
        ApocalypseData data = new ApocalypseData();
        if (tag.contains("Type")) {
            data.type = ApocalypseType.parseType(tag.getString("Type"));
        }
        data.started = tag.getBoolean("Started");
        data.countdownTick = tag.getLong("CountdownTick");
        data.tick = tag.getLong("Tick");
        return data;
    }

    public static ApocalypseData fromData(ApocalypseType t, long ct, boolean st, long ticks) {
        ApocalypseData data = new ApocalypseData();
        data.type = t;
        data.countdownTick = ct;
        data.started = st;
        data.tick = ticks;
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        if (this.type != null) {
            tag.putString("Type", this.type.name());
        }
        tag.putLong("CountdownTick", this.countdownTick);
        tag.putLong("Tick", this.tick);
        tag.putBoolean("Started", this.started);
        return tag;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.type == null ? "None" : this.type.name());
        buf.writeLong(this.countdownTick);
        buf.writeBoolean(this.started);
        buf.writeLong(this.tick);
    }
}
