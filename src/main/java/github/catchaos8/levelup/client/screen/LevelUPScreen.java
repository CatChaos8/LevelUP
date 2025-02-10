package github.catchaos8.levelup.client.screen;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.attributes.ModAttributes;
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


    private static final Component LIMIT = Component.translatable("gui.levelup.limit");

    private static final ResourceLocation GUI_LOCATION = new ResourceLocation(LevelUP.MOD_ID, "textures/gui/container/levelup_gui.png");
    private static final ResourceLocation XP_BAR_BG = new ResourceLocation(LevelUP.MOD_ID, "textures/gui/sprites/levelup/experience_bar_background.png");
    private static final ResourceLocation XP_BAR_FULL = new ResourceLocation(LevelUP.MOD_ID, "textures/gui/sprites/levelup/experience_bar_progress.png");

    private static final int increase_button_x = 158;
    private static final int info_button_x = 8;
    private static final int text_x = 28;

    private final int imageWidth, imageHeight;

    private int leftPos, topPos;

    private Button constitutionInfo;
    private Button dexterityInfo;
    private Button strengthInfo;
    private Button vitalityInfo;
    private Button enduranceInfo;


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


        //Con
        addRenderableWidget(
                ImageButton.builder(
                                PLUS,
                                this::handleConButton)
                        .bounds(this.leftPos + increase_button_x, this.topPos + 38, 10, 10)
                        .build());
        //Dex
        addRenderableWidget(
                ImageButton.builder(
                                PLUS,
                                this::handleDexButton)
                        .bounds(this.leftPos + increase_button_x, this.topPos + 58, 10, 10)
                        .build());

        //Str
        addRenderableWidget(
                ImageButton.builder(
                                PLUS,
                                this::handleStrButton)
                        .bounds(this.leftPos + increase_button_x, this.topPos + 78, 10, 10)
                        .build());

        //Vit
        addRenderableWidget(
                ImageButton.builder(
                                PLUS,
                                this::handleVitButton)
                        .bounds(this.leftPos + increase_button_x, this.topPos + 98, 10, 10)
                        .build());

        //Endurance
        addRenderableWidget(
                ImageButton.builder(
                                PLUS,
                                this::handleEndButton)
                        .bounds(this.leftPos + increase_button_x, this.topPos + 118, 10, 10)
                        .build());

        addRenderableWidget(
                ImageButton.builder(
                                LIMIT,
                                this::handleLimitButton)
                        .bounds(this.leftPos + 108, this.topPos + 137, 60, 12)
                        .build());

        var player = this.minecraft.player;
        assert player != null;

        constitutionInfo = addRenderableOnly(
                ImageButton.builder(
                                INFO, this::handleInfoButton)
                        .bounds(this.leftPos + info_button_x, this.topPos + 38, 12, 12)
                        .tooltip(Tooltip.create(Component.empty()))
                        .build()
        );

        dexterityInfo = addRenderableOnly(
                ImageButton.builder(
                                INFO, this::handleInfoButton)
                        .bounds(this.leftPos + info_button_x, this.topPos + 58, 12, 12)
                        .tooltip(Tooltip.create(Component.empty()))
                        .build()
        );

        strengthInfo = addRenderableOnly(
                ImageButton.builder(
                                INFO, this::handleInfoButton)
                        .bounds(this.leftPos + info_button_x, this.topPos + 78, 12, 12)
                        .tooltip(Tooltip.create(Component.empty()))
                        .build()
        );

        vitalityInfo = addRenderableOnly(
                ImageButton.builder(
                                INFO, this::handleInfoButton)
                        .bounds(this.leftPos + info_button_x, this.topPos + 98, 12, 12)
                        .tooltip(Tooltip.create(Component.empty()))
                        .build()
        );

        enduranceInfo = addRenderableOnly(
                ImageButton.builder(
                                INFO, this::handleInfoButton)
                        .bounds(this.leftPos + info_button_x, this.topPos + 118, 12, 12)
                        .tooltip(Tooltip.create(Component.empty()))
                        .build()
        );


    }


    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        graphics.blit(GUI_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);
        assert this.minecraft != null;

        var player = this.minecraft.player;
        assert player != null;


        drawXpBar(graphics, 7,20, this.imageWidth - 14);

        graphics.drawString(this.font, CONSTITUTION.getString() + "%d" .formatted((int) player.getAttributeValue(ModAttributes.CONSTITUTION.get())),
                this.leftPos + text_x, this.topPos + 40, 0x404040, false);
        graphics.drawString(this.font, DEXTERITY.getString() + "%d".formatted((int) player.getAttributeValue(ModAttributes.DEXTERITY.get())),
                this.leftPos + text_x, this.topPos + 60, 0x404040, false);
        graphics.drawString(this.font, STRENGTH.getString() + "%d".formatted((int) player.getAttributeValue(ModAttributes.STRENGTH.get())),
                this.leftPos + text_x, this.topPos + 80, 0x404040, false);
        graphics.drawString(this.font, VITALITY.getString() + "%d".formatted((int) player.getAttributeValue(ModAttributes.VITALITY.get())),
                this.leftPos + text_x, this.topPos + 100, 0x404040, false);
        graphics.drawString(this.font, ENDURANCE.getString() + "%d".formatted((int) player.getAttributeValue(ModAttributes.ENDURANCE.get())),
                this.leftPos + text_x, this.topPos + 120, 0x404040, false);
        graphics.drawString(this.font, FREEPOINTS.getString() + "%d".formatted(this.getStat(5)),
                this.leftPos + 8, this.topPos + 140, 0x404040, false);


        Component CON_INFO = Component.translatable("gui.levelup.con_description").append(String.valueOf((int) player.getAttributeValue(ModAttributes.CONSTITUTION.get()))).append(Component.translatable("gui.levelup.newline"))
                .append(Component.translatable("gui.levelup.base")).append(String.valueOf(getStat(0)))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.items"))
                .append(String.valueOf((int) player.getAttributeValue(ModAttributes.CONSTITUTION.get()) - getStat(0)))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.line"))

                .append(PLUS.getString() + LevelUPCommonConfig.CONSTITUTION_HP.get()*100*getStat(8) + Component.translatable("gui.levelup.percent_hp").getString())
                .append(PLUS.getString() + LevelUPCommonConfig.CONSTITUTION_FALL_DAMAGE_REDUCTION.get()*getStat(8) + Component.translatable("gui.levelup.max_fall_height").getString());

        constitutionInfo.setTooltip(Tooltip.create(CON_INFO));

        Component DEX_INFO = Component.translatable("gui.levelup.dex_description").append(String.valueOf((int) player.getAttributeValue(ModAttributes.DEXTERITY.get()))).append(Component.translatable("gui.levelup.newline"))
                .append(Component.translatable("gui.levelup.base")).append(String.valueOf(getStat(1)))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.items"))
                .append(String.valueOf((int) player.getAttributeValue(ModAttributes.DEXTERITY.get()) - getStat(1)))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.line"))

                .append(PLUS.getString() + LevelUPCommonConfig.DEXTERITY_SPEED.get()*100*getStat(9) + Component.translatable("gui.levelup.speed").getString())
                .append(PLUS.getString() + LevelUPCommonConfig.DEXTERITY_SWIM_SPEED.get()*100*getStat(9) + Component.translatable("gui.levelup.swim_speed").getString());

        dexterityInfo.setTooltip(Tooltip.create(DEX_INFO));

        Component STR_INFO = Component.translatable("gui.levelup.str_description").append(String.valueOf((int) player.getAttributeValue(ModAttributes.STRENGTH.get()))).append(Component.translatable("gui.levelup.newline"))
                .append(Component.translatable("gui.levelup.base")).append(String.valueOf(getStat(2)))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.items"))
                .append(String.valueOf((int) player.getAttributeValue(ModAttributes.STRENGTH.get()) - getStat(2)))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.line"))

                .append(PLUS.getString() + LevelUPCommonConfig.STRENGTH_DAMAGE.get()*100*getStat(10) + Component.translatable("gui.levelup.damage").getString())
                .append(PLUS.getString() + LevelUPCommonConfig.STRENGTH_KNOCKBACK.get()*100*getStat(10) + Component.translatable("gui.levelup.knockback").getString());

        strengthInfo.setTooltip(Tooltip.create(STR_INFO));

        if(this.minecraft == null) return;
        Level level = this.minecraft.level;
        if(level == null) return;

        Component VIT_INFO;

        if(level.getLevelData().isHardcore()) {
            VIT_INFO = Component.translatable("gui.levelup.vit_description").append(String.valueOf((int) player.getAttributeValue(ModAttributes.VITALITY.get()))).append(Component.translatable("gui.levelup.newline"))
                    .append(Component.translatable("gui.levelup.base")).append(String.valueOf(getStat(3)))
                    .append(Component.translatable("gui.levelup.newline"))

                    .append(Component.translatable("gui.levelup.items"))
                    .append(String.valueOf((int) player.getAttributeValue(ModAttributes.VITALITY.get()) - getStat(3)))
                    .append(Component.translatable("gui.levelup.newline"))

                    .append(Component.translatable("gui.levelup.line"))

                    .append(PLUS.getString() + (LevelUPCommonConfig.VITALITY_HP_REGEN.get()*getStat(11)*20)/3 + Component.translatable("gui.levelup.heal").getString())
                    .append(PLUS.getString() + LevelUPCommonConfig.VITALITY_ARMOR.get()*100*getStat(11) + Component.translatable("gui.levelup.armor").getString());
        } else {
            VIT_INFO = Component.translatable("gui.levelup.vit_description").append(String.valueOf((int) player.getAttributeValue(ModAttributes.VITALITY.get()))).append(Component.translatable("gui.levelup.newline"))
                    .append(Component.translatable("gui.levelup.base")).append(String.valueOf(getStat(3)))
                    .append(Component.translatable("gui.levelup.newline"))

                    .append(Component.translatable("gui.levelup.items"))
                    .append(String.valueOf((int) player.getAttributeValue(ModAttributes.VITALITY.get()) - getStat(3)))
                    .append(Component.translatable("gui.levelup.newline"))

                    .append(Component.translatable("gui.levelup.line"))

                    .append(PLUS.getString() + LevelUPCommonConfig.VITALITY_HP_REGEN.get()*getStat(11)*20 + Component.translatable("gui.levelup.heal").getString())
                    .append(PLUS.getString() + LevelUPCommonConfig.VITALITY_ARMOR.get()*100*getStat(11) + Component.translatable("gui.levelup.armor").getString());

        }
        vitalityInfo.setTooltip(Tooltip.create(VIT_INFO));

        Component END_INFO = Component.translatable("gui.levelup.end_description").append(String.valueOf((int) player.getAttributeValue(ModAttributes.ENDURANCE.get()))).append(Component.translatable("gui.levelup.newline"))
                .append(Component.translatable("gui.levelup.base")).append(String.valueOf(getStat(4)))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.items"))
                .append(String.valueOf((int) player.getAttributeValue(ModAttributes.ENDURANCE.get()) - getStat(4)))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.line"))

                .append(PLUS.getString() + LevelUPCommonConfig.ENDURANCE_ARMOR_TOUGHNESS.get()*getStat(12) + Component.translatable("gui.levelup.toughness").getString())
                .append(PLUS.getString() + LevelUPCommonConfig.ENDURANCE_KNOCKBACK_RESISTANCE.get()*100*getStat(12) + Component.translatable("gui.levelup.knockback_resistance").getString());

        enduranceInfo.setTooltip(Tooltip.create(END_INFO));

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

    private void drawXpBar(@NotNull GuiGraphics graphics, int x, int y, int width) {
        int currentXp = getStat(6); // Get XP value
        int level = getStat(7);
        int maxXp = (int) (LevelUPCommonConfig.A_VALUE.get() * (level * level) + LevelUPCommonConfig.B_VALUE.get() * level + LevelUPCommonConfig.C_VALUE.get()); // Dynamic max XP calculation

        // Calculate the width of the filled portion
        int filledWidth = (int) ((currentXp / (float) maxXp) * width);

        // Adjust position relative to `leftPos` and `topPos`
        int adjustedX = x + this.leftPos;
        int adjustedY = y + this.topPos;

        // Draw the XP bar background using the XP_BAR_BG texture (142x5)
        // The background needs to be scaled properly to match the XP bar size
        graphics.blit(XP_BAR_BG, adjustedX, adjustedY-10, 0, 0, width, 5, 162, 5); // Background texture

        // Draw the XP progress using the XP_BAR_FULL texture (142x5)
        // Scale the progress width based on the current XP
        graphics.blit(XP_BAR_FULL, adjustedX, adjustedY-10, 0, 0, filledWidth, 5, 162, 5); // Progress texture

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
