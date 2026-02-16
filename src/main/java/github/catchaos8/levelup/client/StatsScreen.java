package github.catchaos8.levelup.client;

//import github.catchaos8.levelup.network.SetLimitedStatPacket;
//import github.catchaos8.levelup.network.SpendStatPointPacket;
//import github.catchaos8.levelup.network.StatsDataPacket;

import github.catchaos8.levelup.Config;
import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.networking.packets.SetLimitedStatsC2SPacket;
import github.catchaos8.levelup.networking.packets.SpendPointsC2SPacket;
import github.catchaos8.levelup.util.FormulaParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

public class StatsScreen extends Screen {

    private static final String[] STAT_NAMES = {"CON", "DEX", "STR", "VIT", "WIS", "INT"};

    // Local copy of server data
    private int level = 0;
    private double freePoints = 0;
    private double xp = 0;
    private int[] stats = new int[STAT_NAMES.length];
    private int[] limitedStats = new int[STAT_NAMES.length];
    private String name = "Unknown";

    // UI layout
    private static final int LOWER_PANEL_WIDTH = 190;
    private static final int LOWER_PANEL_HEIGHT = 156;

    private static final int UPPER_PANEL_WIDTH = 190;
    private static final int UPPER_PANEL_HEIGHT = 50;

    private static final int PANEL_GAP = 1;

    private static final int step = 22;

    private int spendAmount = 1;

    private StatSlider[] sliders = new StatSlider[STAT_NAMES.length];

    private String formula = Config.XP_FORMULA.get();
    private int xpRequired = (int) Math.round(FormulaParser.evaluate(formula, level));

    public StatsScreen() {
        super(Component.translatable("gui.levelup.level_gui"));
    }

    @Override
    protected void init() {
        int upperPanelY = height/2 - LOWER_PANEL_HEIGHT/2 - UPPER_PANEL_HEIGHT;
        int upperPanelX = width/2 - UPPER_PANEL_WIDTH;

        int lowerPanelY = height/2 - LOWER_PANEL_HEIGHT/2 + UPPER_PANEL_HEIGHT/2 + PANEL_GAP;
        int lowerPanelX = width/2 - LOWER_PANEL_WIDTH/2;

        int sliderSize = 12;

        for(int i = 0; i < STAT_NAMES.length; i++) {
            final int statIndex = i;
            int rowY = lowerPanelY + 24 + i*step;

            this.addRenderableWidget(new Button.Builder(
                    Component.translatable("gui.levelup.plus"),
                    btn -> {
                        PacketDistributor.sendToServer(new SpendPointsC2SPacket(statIndex, spendAmount));
                    }).pos(lowerPanelX + LOWER_PANEL_WIDTH - 80 - sliderSize, rowY)
                    .size(sliderSize, sliderSize)
                    .build()
            );

            StatSlider slider = new StatSlider(lowerPanelX + LOWER_PANEL_WIDTH - 70, rowY, 60, sliderSize, statIndex);
            this.sliders[i] = slider;
            this.addRenderableWidget(slider);
        }

        refreshSliders();
    }

    private void refreshSliders() {
        for (int i = 0; i < STAT_NAMES.length; i++) {
            if (sliders[i] != null) {
                sliders[i].updateRange(stats[i], limitedStats[i]);
            }
        }
    }

    public void updateData() {
        this.level = ClientData.getLevel();
        this.xp = ClientData.getXp();
        this.freePoints = ClientData.getFreepoints();
        this.stats = ClientData.getBase();
        this.limitedStats = ClientData.getLimited();
        this.name = ClientData.getUsername();
        this.xpRequired = (int) Math.round(FormulaParser.evaluate(formula, level));

        refreshSliders();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int upperPanelY = height/2 - LOWER_PANEL_HEIGHT/2 - UPPER_PANEL_HEIGHT/2;
        int upperPanelX = width/2 - UPPER_PANEL_WIDTH/2;

        int lowerPanelY = height/2 - LOWER_PANEL_HEIGHT/2 + UPPER_PANEL_HEIGHT/2 + PANEL_GAP;
        int lowerPanelX = width/2 - LOWER_PANEL_WIDTH/2;

        renderTransparentBackground(graphics);
        renderBlurredBackground(partialTick);

        //Lower BG
        graphics.blitSprite(ResourceLocation.fromNamespaceAndPath(LevelUP.MOD_ID, "gui/container/levelup_gui_slice"),
                this.width/2 - LOWER_PANEL_WIDTH/2, this.height/2 - LOWER_PANEL_HEIGHT/2 + UPPER_PANEL_HEIGHT/2 + PANEL_GAP,
                LOWER_PANEL_WIDTH, LOWER_PANEL_HEIGHT);
        //Upper BG
        graphics.blitSprite(ResourceLocation.fromNamespaceAndPath(LevelUP.MOD_ID, "gui/container/levelup_gui_slice"),
                this.width/2 - UPPER_PANEL_WIDTH/2, this.height/2 - LOWER_PANEL_HEIGHT/2  - UPPER_PANEL_HEIGHT/2,
                UPPER_PANEL_WIDTH, UPPER_PANEL_HEIGHT);


        //Upper Panel strings

//      Display 'username's stats'
        graphics.drawString(this.font, Component.literal(name + Component.translatable("gui.levelup.username_stat").getString()),
                upperPanelX + 10,
                upperPanelY + 10,
                0xFFFFFF);

        //Draw XP display
        graphics.drawCenteredString(this.font, Component.literal(xp + "/" + xpRequired +
                Component.translatable("gui.levelup.xp").getString()),
                upperPanelX + UPPER_PANEL_WIDTH - 50,
                upperPanelY + 10,
                0xFFFFFF
                );


        //Draw Titles
        graphics.drawString(this.font, Component.translatable("gui.levelup.stats"),
                lowerPanelX + 10,
                lowerPanelY + 10, 0xFFFFFF);
        graphics.drawString(this.font, Component.translatable("gui.levelup.total"),
                lowerPanelX + 72,
                lowerPanelY + 10, 0xFFFFFF);
        graphics.drawString(this.font, Component.translatable("gui.levelup.limit"),
                lowerPanelX + LOWER_PANEL_WIDTH - 70,
                lowerPanelY + 10, 0xFFFFFF);

        for(int i = 0;i < STAT_NAMES.length; i++) {
            //Calculate the y of the row
            int rowY = lowerPanelY + 26 + i*step;

            //Display names
            graphics.drawString(this.font,
                    Component.translatable("stat.levelup." + STAT_NAMES[i].toLowerCase()),
                    lowerPanelX + 10,
                    rowY, 0xFFFFFF
                    );

            //Display invested amount
            if(ClientData.getBase() != null) graphics.drawString(this.font,
                    Component.literal(String.valueOf(ClientData.getBase()[i])),
                    lowerPanelX + 72,
                    rowY,
                    0xFFFFFF
                    );
        }



        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyPressed, int scanCode, int modifiers) {

        if(Minecraft.getInstance().options.keyInventory.matches(keyPressed, scanCode)) {
            this.onClose();
            return true;
        } else if (ModKeybindings.OPEN_STATS_SCREEN.matches(keyPressed, scanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyPressed, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }


    private class StatSlider extends AbstractSliderButton {
        private int maxValue;
        private int statIndex;

        public StatSlider(int x, int y, int width, int height, int statIndex) {
            super(x, y, width, height, Component.empty(), 1.0);
            this.maxValue = 1;
            this.statIndex = statIndex;
        }

        public void updateRange(int maxValue, int currentValue) {
            this.maxValue = maxValue;

            if (maxValue == 0) {
                this.value = 0;
                this.active = false; // disable slider if no points invested
            } else {
                this.active = true;
                this.value = (double) currentValue / maxValue;
            }
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            if (maxValue == 0) {
                this.setMessage(Component.literal("0"));
            } else {
                int current = (int) Math.round(this.value * maxValue);
                this.setMessage(Component.literal(String.valueOf(current)));
            }
        }

        @Override
        protected void applyValue() {
            int newValue = (int) Math.round(this.value * maxValue);
            limitedStats[statIndex] = newValue;
            PacketDistributor.sendToServer(new SetLimitedStatsC2SPacket(limitedStats));
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    }
}