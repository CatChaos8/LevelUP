package github.catchaos8.levelup.client.screen;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.client.ClientLimitedStatData;
import github.catchaos8.levelup.client.ClientStatData;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.networking.packet.IncreaseStatC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import org.jetbrains.annotations.NotNull;
import org.stringtemplate.v4.ST;

public class LevelUPLimitScreen extends Screen {

    private static final Component TITLE = Component.translatable("gui.levelup.level_gui_limit");
    private static final Component CONSTITUTION = Component.translatable("stat.levelup.con");
    private static final Component DEXTERITY = Component.translatable("stat.levelup.dex");
    private static final Component STRENGTH = Component.translatable("stat.levelup.str");
    private static final Component VITALITY = Component.translatable("stat.levelup.vit");
    private static final Component ENDURANCE = Component.translatable("stat.levelup.end");
    private static final Component FREEPOINTS = Component.translatable("stat.levelup.fp");

    private static final ResourceLocation GUI_LOCATION = new ResourceLocation(LevelUP.MOD_ID, "textures/gui/container/levelup_gui.png");

    private final int imageWidth, imageHeight;

    private int leftPos, topPos;

    public LevelUPLimitScreen() {
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

        ForgeSlider increaseCon = addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 8,
                160,
                20,
                CONSTITUTION,
                Component.empty(),
                0.0,
                getStat(0),
                getLimitedStat(0),
                true
        ));
        increaseCon.setValue(getLimitedStat(0));
        ForgeSlider increaseDex = addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 36,
                160,
                20,
                DEXTERITY,
                Component.empty(),
                0.0,
                getStat(1),
                getLimitedStat(1),
                true
        ));
        ForgeSlider increaseStr = addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 62,
                160,
                20,
                STRENGTH,
                Component.empty(),
                0.0,
                getStat(2),
                getLimitedStat(2),
                true
        ));
        ForgeSlider increaseVit = addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 90,
                160,
                20,
                VITALITY,
                Component.empty(),
                0.0,
                getStat(3),
                getLimitedStat(3),
                true
        ));
        ForgeSlider increaseEnd = addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 118,
                160,
                20,
                ENDURANCE,
                Component.empty(),
                0.0,
                getStat(4),
                getLimitedStat(4),
                true
        ));
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        graphics.blit(GUI_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    private int getStat(int type) {
        int[] stats = ClientStatData.getPlayerStats();
        if(this.minecraft != null && this.minecraft.player != null) {
            return stats[type];
        }
        return 0;
    }
    private int getLimitedStat(int type) {
        int[] stats = ClientLimitedStatData.getLimitedPlayerStats();
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
