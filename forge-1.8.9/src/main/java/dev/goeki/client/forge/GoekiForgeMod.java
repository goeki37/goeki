package dev.goeki.client.forge;

import dev.goeki.client.core.Goeki;
import dev.goeki.client.config.ProfileManager;
import dev.goeki.client.util.FrameMonitor;
import java.io.File;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(modid="goekiclient",name="Goeki Client",version=Goeki.VERSION,clientSideOnly=true)
public final class GoekiForgeMod {
    private ProfileManager profiles;
    private GoekiInput input;
    @Mod.EventHandler
    public void initialize(net.minecraftforge.fml.common.event.FMLInitializationEvent event) {
        Goeki.getInstance();
        profiles=new ProfileManager(new File(Minecraft.getMinecraft().mcDataDir,"goeki/profiles"));
        try { profiles.load("default"); } catch (IOException ex) { System.err.println("[Goeki] Could not load profile: "+ex.getMessage()); }
        input=new GoekiInput(profiles);
        MinecraftForge.EVENT_BUS.register(new GoekiHud(profiles,new FrameMonitor()));
        MinecraftForge.EVENT_BUS.register(input);
    }
}
