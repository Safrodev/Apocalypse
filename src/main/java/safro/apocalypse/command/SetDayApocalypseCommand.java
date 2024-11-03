package safro.apocalypse.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import safro.apocalypse.api.ApocalypseData;

public class SetDayApocalypseCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("apocalypse").requires(sourceStack -> sourceStack.hasPermission(2))
                .then(Commands.literal("setDay")
                        .then(Commands.argument("day", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                .executes(context -> executeSet(context.getSource(), IntegerArgumentType.getInteger(context, "day"))))));
    }

    private static int executeSet(CommandSourceStack source, int day) {
        ApocalypseData data = ApocalypseData.get(source.getServer());
        if (data.hasStarted()) {
            data.setDay(day);
            source.sendSuccess(() -> Component.translatable("command.apocalypse.set_day", day), true);
        } else {
            source.sendFailure(Component.translatable("command.apocalypse.not_started"));
        }
        return 1;
    }
}
