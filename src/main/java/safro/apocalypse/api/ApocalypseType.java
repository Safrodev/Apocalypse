package safro.apocalypse.api;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;
import safro.apocalypse.ApocalypseConfig;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public enum ApocalypseType {
    SOLAR_APOCALYPSE(() -> true, ApocalypseConfig.saStage1, ApocalypseConfig.saStage2, ApocalypseConfig.saStage3),
    WITHER_STORM(() -> ModList.get().isLoaded("witherstormmod")),
    METEOR_SHOWER(() -> true);

    final Supplier<Boolean> condition;
    final int[] stages;

    ApocalypseType(Supplier<Boolean> condition, int... stages) {
        this.condition = condition;
        this.stages = stages;
    }

    @Nullable
    public static ApocalypseType parseType(String raw) {
        try {
            return ApocalypseType.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static CompletableFuture<Suggestions> suggestTypes(CommandContext<SharedSuggestionProvider> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Arrays.stream(ApocalypseType.values()).map(type -> type.name().toLowerCase()), builder);
    }

    public boolean canStart() {
        return condition.get();
    }

    public int[] getStages() {
        return stages;
    }
}
