package dev.goeki.client.forge;

import dev.goeki.client.core.Goeki;
import dev.goeki.client.config.ProfileManager;
import dev.goeki.client.module.Category;
import dev.goeki.client.module.Module;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import java.io.IOException;

/** Compact original Goeki interface; all layout uses vanilla rendering. */
public final class GoekiClickGui extends GuiScreen {
    private static final int W = 420, H = 280;
    private static final int LIST_TOP = 46, LIST_BOTTOM = 235;
    private static final int PANEL = 0xFF14141C;

    private final ProfileManager profiles;
    private Category category = Category.HUD;
    private Module expanded;
    private int x, y, scroll;
    private boolean positioned, dragging, editingHex;
    private int dragX, dragY;
    private String hexBuffer = "";
    public GoekiClickGui(ProfileManager profiles) { this.profiles = profiles; }
    @Override public void initGui() {
        Keyboard.enableRepeatEvents(true);
        if (!positioned) { x=(width-W)/2; y=(height-H)/2; positioned=true; }
    }
    @Override public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        try { profiles.save(); } catch (IOException ex) { System.err.println("[Goeki] Profile save failed: " + ex.getMessage()); }
    }
    private int rowHeight(Module m) { return expanded==m?49:29; }
    private int maxScroll() {
        int total=0;
        for (Module m:Goeki.getInstance().getModuleManager().getModules()) {
            if (m.getCategory()==category) total+=rowHeight(m)+5;
        }
        total-=5;
        return Math.max(0,total-(LIST_BOTTOM-LIST_TOP));
    }
    @Override public void drawScreen(int mouseX,int mouseY,float partialTicks) {
        drawDefaultBackground();
        if (dragging) { x=mouseX-dragX; y=mouseY-dragY; }
        scroll=Math.max(0,Math.min(maxScroll(),scroll));
        int accent=0xFF000000 | profiles.accentRgb();
        int top=y+LIST_TOP, bottom=y+LIST_BOTTOM, cursor=top-scroll;
        drawRect(x,y,x+W,y+H,PANEL);
        drawRect(x,y,x+111,y+H,0xFF1D1D29);
        // Module rows first: anything overflowing the list area is covered by the header/footer strips below.
        for (Module m:Goeki.getInstance().getModuleManager().getModules()) {
            if (m.getCategory()!=category) continue;
            int h=rowHeight(m);
            if (cursor+h>top && cursor<bottom) {
                drawRect(x+123,cursor,x+405,cursor+h,0xFF292936);
                fontRendererObj.drawString(m.getName(),x+132,cursor+10,0xFFFFFFFF);
                drawRect(x+365,cursor+7,x+393,cursor+20,m.isEnabled()?accent:0xFF50505A);
                fontRendererObj.drawString(m.isEnabled()?"ON":"OFF",x+370,cursor+10,0xFFFFFFFF);
                if (expanded==m) fontRendererObj.drawString(m.getDescription(),x+132,cursor+31,0xFFB4B4C1);
            }
            cursor+=h+5;
        }
        drawRect(x+111,y,x+W,top,PANEL);
        drawRect(x+111,bottom,x+W,y+H,PANEL);
        drawRect(x,y,x+W,y+3,accent);
        fontRendererObj.drawStringWithShadow("* GOEKI CLIENT",x+12,y+19,0xFFFFFFFF);
        fontRendererObj.drawString("v"+Goeki.VERSION+" | Utility",x+12,y+34,0xFF9B9BAF);
        Category[] cats=Category.values();
        for (int i=0;i<cats.length;i++) {
            int cy=y+66+i*30;
            if (cats[i]==category) {
                drawRect(x+6,cy,x+105,cy+25,0xFF303044);
                drawRect(x+6,cy,x+9,cy+25,accent);
            }
            fontRendererObj.drawString(cats[i].name(),x+17,cy+9,cats[i]==category?0xFFFFFFFF:0xFFAAAAAF);
        }
        fontRendererObj.drawStringWithShadow(category.name(),x+125,y+18,0xFFFFFFFF);
        drawRect(x+122,y+239,x+408,y+240,0xFF404050);
        fontRendererObj.drawString("ACCENT",x+130,y+251,0xFFAAAAAF);
        drawRect(x+188,y+248,x+207,y+266,accent);
        fontRendererObj.drawString(editingHex?hexBuffer+"_":profiles.getAccent(),x+215,y+252,0xFFFFFFFF);
        fontRendererObj.drawString("RSHIFT / ESC",x+320,y+252,0xFF858595);
        super.drawScreen(mouseX,mouseY,partialTicks);
    }
    @Override protected void mouseClicked(int mx,int my,int button) throws IOException {
        super.mouseClicked(mx,my,button);
        if (button==0 && hit(mx,my,x,y,W,12)) { dragging=true;dragX=mx-x;dragY=my-y;return; }
        if (button==0 && hit(mx,my,x+185,y+244,130,29)) { editingHex=true;hexBuffer=profiles.getAccent();return; }
        Category[] cats=Category.values();
        for (int i=0;i<cats.length;i++) if (button==0 && hit(mx,my,x+6,y+66+i*30,99,25)) {
            category=cats[i];expanded=null;scroll=0;return;
        }
        int cursor=y+LIST_TOP-scroll;
        for (Module m:Goeki.getInstance().getModuleManager().getModules()) {
            if (m.getCategory()!=category) continue;
            int h=rowHeight(m);
            if (my>=y+LIST_TOP && my<y+LIST_BOTTOM && hit(mx,my,x+123,cursor,282,29)) {
                if (button==0) m.toggle();
                if (button==1) expanded=expanded==m?null:m;
                return;
            }
            cursor+=h+5;
        }
    }
    @Override protected void mouseReleased(int mx,int my,int state) { super.mouseReleased(mx,my,state);dragging=false; }
    @Override public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel=org.lwjgl.input.Mouse.getEventDWheel();
        if (wheel!=0) scroll=Math.max(0,Math.min(maxScroll(),scroll+(wheel<0?25:-25)));
    }
    @Override protected void keyTyped(char c,int key) throws IOException {
        if (editingHex) {
            if (key==Keyboard.KEY_RETURN) { profiles.setAccent(hexBuffer);editingHex=false;return; }
            if (key==Keyboard.KEY_ESCAPE) { editingHex=false;return; }
            if (key==Keyboard.KEY_BACK) { if (hexBuffer.length()>0) hexBuffer=hexBuffer.substring(0,hexBuffer.length()-1); return; }
            if (hexBuffer.length()<7 && (c=='#' || Character.digit(c,16)>=0)) hexBuffer+=c;
            return;
        }
        if (key==Keyboard.KEY_ESCAPE || key==Keyboard.KEY_RSHIFT) { mc.displayGuiScreen(null);return; }
        super.keyTyped(c,key);
    }
    private boolean hit(int mx,int my,int ax,int ay,int w,int h) { return mx>=ax&&my>=ay&&mx<ax+w&&my<ay+h; }
    @Override public boolean doesGuiPauseGame() { return false; }
}
