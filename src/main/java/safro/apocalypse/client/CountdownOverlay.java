package safro.apocalypse.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.StringUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class CountdownOverlay implements IGuiOverlay {
    public static long SECONDS_LEFT = -1;

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics graphics, float partialTicks, int screenWidth, int screenHeight) {
        Minecraft client = Minecraft.getInstance();
        int secondsLeft = (int)(SECONDS_LEFT);
        if (secondsLeft >= 0) {
            int x = screenWidth / 2 - 8;
            int y = 1;
            String time = StringUtil.formatTickDuration(secondsLeft);
            graphics.fillGradient(x - 1, y - 2, x + client.font.width(time) + 1, y + client.font.lineHeight + 1, -1072689136, -804253680);
            graphics.drawString(client.font, time, x, y, secondsLeft <= 200 ? 0xd83d17 : 16777215);
        }
    }
}
