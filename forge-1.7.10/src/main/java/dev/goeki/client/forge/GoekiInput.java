package dev.goeki.client.forge;

import dev.goeki.client.core.Goeki;
import dev.goeki.client.config.ProfileManager;
import dev.goeki.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import org.lwjgl.input.Keyboard;

public final class GoekiInput {
    private final ProfileManager profiles;
    private boolean shift;
    private float previousGamma;
    private boolean gammaOverridden;
    public GoekiInput(ProfileManager profiles) { this.profiles=profiles; }

    /** Counts every real mouse press (not a 20 Hz poll), so fast clicking is not under-reported. */
    @cpw.mods.fml.common.eventhandler.SubscribeEvent
    public void onMouse(MouseEvent event) {
        Minecraft mc=Minecraft.getMinecraft();
        if (!event.buttonstate || mc.theWorld==null || mc.currentScreen!=null) return;
        long now=System.currentTimeMillis();
        if (event.button==0) Goeki.getInstance().getCpsTracker().recordLeftClick(now);
        else if (event.button==1) Goeki.getInstance().getCpsTracker().recordRightClick(now);
    }

    @cpw.mods.fml.common.eventhandler.SubscribeEvent
    public void tick(cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent event) {
        if (event.phase != cpw.mods.fml.common.gameevent.TickEvent.Phase.END) return;
        Minecraft mc=Minecraft.getMinecraft();
        Goeki.getInstance().getModuleManager().onTick();
        boolean current=Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        if (current&&!shift && mc.currentScreen==null) mc.displayGuiScreen(new GoekiClickGui(profiles));
        shift=current;
        Module fullbright=Goeki.getInstance().getModuleManager().getByName("Fullbright");
        boolean shouldBrighten=fullbright!=null&&fullbright.isEnabled()&&mc.theWorld!=null;
        if (shouldBrighten && !gammaOverridden) { previousGamma=mc.gameSettings.gammaSetting;gammaOverridden=true; }
        if (shouldBrighten) mc.gameSettings.gammaSetting=10.0F;
        else if (gammaOverridden) { mc.gameSettings.gammaSetting=previousGamma;gammaOverridden=false; }
    }
}
