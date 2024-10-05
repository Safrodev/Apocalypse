package safro.apocalypse.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import safro.apocalypse.Apocalypse;
import safro.apocalypse.api.ApocalypseData;
import safro.apocalypse.api.ApocalypseType;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class StartApocalypseCommand {
    private static final SuggestionProvider<CommandSourceStack> TYPES = SuggestionProviders.register(Apocalypse.id("events"), ApocalypseType::suggestTypes);
    private static final SuggestionProvider<CommandSourceStack> TIME = SuggestionProviders.register(Apocalypse.id("time_formats"), Time::suggestFormats);
    private static final SimpleCommandExceptionType UNAVAILABLE_TYPE = new SimpleCommandExceptionType(Component.translatable("command.apocalypse.invalid_type"));
    private static final SimpleCommandExceptionType INVALID_TIME = new SimpleCommandExceptionType(Component.translatable("command.apocalypse.invalid_time"));
    private static final SimpleCommandExceptionType ALREADY_BEGUN = new SimpleCommandExceptionType(Component.translatable("command.apocalypse.already_begun"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("apocalypse").requires(sourceStack -> sourceStack.hasPermission(2))
                .then(Commands.literal("start")
                        .then(Commands.argument("type", StringArgumentType.string()).suggests(TYPES)
                                .then(Commands.argument("countdown", IntegerArgumentType.integer(1))
                                        .then(Commands.argument("time", StringArgumentType.string()).suggests(TIME)
                                                .executes(context -> executeStart(context.getSource(), StringArgumentType.getString(context, "type"), IntegerArgumentType.getInteger(context, "countdown"), StringArgumentType.getString(context, "time"))))))));
    }

    public static int executeStart(CommandSourceStack source, String rawType, int countdown, String rawTime) throws CommandSyntaxException {
        Time timeformat = Time.parse(rawTime);
        ApocalypseType type = ApocalypseType.parseType(rawType);
        if (timeformat == null) {
            throw INVALID_TIME.create();
        } else if (type == null) {
            throw UNAVAILABLE_TYPE.create();
        } else if (!type.canStart()) {
            throw UNAVAILABLE_TYPE.create();
        }

        if (ApocalypseData.begin(source.getServer(), type, (long)countdown * timeformat.multiplier)) {
            String time = timeformat.name().toLowerCase();
            for (ServerPlayer player : source.getServer().getPlayerList().getPlayers()) {
                player.sendSystemMessage(Component.translatable("command.apocalypse.start", countdown, time));
            }
            source.sendSuccess(() -> Component.translatable("command.apocalypse.start.system", type.name().toLowerCase(), countdown, time).withStyle(ChatFormatting.GRAY), true);
            return 1;
        } else {
            throw ALREADY_BEGUN.create();
        }
    }

    enum Time {
        SECONDS(20L),
        MINUTES(20L * 60L),
        HOURS(20L * 60L * 60L);

        private final long multiplier;

        Time(long multiplier) {
            this.multiplier = multiplier;
        }

        @Nullable
        public static Time parse(String arg) {
            try {
                return Time.valueOf(arg.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        public static CompletableFuture<Suggestions> suggestFormats(CommandContext<SharedSuggestionProvider> context, SuggestionsBuilder builder) {
            return SharedSuggestionProvider.suggest(Arrays.stream(Time.values()).map(Enum::name), builder);
        }
    }
}
