package safro.apocalypse.api;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public enum ApocalypseType {
    SOLAR_APOCALYPSE(() -> true),
    WITHER_STORM(() -> ModList.get().isLoaded("witherstormmod")),
    METEOR_SHOWER(() -> true),
    BLIZZARD(() -> true);

    final Supplier<Boolean> condition;

    ApocalypseType(Supplier<Boolean> condition) {
        this.condition = condition;
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
}
