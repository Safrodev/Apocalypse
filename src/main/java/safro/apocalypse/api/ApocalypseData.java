package safro.apocalypse.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class ApocalypseData extends SavedData {
    public static final String KEY = "apocalypse_data";
    public static ApocalypseData INSTANCE;
    private ApocalypseType type;
    private long tick;
    private boolean started;

    public ApocalypseData() {
        this.tick = -1;
        this.setDirty();
    }

    public static boolean begin(MinecraftServer server, ApocalypseType type, long ticksToStart) {
        if (INSTANCE == null) {
            ServerLevel level = server.getLevel(Level.OVERWORLD);
            INSTANCE = level.getDataStorage().computeIfAbsent(ApocalypseData::load, ApocalypseData::new, KEY);
            INSTANCE.type = type;
            INSTANCE.tick = ticksToStart;
            INSTANCE.setDirty();
            return true;
        } else {
            if (INSTANCE.started) {
                return false;
            }

            INSTANCE.type = type;
            INSTANCE.tick = ticksToStart;
            INSTANCE.setDirty();
            return true;
        }
    }

    public void tick() {
        if (!this.started) {
            if (this.tick > 0) {
                --this.tick;

                if (this.tick == 0) {
                    this.started = true;
                    this.setDirty();
                }
            }

            if (this.tick % 20 == 0) {
                this.setDirty();
            }
        }
    }

    public static ApocalypseData load(CompoundTag tag) {
        ApocalypseData data = new ApocalypseData();
        data.type = ApocalypseType.parseType(tag.getString("Type"));
        data.started = tag.getBoolean("Started");
        data.tick = tag.getLong("Tick");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("Type", this.type.name());
        tag.putLong("Tick", this.tick);
        tag.putBoolean("Started", this.started);
        return tag;
    }
}
