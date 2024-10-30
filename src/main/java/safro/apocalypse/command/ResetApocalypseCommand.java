package safro.apocalypse.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import safro.apocalypse.api.ApocalypseData;

public class ResetApocalypseCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("apocalypse").requires(sourceStack -> sourceStack.hasPermission(2))
                .then(Commands.literal("clear")
                        .executes(context -> executeClear(context.getSource()))));
    }

    private static int executeClear(CommandSourceStack source) {
        ApocalypseData data = ApocalypseData.get(source.getServer());
        data.clear();
        source.sendSuccess(() -> Component.translatable("command.apocalypse.clear"), true);
        return 1;
    }
}
