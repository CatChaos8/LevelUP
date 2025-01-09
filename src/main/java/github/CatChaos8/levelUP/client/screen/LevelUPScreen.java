package github.catchaos8.levelup.client.screen;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.client.ClientStatData;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class LevelUPScreen extends Screen {

    private static final Component TITLE = Component.translatable("gui.levelup.level_gui");
    private static final Component CONSTITUTION = Component.translatable("stat.levelup.con");
    private static final Component DEXTERITY = Component.translatable("stat.levelup.dex");
    private static final Component STRENGTH = Component.translatable("stat.levelup.str");
    private static final Component VITALITY = Component.translatable("stat.levelup.vit");
    private static final Component ENDURANCE = Component.translatable("stat.levelup.end");

    private static final Component PLUS = Component.translatable("gui.levelup.plus");
    private static final ResourceLocation GUI_LOCATION = new ResourceLocation(LevelUP.MOD_ID, "textures/gui/container/levelup_gui.png");


    private final int imageWidth, imageHeight;

    private int leftPos, topPos;

    private Button increaseCon;

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

        this.increaseCon = addRenderableWidget(
                ImageButton.builder(
                                PLUS,
                        this::handleConstitutionButton)
                        .bounds(this.leftPos + 158, this.topPos + 38, 10, 10)
                        .build());

    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        graphics.blit(GUI_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);
        assert this.minecraft != null;


        graphics.drawString(this.font, CONSTITUTION.getString() + "%d" .formatted(this.getStat(0)),
                this.leftPos + 8, this.topPos + 40, 0x404040, false);
        graphics.drawString(this.font, DEXTERITY.getString() + "%d".formatted(this.getStat(1)),
                this.leftPos + 8, this.topPos + 60, 0x404040, false);
        graphics.drawString(this.font, STRENGTH.getString() + "%d".formatted(this.getStat(2)),
                this.leftPos + 8, this.topPos + 80, 0x404040, false);
        graphics.drawString(this.font, VITALITY.getString() + "%d".formatted(this.getStat(3)),
                this.leftPos + 8, this.topPos + 100, 0x404040, false);
        graphics.drawString(this.font, ENDURANCE.getString() + "%d".formatted(this.getStat(4)),
                this.leftPos + 8, this.topPos + 120, 0x404040, false);


    }
    private void handleConstitutionButton(Button increase) {

    }

    private int getStat(int type) {
        int[] stats = ClientStatData.getPlayerStats();
        if(this.minecraft != null && this.minecraft.player != null) {
            return stats[type];
        }
        return 0;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
