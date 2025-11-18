package github.catchaos8.levelup.client.screen;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.attributes.ModAttributes;
import github.catchaos8.levelup.client.ClientStatData;
import github.catchaos8.levelup.config.LevelUPClientConfig;
import github.catchaos8.levelup.config.LevelUPCommonConfig;
import github.catchaos8.levelup.lib.StatType;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.networking.packet.IncreaseBaseStatC2SPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
    private static final Component MINUS = Component.translatable("gui.levelup.minus");

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

        StatType constitution = getStat(0);
        StatType dexterity = getStat(1);
        StatType strength = getStat(2);
        StatType vitality = getStat(3);
        StatType endurance = getStat(4);


        drawXpBar(graphics, this.imageWidth - 14);

        graphics.drawString(this.font, CONSTITUTION.getString() + "%.0f" .formatted((float) player.getAttributeValue(ModAttributes.CONSTITUTION.get())),
                this.leftPos + text_x, this.topPos + 40, 0x404040, false);
        graphics.drawString(this.font, DEXTERITY.getString() + "%.0f".formatted((float) player.getAttributeValue(ModAttributes.DEXTERITY.get())),
                this.leftPos + text_x, this.topPos + 60, 0x404040, false);
        graphics.drawString(this.font, STRENGTH.getString() + "%.0f".formatted((float) player.getAttributeValue(ModAttributes.STRENGTH.get())),
                this.leftPos + text_x, this.topPos + 80, 0x404040, false);
        graphics.drawString(this.font, VITALITY.getString() + "%.0f".formatted((float) player.getAttributeValue(ModAttributes.VITALITY.get())),
                this.leftPos + text_x, this.topPos + 100, 0x404040, false);
        graphics.drawString(this.font, ENDURANCE.getString() + "%.0f".formatted((float) player.getAttributeValue(ModAttributes.ENDURANCE.get())),
                this.leftPos + text_x, this.topPos + 120, 0x404040, false);
        graphics.drawString(this.font, FREEPOINTS.getString() + "%.2f".formatted(this.getInfo(0)),
                this.leftPos + 8, this.topPos + 140, 0x404040, false);

        Component DO_HP = Component.empty();
        Component DO_FALL = Component.empty();
        MutableComponent CON_INFO = Component.translatable("gui.levelup.con_description").append(String.valueOf((float) player.getAttributeValue(ModAttributes.CONSTITUTION.get()))).append(Component.translatable("gui.levelup.newline"))
                .append(Component.translatable("gui.levelup.base")).append(String.valueOf(constitution.getBase()))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.items"))
                .append(String.valueOf((float) player.getAttributeValue(ModAttributes.CONSTITUTION.get()) - constitution.getBase()))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.line"));
        if(LevelUPCommonConfig.DO_HP.get()) {
            DO_HP = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.CONSTITUTION_HP.get() * 100 * constitution.getLimited() + Component.translatable("gui.levelup.percent_hp").getString());
        } else if(LevelUPClientConfig.DISPLAY_DISABLED_STAT_INCREASE.get()) {
            DO_HP = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.CONSTITUTION_HP.get() * 100 * constitution.getLimited() + Component.translatable("gui.levelup.percent_hp").getString()).withStyle(ChatFormatting.RED);
        }
        if(LevelUPCommonConfig.DO_FALL_DAMAGE_REDUCTION.get()) {
            DO_FALL = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.CONSTITUTION_FALL_DAMAGE_REDUCTION.get() * constitution.getLimited() + Component.translatable("gui.levelup.max_fall_height").getString());
        } else if(LevelUPClientConfig.DISPLAY_DISABLED_STAT_INCREASE.get()) {
            DO_FALL = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.CONSTITUTION_FALL_DAMAGE_REDUCTION.get() * constitution.getLimited() + Component.translatable("gui.levelup.max_fall_height").getString()).withStyle(ChatFormatting.RED);
        }
        CON_INFO.append(DO_HP).append(DO_FALL);
        constitutionInfo.setTooltip(Tooltip.create(CON_INFO));

        MutableComponent DEX_INFO = Component.translatable("gui.levelup.dex_description").append(String.valueOf((float) player.getAttributeValue(ModAttributes.DEXTERITY.get()))).append(Component.translatable("gui.levelup.newline"))
                .append(Component.translatable("gui.levelup.base")).append(String.valueOf(dexterity.getBase()))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.items"))
                .append(String.valueOf((float) player.getAttributeValue(ModAttributes.DEXTERITY.get()) - dexterity.getBase()))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.line"));
        Component DO_SPEED = Component.empty();
        Component DO_ATTACK_SPEED = Component.empty();
        Component DO_SWIM_SPEED = Component.empty();

        if(LevelUPCommonConfig.DO_SPEED.get()) {
            DO_SPEED = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.DEXTERITY_SPEED.get() * 100 * dexterity.getLimited() + Component.translatable("gui.levelup.speed").getString());
        } else if(LevelUPClientConfig.DISPLAY_DISABLED_STAT_INCREASE.get()) {
            DO_SPEED = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.DEXTERITY_SPEED.get() * 100 * dexterity.getLimited() + Component.translatable("gui.levelup.speed").getString()).withStyle(ChatFormatting.RED);
        }
        if(LevelUPCommonConfig.DO_ATTACK_SPEED.get()) {
            DO_ATTACK_SPEED = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.DEXTERITY_ATTACK_SPEED.get() * 100 * dexterity.getLimited() + Component.translatable("gui.levelup.attack_speed").getString());
        } else if(LevelUPClientConfig.DISPLAY_DISABLED_STAT_INCREASE.get()) {
            DO_ATTACK_SPEED = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.DEXTERITY_ATTACK_SPEED.get() * 100 * dexterity.getLimited() + Component.translatable("gui.levelup.attack_speed").getString()).withStyle(ChatFormatting.RED);
        }
        if(LevelUPCommonConfig.DO_SWIM_SPEED.get()) {
            DO_SWIM_SPEED = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.DEXTERITY_SWIM_SPEED.get() * 100 * dexterity.getLimited() + Component.translatable("gui.levelup.swim_speed").getString());
        } else if(LevelUPClientConfig.DISPLAY_DISABLED_STAT_INCREASE.get()) {
            DO_SWIM_SPEED = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.DEXTERITY_SWIM_SPEED.get() * 100 * dexterity.getLimited() + Component.translatable("gui.levelup.swim_speed").getString()).withStyle(ChatFormatting.RED);
        }

        DEX_INFO.append(DO_SPEED).append(DO_ATTACK_SPEED).append(DO_SWIM_SPEED);
        dexterityInfo.setTooltip(Tooltip.create(DEX_INFO));

        MutableComponent STR_INFO = Component.translatable("gui.levelup.str_description").append(String.valueOf((float) player.getAttributeValue(ModAttributes.STRENGTH.get()))).append(Component.translatable("gui.levelup.newline"))
                .append(Component.translatable("gui.levelup.base")).append(String.valueOf(strength.getBase()))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.items"))
                .append(String.valueOf((float) player.getAttributeValue(ModAttributes.STRENGTH.get()) - strength.getBase()))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.line"));
        Component DO_DAMAGE = Component.empty();
        Component DO_KNOCKBACK = Component.empty();
        if(LevelUPCommonConfig.DO_DAMAGE.get()) {
            DO_DAMAGE = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.STRENGTH_DAMAGE.get() * 100 * strength.getLimited() + Component.translatable("gui.levelup.damage").getString());
        } else if(LevelUPClientConfig.DISPLAY_DISABLED_STAT_INCREASE.get()) {
            DO_DAMAGE = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.STRENGTH_DAMAGE.get() * 100 * strength.getLimited() + Component.translatable("gui.levelup.damage").getString()).withStyle(ChatFormatting.RED);
        }
        if(LevelUPCommonConfig.DO_KNOCKBACK.get()) {
            DO_KNOCKBACK = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.STRENGTH_KNOCKBACK.get() * 100 * strength.getLimited() + Component.translatable("gui.levelup.knockback").getString());
        } else if(LevelUPClientConfig.DISPLAY_DISABLED_STAT_INCREASE.get()) {
            DO_KNOCKBACK = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.STRENGTH_KNOCKBACK.get() * 100 * strength.getLimited() + Component.translatable("gui.levelup.knockback").getString()).withStyle(ChatFormatting.RED);
        }
        STR_INFO.append(DO_DAMAGE).append(DO_KNOCKBACK);
        strengthInfo.setTooltip(Tooltip.create(STR_INFO));

        if(this.minecraft == null) return;
        Level level = this.minecraft.level;
        if(level == null) return;

        MutableComponent VIT_INFO;

        VIT_INFO = Component.translatable("gui.levelup.vit_description").append(String.valueOf((float) player.getAttributeValue(ModAttributes.VITALITY.get()))).append(Component.translatable("gui.levelup.newline"))
                .append(Component.translatable("gui.levelup.base")).append(String.valueOf(vitality.getBase()))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.items"))
                .append(String.valueOf((float) player.getAttributeValue(ModAttributes.VITALITY.get()) - vitality.getBase()))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.line"));

        Component DO_HP_REGEN = Component.empty();
        Component DO_HEALING_MULT = Component.empty();
        if(LevelUPCommonConfig.DO_HP_REGEN.get()) {
            if(level.getLevelData().isHardcore()) {
                double hpRegenThing = Math.round((LevelUPCommonConfig.VITALITY_HP_REGEN.get() * vitality.getLimited() * 20)*1000);
                DO_HP_REGEN = Component.translatable("gui.levelup.newline").append(PLUS.getString() + hpRegenThing/1000 / LevelUPCommonConfig.VITALITY_HARDCORE_NERF.get() + Component.translatable("gui.levelup.heal").getString());
            } else {
                double hpRegenThing = Math.round((LevelUPCommonConfig.VITALITY_HP_REGEN.get() * vitality.getLimited() * 20)*1000);
                DO_HP_REGEN = Component.translatable("gui.levelup.newline").append(PLUS.getString() + hpRegenThing/1000 + Component.translatable("gui.levelup.heal").getString());
            }
        } else if(LevelUPClientConfig.DISPLAY_DISABLED_STAT_INCREASE.get()) {
            if(level.getLevelData().isHardcore()) {
                double hpRegenThing = Math.round((LevelUPCommonConfig.VITALITY_HP_REGEN.get() * vitality.getLimited() * 20)*1000);
                DO_HP_REGEN = Component.translatable("gui.levelup.newline").append(PLUS.getString() + hpRegenThing/1000 / LevelUPCommonConfig.VITALITY_HARDCORE_NERF.get() + Component.translatable("gui.levelup.heal").getString()).withStyle(ChatFormatting.RED);
            } else {
                double hpRegenThing = Math.round((LevelUPCommonConfig.VITALITY_HP_REGEN.get() * vitality.getLimited() * 20)*1000);
                DO_HP_REGEN = Component.translatable("gui.levelup.newline").append(PLUS.getString() + hpRegenThing/1000 + Component.translatable("gui.levelup.heal").getString()).withStyle(ChatFormatting.RED);
            }
        }
        if(LevelUPCommonConfig.DO_HEALING.get()) {
            double armorMult = Math.round((LevelUPCommonConfig.ENDURANCE_ARMOR.get() * vitality.getLimited())*1000);
            DO_HEALING_MULT = Component.translatable("gui.levelup.newline").append(PLUS.getString() + armorMult/10 + Component.translatable("gui.levelup.healing").getString());
        } else {
            double armorMult = Math.round((LevelUPCommonConfig.VITALITY_HP_REGEN.get() * vitality.getLimited())*1000);
            DO_HEALING_MULT = Component.translatable("gui.levelup.newline").append(PLUS.getString() + armorMult/10 + Component.translatable("gui.levelup.healing").getString()).withStyle(ChatFormatting.RED);
        }

        VIT_INFO.append(DO_HP_REGEN).append(DO_HEALING_MULT);

        vitalityInfo.setTooltip(Tooltip.create(VIT_INFO));

        Component DO_ARMOR_TOUGHNESS = Component.empty();
        Component DO_KB_RES = Component.empty();
        Component DO_HUNGER = Component.empty();
        Component DO_ARMOR = Component.empty();

        MutableComponent END_INFO = Component.translatable("gui.levelup.end_description").append(String.valueOf((float) player.getAttributeValue(ModAttributes.ENDURANCE.get()))).append(Component.translatable("gui.levelup.newline"))
                .append(Component.translatable("gui.levelup.base")).append(String.valueOf(endurance.getBase()))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.items"))
                .append(String.valueOf((float) player.getAttributeValue(ModAttributes.ENDURANCE.get()) - endurance.getBase()))
                .append(Component.translatable("gui.levelup.newline"))

                .append(Component.translatable("gui.levelup.line"));
        if(LevelUPCommonConfig.DO_ARMOR_TOUGHNESS.get()) {
            double armorThoughnessThing = Math.round((LevelUPCommonConfig.ENDURANCE_ARMOR_TOUGHNESS.get() * endurance.getLimited())*1000);
            DO_ARMOR_TOUGHNESS = Component.translatable("gui.levelup.newline").append(PLUS.getString() + armorThoughnessThing/1000 + Component.translatable("gui.levelup.toughness").getString());
        } else if (LevelUPClientConfig.DISPLAY_DISABLED_STAT_INCREASE.get()) {
            double armorThoughnessThing = Math.round((LevelUPCommonConfig.ENDURANCE_ARMOR_TOUGHNESS.get() * endurance.getLimited())*1000);
            DO_ARMOR_TOUGHNESS = Component.translatable("gui.levelup.newline").append(PLUS.getString() + armorThoughnessThing/1000 + Component.translatable("gui.levelup.toughness").getString()).withStyle(ChatFormatting.RED);
        }
        if(LevelUPCommonConfig.DO_KB_RES.get()) {
            DO_KB_RES = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.ENDURANCE_KNOCKBACK_RESISTANCE.get() * 100 * endurance.getLimited() + Component.translatable("gui.levelup.knockback_resistance").getString());
        } else if(LevelUPClientConfig.DISPLAY_DISABLED_STAT_INCREASE.get()) {
            DO_KB_RES = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.ENDURANCE_KNOCKBACK_RESISTANCE.get() * 100 * endurance.getLimited() + Component.translatable("gui.levelup.knockback_resistance").getString()).withStyle(ChatFormatting.RED);
        }
        if(LevelUPCommonConfig.DO_HUNGER.get()) {
            double hungerThing = (Math.round((100 - Math.pow(1.0-LevelUPCommonConfig.ENDURANCE_HUNGER.get(), endurance.getLimited()) * 100)*1000));
            DO_HUNGER = Component.translatable("gui.levelup.newline").append(MINUS.getString() + hungerThing/1000 + Component.translatable("gui.levelup.hunger").getString());
        } else if(LevelUPClientConfig.DISPLAY_DISABLED_STAT_INCREASE.get()) {
            double hungerThing = (Math.round((100 - Math.pow(1.0-LevelUPCommonConfig.ENDURANCE_HUNGER.get(), endurance.getLimited()) * 100)*1000));
            DO_HUNGER = Component.translatable("gui.levelup.newline").append(MINUS.getString() + hungerThing/1000 + Component.translatable("gui.levelup.hunger").getString()).withStyle(ChatFormatting.RED);
        }
        if(LevelUPCommonConfig.DO_ARMOR.get()) {
            DO_ARMOR = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.ENDURANCE_ARMOR.get() * 100 * endurance.getLimited() + Component.translatable("gui.levelup.armor").getString());
        } else if(LevelUPClientConfig.DISPLAY_DISABLED_STAT_INCREASE.get()){
            DO_ARMOR = Component.translatable("gui.levelup.newline").append(PLUS.getString() + LevelUPCommonConfig.ENDURANCE_ARMOR.get() * 100 * endurance.getLimited() + Component.translatable("gui.levelup.armor").getString()).withStyle(ChatFormatting.RED);
        }

        END_INFO.append(DO_ARMOR_TOUGHNESS).append(DO_KB_RES).append(DO_HUNGER).append(DO_ARMOR);
        enduranceInfo.setTooltip(Tooltip.create(END_INFO));

    }
    private void handleConButton(Button increase) {
        ModNetwork.sendToServer(new IncreaseBaseStatC2SPacket(0, 1.0f));
    }
    private void handleDexButton(Button increase) {
        ModNetwork.sendToServer(new IncreaseBaseStatC2SPacket(1, 1.0f));
    }
    private void handleStrButton(Button increase) {
        ModNetwork.sendToServer(new IncreaseBaseStatC2SPacket(2, 1.0f));
    }
    private void handleVitButton(Button increase) {
        ModNetwork.sendToServer(new IncreaseBaseStatC2SPacket(3, 1.0f));
    }
    private void handleEndButton(Button increase) {
        ModNetwork.sendToServer(new IncreaseBaseStatC2SPacket(4, 1.0f));
    }

    private void handleLimitButton(Button button) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientHooks::openLevelUPLimitGUI);
    }
    private void handleInfoButton(Button info) {

    }


    private float getInfo(int type) {
        float[] info = ClientStatData.getPlayerInfo();
        if(this.minecraft != null && this.minecraft.player != null) {
            return info[type];
        }
        return 0;
    }

    private StatType getStat(int type) {
        StatType[] statTypes = ClientStatData.getStatTypes();
        if (this.minecraft != null && this.minecraft.player != null) {
            return statTypes[type];
        }
        return new StatType(0,0,"Null");
    }

    private void drawXpBar(@NotNull GuiGraphics graphics, int width) {
        float currentXp = getInfo(1); // Get XP value
        float level = getInfo(2);
        int maxXp = (int) (LevelUPCommonConfig.A_VALUE.get() * (level * level) + LevelUPCommonConfig.B_VALUE.get() * level + LevelUPCommonConfig.C_VALUE.get()); // xp to level up

        // get width of the filled portion
        int filledWidth = (int) ((currentXp / (float) maxXp) * width);

        // Adjust position relative to `leftPos` and `topPos`
        int adjustedX = 7 + this.leftPos;
        int adjustedY = 20 + this.topPos;

        // Draw the XP bar background using the XP_BAR_BG texture (142x5)
        // The background needs to be scaled properly to match the XP bar size
        graphics.blit(XP_BAR_BG, adjustedX, adjustedY-10, 0, 0, width, 5, 162, 5); // Background texture

        // Draw the XP progress using the XP_BAR_FULL texture (142x5)
        // Scale the progress width based on the current XP
        graphics.blit(XP_BAR_FULL, adjustedX, adjustedY-10, 0, 0, filledWidth, 5, 162, 5); // Progress texture

        // Optionally, draw the XP value as text
        graphics.drawString(this.font, "Level %.0f - XP: %.0f / %d".formatted(level, currentXp, maxXp),
                adjustedX + width / 2 - this.font.width("Level %.0f - XP: %.0f / %d".formatted(level, currentXp, maxXp)) / 2,
                adjustedY, 0x404040, false); // White text above the bar
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
