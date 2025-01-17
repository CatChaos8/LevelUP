package github.catchaos8.levelup.client.screen;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.client.ClientStatData;
import github.catchaos8.levelup.config.LevelUPCommonConfig;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.networking.packet.IncreaseStatC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

public class LevelUPScreen extends Screen {

    private static final Component TITLE = Component.translatable("gui.levelup.level_gui");
    private static final Component CONSTITUTION = Component.translatable("stat.levelup.con");
    private static final Component DEXTERITY = Component.translatable("stat.levelup.dex");
    private static final Component STRENGTH = Component.translatable("stat.levelup.str");
    private static final Component VITALITY = Component.translatable("stat.levelup.vit");
    private static final Component ENDURANCE = Component.translatable("stat.levelup.end");
    private static final Component FREEPOINTS = Component.translatable("stat.levelup.fp");

    private static final Component PLUS = Component.translatable("gui.levelup.plus");

    private static final Component INFO = Component.translatable("gui.levelup.info");

    private final Component CON_INFO = Component.translatable("gui.levelup.con_description")
            .append(PLUS.getString() + LevelUPCommonConfig.CONSTITUTION_HP.get()*100 + Component.translatable("gui.levelup.percent_hp").getString())
            .append(PLUS.getString() + LevelUPCommonConfig.CONSTITUTION_FALL_DAMAGE_REDUCTION.get() + Component.translatable("gui.levelup.max_fall_height").getString());

    private static final Component LIMIT = Component.translatable("gui.levelup.limit");

    private static final ResourceLocation GUI_LOCATION = new ResourceLocation(LevelUP.MOD_ID, "textures/gui/container/levelup_gui.png");
    private static final ResourceLocation XP_BAR_BG = new ResourceLocation(LevelUP.MOD_ID, "textures/gui/sprites/levelup/experience_bar_background.png");
    private static final ResourceLocation XP_BAR_FULL = new ResourceLocation(LevelUP.MOD_ID, "textures/gui/sprites/levelup/experience_bar_progress.png");

    private static final int increase_button_x = 158;
    private static final int info_button_x = 8;
    private static final int text_x = 28;

    private final int imageWidth, imageHeight;

    private int leftPos, topPos;

    public LevelUPScreen() {
        super(TITLE);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) /2;

        this.topPos = (this.height - this.imageHeight) /2;

        if(this.minecraft == null) return;
        Level level = this.minecraft.level;
        if(level == null) return;

        Button increaseCon = addRenderableWidget(
                ImageButton.builder(
                                PLUS,
                                this::handleConButton)
                        .bounds(this.leftPos + increase_button_x, this.topPos + 38, 10, 10)
                        .build());
        Button increaseDex = addRenderableWidget(
                ImageButton.builder(
                                PLUS,
                                this::handleDexButton)
                        .bounds(this.leftPos + increase_button_x, this.topPos + 58, 10, 10)
                        .build());
        Button increaseStr = addRenderableWidget(
                ImageButton.builder(
                                PLUS,
                                this::handleStrButton)
                        .bounds(this.leftPos + increase_button_x, this.topPos + 78, 10, 10)
                        .build());
        Button increaseVit = addRenderableWidget(
                ImageButton.builder(
                                PLUS,
                                this::handleVitButton)
                        .bounds(this.leftPos + increase_button_x, this.topPos + 98, 10, 10)
                        .build());
        Button increaseEnd = addRenderableWidget(
                ImageButton.builder(
                                PLUS,
                                this::handleEndButton)
                        .bounds(this.leftPos + increase_button_x, this.topPos + 118, 10, 10)
                        .build());

        Button limitStats = addRenderableWidget(
                ImageButton.builder(
                                LIMIT,
                                this::handleLimitButton)
                        .bounds(this.leftPos + 108, this.topPos + 137, 60, 12)
                        .build());

        Button constitutionInfo = addRenderableOnly(
                ImageButton.builder(
                        INFO, this::handleInfoButton)
                        .bounds(this.leftPos + info_button_x, this.topPos + 38, 12, 12)
                        .tooltip(Tooltip.create(CON_INFO))
                        .build()
                );


    }


    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        graphics.blit(GUI_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);
        assert this.minecraft != null;

        drawXpBar(graphics, 7,20, this.imageWidth - 14, 5);

        graphics.drawString(this.font, CONSTITUTION.getString() + "%d" .formatted(this.getStat(0)),
                this.leftPos + text_x, this.topPos + 40, 0x404040, false);
        graphics.drawString(this.font, DEXTERITY.getString() + "%d".formatted(this.getStat(1)),
                this.leftPos + text_x, this.topPos + 60, 0x404040, false);
        graphics.drawString(this.font, STRENGTH.getString() + "%d".formatted(this.getStat(2)),
                this.leftPos + text_x, this.topPos + 80, 0x404040, false);
        graphics.drawString(this.font, VITALITY.getString() + "%d".formatted(this.getStat(3)),
                this.leftPos + text_x, this.topPos + 100, 0x404040, false);
        graphics.drawString(this.font, ENDURANCE.getString() + "%d".formatted(this.getStat(4)),
                this.leftPos + text_x, this.topPos + 120, 0x404040, false);
        graphics.drawString(this.font, FREEPOINTS.getString() + "%d".formatted(this.getStat(5)),
                this.leftPos + 8, this.topPos + 140, 0x404040, false);


    }
    private void handleConButton(Button increase) {
        ModNetwork.sendToServer(new IncreaseStatC2SPacket(0, 1));
    }
    private void handleDexButton(Button increase) {
        ModNetwork.sendToServer(new IncreaseStatC2SPacket(1, 1));
    }
    private void handleStrButton(Button increase) {
        ModNetwork.sendToServer(new IncreaseStatC2SPacket(2, 1));
    }
    private void handleVitButton(Button increase) {
        ModNetwork.sendToServer(new IncreaseStatC2SPacket(3, 1));
    }
    private void handleEndButton(Button increase) {
        ModNetwork.sendToServer(new IncreaseStatC2SPacket(4, 1));
    }
    private void handleLimitButton(Button button) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientHooks::openLevelUPLimitGUI);
    }
    private void handleInfoButton(Button info) {

    }


    private int getStat(int type) {
        int[] stats = ClientStatData.getPlayerStats();
        if(this.minecraft != null && this.minecraft.player != null) {
            return stats[type];
        }
        return 0;
    }

    private void drawXpBar(@NotNull GuiGraphics graphics, int x, int y, int width, int height) {
        int currentXp = getStat(6); // Get XP value
        int level = getStat(7);
        int maxXp = (int) (0.2 * (level * level) + 0.25 * level + 10); // Dynamic max XP calculation

        // Calculate the width of the filled portion
        int filledWidth = (int) ((currentXp / (float) maxXp) * width);

        // Adjust position relative to `leftPos` and `topPos`
        int adjustedX = x + this.leftPos;
        int adjustedY = y + this.topPos;

        // Draw the XP bar background using the XP_BAR_BG texture (142x5)
        // The background needs to be scaled properly to match the XP bar size
        graphics.blit(XP_BAR_BG, adjustedX, adjustedY-10, 0, 0, width, height, 162, 5); // Background texture

        // Draw the XP progress using the XP_BAR_FULL texture (142x5)
        // Scale the progress width based on the current XP
        graphics.blit(XP_BAR_FULL, adjustedX, adjustedY-10, 0, 0, filledWidth, height, 162, 5); // Progress texture

        // Optionally, draw the XP value as text
        graphics.drawString(this.font, "Level %d - XP: %d / %d".formatted(level, currentXp, maxXp),
                adjustedX + width / 2 - this.font.width("Level %d - XP: %d / %d".formatted(level, currentXp, maxXp)) / 2,
                adjustedY, 0x404040, false); // White text above the bar
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
