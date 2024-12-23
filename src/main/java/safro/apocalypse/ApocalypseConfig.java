package safro.apocalypse;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = Apocalypse.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ApocalypseConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue TIMER_X = BUILDER.comment("The x-offset for the countdown timer. Position is relative to the center of the screen.")
            .defineInRange("timerX", 120, Integer.MIN_VALUE, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue TIMER_Y = BUILDER.comment("The y-offset for the countdown timer. Position is relative to the bottom of the screen.")
            .defineInRange("timerY", -3, Integer.MIN_VALUE, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue SA_STAGE_1 = BUILDER.comment("Number of days until stage 1 of the Solar Apocalypse begins.")
            .defineInRange("saStage1", 2, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue SA_STAGE_2 = BUILDER.comment("Number of days until stage 2 of the Solar Apocalypse begins.")
            .defineInRange("saStage2", 4, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue SA_STAGE_3 = BUILDER.comment("Number of days until stage 3 of the Solar Apocalypse begins.")
            .defineInRange("saStage3", 6, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> WS_SPAWN = BUILDER.comment("The spawn coordinates for the Wither Storm Apocalypse. Spawns at (0, 100, 0) by default.")
            .defineList("witherStormCoords", List.of(0, 100, 0), ApocalypseConfig::isValidCoords);

    private static final ForgeConfigSpec.DoubleValue METEOR_CHANCE = BUILDER.comment("The random chance for meteors to fall for the Meteor Apocalypse. The value should be entered as a decimal representation of a percent (ex. 0.05 = 5%).")
            .defineInRange("meteorChance", 0.1, 0.001, 1.0);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int timerXOffset;
    public static int timerYOffset;

    public static int saStage1;
    public static int saStage2;
    public static int saStage3;

    public static List<? extends Integer> witherStormCoords;

    public static double meteorChance;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        timerXOffset = TIMER_X.get();
        timerYOffset = TIMER_Y.get();
        saStage1 = SA_STAGE_1.get();
        saStage2 = SA_STAGE_2.get();
        saStage3 = SA_STAGE_3.get();
        witherStormCoords = WS_SPAWN.get();
        meteorChance = METEOR_CHANCE.get();
    }

    private static boolean isValidCoords(Object object) {
        if (object instanceof List<?> list && list.size() == 3) {
            return list.get(0) instanceof Integer && list.get(1) instanceof Integer && list.get(2) instanceof Integer;
        }
        return false;
    }
}
