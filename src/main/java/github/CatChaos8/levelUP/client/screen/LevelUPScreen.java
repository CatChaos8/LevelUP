package github.catchaos8.levelup.client.screen;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class LevelUPScreen extends Screen {

    private static final Component TITLE = Component.translatable("gui.levelup.level_gui");
    private static final Component CONSTITUTION_BUTTON = Component.translatable("gui.levelup.constitution");
    private static final ResourceLocation INCREASE_BUTTON_LOCATION = new ResourceLocation(LevelUP.MOD_ID, "textures/gui/container/plus_button.png");
    private static final ResourceLocation GUI_LOCATION = new ResourceLocation(LevelUP.MOD_ID, "textures/gui/container/levelup_gui.png");


    private final int imageWidth, imageHeight;

    private int leftPos, topPos;

    private Button increase;

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

        this.increase = addRenderableWidget(
                ImageButton.builder(
                                CONSTITUTION_BUTTON,
                        this::handleConstitutionButton)
                        .bounds(this.leftPos + 8, this.topPos + 20, 80, 20)
                        .tooltip(Tooltip.create(CONSTITUTION_BUTTON))
                        .build());

    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        graphics.blit(GUI_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);
        assert this.minecraft != null;
        graphics.drawString(this.minecraft.font, TITLE, this.leftPos + 8, this.topPos + 8, 0x404040, false);


        graphics.drawString(this.font, "Constitution: %d".formatted(this.getStat(0)),
                this.leftPos + 20, this.topPos + 20, 0x404040, false);

        graphics.drawString(this.font, "Dexterity: %d".formatted(this.getStat(1)),
                this.leftPos + 20, this.topPos + 40, 0x404040, false);
    }
    private void handleConstitutionButton(Button increase) {

    }

    private int getStat(int type) {
        if(this.minecraft != null && this.minecraft.player != null) {
            Player player = this.minecraft.player;

            player.getCapability(PlayerStatsProvider.PLAYER_STATS).map(stats -> stats.getStat(type));
        }
        return 0;
    }


}
