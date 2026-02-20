package github.catchaos8.levelup.client;

//import github.catchaos8.levelup.network.SetLimitedStatPacket;
//import github.catchaos8.levelup.network.SpendStatPointPacket;
//import github.catchaos8.levelup.network.StatsDataPacket;

import github.catchaos8.levelup.Config;
import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.networking.packets.SetLimitedStatsC2SPacket;
import github.catchaos8.levelup.networking.packets.SpendPointsC2SPacket;
import github.catchaos8.levelup.registries.ModAttributes;
import github.catchaos8.levelup.util.FormulaParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class StatsScreen extends Screen {

    private static final Holder<Attribute>[] attributes = new Holder[]{
            ModAttributes.CONSTITUTION,
            ModAttributes.DEXTERITY,
            ModAttributes.STRENGTH,
            ModAttributes.VITALITY,
            ModAttributes.WISDOM,
            ModAttributes.INTELLIGENCE
    };

    private List<ModConfigSpec.ConfigValue<List<? extends String>>> lists = List.of(
            Config.CONSTITUTION_ATTRIBUTES,
            Config.DEXTERITY_ATTRIBUTES,
            Config.STRENGTH_ATTRIBUTES,
            Config.VITALITY_ATTRIBUTES,
            Config.WISDOM_ATTRIBUTES,
            Config.INTELLIGENCE_ATTRIBUTES);

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

    private int spendIndex = 0;

    private final int[] spendIncrements = {1, 3, 5, 10, 15, 20, 25, 50, 100, 250, 500, 1000};

    private StatSlider[] sliders = new StatSlider[STAT_NAMES.length];

    private String formula = Config.XP_FORMULA.get();
    private int xpRequired = (int) Math.round(FormulaParser.evaluate(formula, level));

    public StatsScreen() {
        super(Component.translatable("gui.levelup.level_gui"));
    }

    @Override
    protected void init() {
        int upperPanelY = height/2 - LOWER_PANEL_HEIGHT/2 - UPPER_PANEL_HEIGHT/2;
        int upperPanelX = width/2 - UPPER_PANEL_WIDTH/2;

        int lowerPanelY = height/2 - LOWER_PANEL_HEIGHT/2 + UPPER_PANEL_HEIGHT/2 + PANEL_GAP;
        int lowerPanelX = width/2 - LOWER_PANEL_WIDTH/2;

        int sliderSize = 12;

        for(int i = 0; i < STAT_NAMES.length; i++) {
            final int statIndex = i;
            int rowY = lowerPanelY + 24 + i*step;

            this.addRenderableWidget(new Button.Builder(
                    Component.translatable("gui.levelup.plus"),
                    btn -> {
                        PacketDistributor.sendToServer(new SpendPointsC2SPacket(statIndex, spendIncrements[spendIndex]));
                    }).pos(lowerPanelX + LOWER_PANEL_WIDTH - 80 - sliderSize, rowY)
                    .tooltip(Tooltip.create(Component.translatable("gui.levelup."+ STAT_NAMES[i].toLowerCase() + "_upgrade")))
                    .size(sliderSize, sliderSize)
                    .build()
            );

            StatSlider slider = new StatSlider(lowerPanelX + LOWER_PANEL_WIDTH - 70, rowY, 60, sliderSize, statIndex);
            this.sliders[i] = slider;
            this.addRenderableWidget(slider);
        }


        //Step increase
        this.addRenderableWidget(new Button.Builder(
                Component.translatable("gui.levelup.plus"),
                btn -> {
                    this.spendIndex = (spendIndex+ 1) % spendIncrements.length;
                }).pos(upperPanelX+UPPER_PANEL_WIDTH-36, upperPanelY + 30)
                .tooltip(Tooltip.create(Component.translatable("gui.levelup.step_description")))
                .size(sliderSize, sliderSize)
                .build());

        //Step minus
        this.addRenderableWidget(new Button.Builder(
                Component.translatable("gui.levelup.minus"),
                btn -> {
                    this.spendIndex = (spendIndex - 1 + spendIncrements.length) % spendIncrements.length;
                }).pos(upperPanelX+UPPER_PANEL_WIDTH-77, upperPanelY + 30)
                .tooltip(Tooltip.create(Component.translatable("gui.levelup.step_description")))
                .size(sliderSize, sliderSize)
                .build());

        refreshSliders();
    }

    private void refreshSliders() {
        for (int i = 0; i < STAT_NAMES.length; i++) {
            if (sliders[i] != null) {
                assert Minecraft.getInstance().player != null;
                sliders[i].updateRange((int) Minecraft.getInstance().player.getAttributeValue(attributes[i]), limitedStats[i]);
            }
        }
    }

    private void drawXPBar(GuiGraphics graphics, int x, int y, int width, int height) {
        double fillPercent = (double) Math.round(100 * (xp / xpRequired)) /100;
        int filledWidth = (int) Math.min((width*fillPercent), width);


        graphics.blitSprite(ResourceLocation.fromNamespaceAndPath(LevelUP.MOD_ID, "gui/sprites/levelup/experience_bar_background"),
                x, y,
                width, height
        );

        if(fillPercent > 0) {
            graphics.blitSprite(ResourceLocation.fromNamespaceAndPath(LevelUP.MOD_ID, "gui/sprites/levelup/experience_bar_progress"),
                    x, y,
                    filledWidth, height
            );
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


        //==============================UPPER PANEL==============================
        //Display 'username's stats'
        graphics.drawString(this.font, Component.literal(name + Component.translatable("gui.levelup.username_stat").getString()),
                upperPanelX + 10,
                upperPanelY + 10,
                0xFFFFFF);

        //Draw XP display
        graphics.drawCenteredString(this.font, Component.literal(xp + "/" + xpRequired +
                Component.translatable("gui.levelup.xp").getString()),
                upperPanelX + UPPER_PANEL_WIDTH - 50,
                upperPanelY + 21,
                0xFFFFFF
                );

        drawXPBar(graphics, upperPanelX + 10, upperPanelY + 22, UPPER_PANEL_WIDTH/2 - 10, 5);

        //Display Level
        graphics.drawCenteredString(this.font, Component.literal(Component.translatable("gui.levelup.level").getString() + level)
        , upperPanelX + UPPER_PANEL_WIDTH - 50,
                upperPanelY + 10,
                0xFFFFFF);

        //Display Free Points
        graphics.drawString(this.font, Component.literal(Component.translatable("stat.levelup.fp").getString() + freePoints),
                upperPanelX + 10,
                upperPanelY + 32,
                0xFFFFFF);

        //Draw Step
        graphics.drawCenteredString(this.font, Component.literal(String.valueOf(spendIncrements[spendIndex])),
                upperPanelX+UPPER_PANEL_WIDTH-50,
                upperPanelY + 32,
                0xFFFFFF);

        //Draw Titles
        graphics.drawString(this.font, Component.translatable("gui.levelup.stats"),
                lowerPanelX + 10,
                lowerPanelY + 10, 0xFFFFFF);
        graphics.drawString(this.font, Component.translatable("gui.levelup.total_stats"),
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

            //Display attribute value
            if(ClientData.getBase() != null && Minecraft.getInstance().player != null) {
                int amount = (int) Minecraft.getInstance().player.getAttributeValue(attributes[i]);

                graphics.drawString(this.font,
                        Component.literal(String.valueOf(amount)),
                        lowerPanelX + 72,
                        rowY,
                        0xFFFFFF
                );
            }
        }


        super.render(graphics, mouseX, mouseY, partialTick);

        // Render stat tooltips
        for(int i = 0; i < STAT_NAMES.length; i++) {
            int rowY = lowerPanelY + 26 + i*step;
            int textWidth = this.font.width(Component.translatable("stat.levelup." + STAT_NAMES[i]));

            if(mouseX >= lowerPanelX + 10 && mouseX <= lowerPanelX + textWidth - 10 &&
                    mouseY >= rowY && mouseY <= rowY + this.font.lineHeight) {

                List<Component> tooltip = buildStatTooltipList(i);
                graphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);

                break;
            }
        }
        // Render stat value tooltips
        for(int i = 0; i < STAT_NAMES.length; i++) {
            int rowY = lowerPanelY + 26 + i*step;

            if(ClientData.getBase() != null && Minecraft.getInstance().player != null) {
                double attributeValue = Minecraft.getInstance().player.getAttributeValue(attributes[i]);
                String displayText = String.format("%d", (int) attributeValue);
                int textWidth = this.font.width(displayText);

                if(mouseX >= lowerPanelX + 72 && mouseX <= lowerPanelX + 72 + textWidth &&
                        mouseY >= rowY && mouseY <= rowY + this.font.lineHeight) {

                    List<Component> tooltip = buildAttributeBreakdownTooltip(i);
                    graphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);

                    break;
                }
            }
        }


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
    private List<Component> buildStatTooltipList(int statIndex) {
        List<Component> tooltipLines = new java.util.ArrayList<>();

        if(ClientData.getBase() == null) return tooltipLines;

        int statValue = ClientData.getBase()[statIndex];
        List<? extends String> attributeList = lists.get(statIndex).get();

        for(String entry : attributeList) {
            String[] parts = entry.split(",");
            if(parts.length != 3) continue;

            String attributeID = parts[0].trim();
            String operation = parts[1].trim();
            double amountPerPoint = Double.parseDouble(parts[2].trim());
            double totalBonus = amountPerPoint*statValue;

            ResourceLocation attrLocation = ResourceLocation.parse(attributeID);
            Holder<Attribute> attribute = BuiltInRegistries.ATTRIBUTE.getHolder(attrLocation).orElse(null);

            String attributeName = "";
            if(attribute != null) attributeName = Component.translatable(attribute.value().getDescriptionId()).getString();

            String sign = totalBonus >= 0 ? "+" : "";
            String valueString;

            if(operation.equals("multiplication")) {
                valueString = String.format("%s%.1f%%", sign, totalBonus * 100);
            } else {
                valueString = String.format("%s%.2f", sign, totalBonus);
            }

            tooltipLines.add(Component.literal(valueString + " " + attributeName));
        }

        return tooltipLines;
    }

    private List<Component> buildAttributeBreakdownTooltip(int statIndex) {
        List<Component> tooltipLines = new java.util.ArrayList<>();

        if(ClientData.getBase() == null || Minecraft.getInstance().player == null) return tooltipLines;

        int baseStatValue = ClientData.getBase()[statIndex];
        double totalAttributeValue = Minecraft.getInstance().player.getAttributeValue(attributes[statIndex]);
        double itemBonus = totalAttributeValue - baseStatValue;

        tooltipLines.add(Component.translatable("gui.levelup.base").append(": " + String.format("%d", baseStatValue)));
        tooltipLines.add(Component.translatable("gui.levelup.items").append(": " + String.format("%.0f", itemBonus)));
        tooltipLines.add(Component.translatable("gui.levelup.total").append(": " + String.format("%.0f", totalAttributeValue)));

        return tooltipLines;
    }

}