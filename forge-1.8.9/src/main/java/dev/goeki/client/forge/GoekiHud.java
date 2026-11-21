package dev.goeki.client.forge;

import dev.goeki.client.core.Goeki;
import dev.goeki.client.config.ProfileManager;
import dev.goeki.client.module.Module;
import dev.goeki.client.util.FrameMonitor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.util.Collection;

public final class GoekiHud {
    private final ProfileManager profiles;
    private final FrameMonitor frames;
    public GoekiHud(ProfileManager profiles,FrameMonitor frames) { this.profiles=profiles;this.frames=frames; }
    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void onOverlay(net.minecraftforge.client.event.RenderGameOverlayEvent.Post event) {
        if (event.type != net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.ALL) return;
        Minecraft mc=Minecraft.getMinecraft();
        if (mc.thePlayer==null || mc.theWorld==null || mc.gameSettings.hideGUI) return;
        frames.frame(System.nanoTime());
        if (!enabled("HUD")) return;
        int x=profiles.getHudX(),y=profiles.getHudY(),accent=0xFF000000|profiles.accentRgb();
        if (enabled("FPS")) { label(mc,x,y,"FPS  " + Minecraft.getDebugFPS(),accent);y+=15; }
        if (enabled("CPS")) {
            long now=System.currentTimeMillis();
            label(mc,x,y,"CPS  " + Goeki.getInstance().getCpsTracker().getLeftCps(now)+" / "+Goeki.getInstance().getCpsTracker().getRightCps(now),accent);y+=15;
        }
        if (enabled("Ping")) { label(mc,x,y,"Ping  " + ping(mc),accent);y+=15; }
        if (enabled("Memory")) { label(mc,x,y,String.format("Frame %.1f ms | RAM %d MB",frames.averageMs(),frames.usedMemoryMb()),accent);y+=15; }
        if (enabled("Direction")) { label(mc,x,y,"Facing  "+heading(mc.thePlayer.rotationYaw),accent);y+=15; }
        if (enabled("Keystrokes")) {
            key(mc,x+24,y,"W",Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()),accent);
            key(mc,x,y+23,"A",Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()),accent);
            key(mc,x+24,y+23,"S",Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()),accent);
            key(mc,x+48,y+23,"D",Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()),accent);
            key(mc,x,y+46,"L",Mouse.isButtonDown(0),accent);
            key(mc,x+24,y+46,"R",Mouse.isButtonDown(1),accent);
            y+=75;
        }
        if (enabled("Armor")) for (int i=3;i>=0;i--) {
            ItemStack item=mc.thePlayer.inventory.armorInventory[i];
            if (item==null) continue;
            label(mc,x,y,item.getDisplayName()+"  "+(item.getMaxDamage()-item.getItemDamage()),accent);y+=15;
        }
        if (enabled("Potions")) {
            Collection effects=mc.thePlayer.getActivePotionEffects();
            for (Object obj:effects) {
                net.minecraft.potion.PotionEffect effect=(net.minecraft.potion.PotionEffect)obj;
                net.minecraft.potion.Potion potion=net.minecraft.potion.Potion.potionTypes[effect.getPotionID()];
                if (potion==null) continue;
                String name=net.minecraft.client.resources.I18n.format(potion.getName());
                int sec=effect.getDuration()/20;
                label(mc,x,y,name+" "+(effect.getAmplifier()+1)+"  "+(sec/60)+":"+String.format("%02d",sec%60),accent);y+=15;
            }
        }
    }
    private boolean enabled(String name) { Module m=Goeki.getInstance().getModuleManager().getByName(name);return m!=null&&m.isEnabled(); }
    private void label(Minecraft mc,int x,int y,String s,int accent) {
        int w=mc.fontRendererObj.getStringWidth(s)+10;
        Gui.drawRect(x,y,x+w,y+13,0xAA11111B);
        Gui.drawRect(x,y,x+2,y+13,accent);
        mc.fontRendererObj.drawStringWithShadow(s,x+6,y+3,0xFFFFFFFF);
    }
    private void key(Minecraft mc,int x,int y,String s,boolean down,int accent) {
        Gui.drawRect(x,y,x+22,y+21,down?accent:0xAA11111B);
        mc.fontRendererObj.drawStringWithShadow(s,x+8,y+7,0xFFFFFFFF);
    }
    private String heading(float yaw) {
        String[] names={"S","SW","W","NW","N","NE","E","SE"};
        int idx=(int)Math.floor((yaw%360+360+22.5)/45.0)%8;
        return names[idx];
    }
    private String ping(Minecraft mc) {
        if (mc.isSingleplayer() || mc.getNetHandler()==null) return "--";
        net.minecraft.client.network.NetworkPlayerInfo info=mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
        return info==null?"--":info.getResponseTime()+"ms";
    }
}
