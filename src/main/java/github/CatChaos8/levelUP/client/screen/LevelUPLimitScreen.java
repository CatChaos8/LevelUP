package github.catchaos8.levelup.client.screen;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.client.ClientStatData;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.networking.packet.IncreaseStatC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import org.jetbrains.annotations.NotNull;

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

        ForgeSlider con = addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 8,
                160,
                20,
                CONSTITUTION,
                Component.empty(),
                0.0,
                getStat(0),
                getStat(8),
                true
        ) {
            @Override
            public void onRelease(double mouseX, double mouseY) {
                super.onRelease(mouseX, mouseY);

                setLimitedStat(8, (int) this.getValue());
            }
            @Override
            public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
                super.onDrag(mouseX, mouseY, deltaX, deltaY);
                setLimitedStat(8, (int) this.getValue());
            }
        });

        ForgeSlider dex = addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 36,
                160,
                20,
                DEXTERITY,
                Component.empty(),
                0.0,
                getStat(1),
                getStat(9),
                true
        ){
            @Override
            public void onRelease(double mouseX, double mouseY) {
                super.onRelease(mouseX, mouseY);

                setLimitedStat(9, (int) this.getValue());
            }
            @Override
            public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
                super.onDrag(mouseX, mouseY, deltaX, deltaY);
                setLimitedStat(9, (int) this.getValue());
            }
        });

        ForgeSlider str = addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 62,
                160,
                20,
                STRENGTH,
                Component.empty(),
                0.0,
                getStat(2),
                getStat(10),
                true
        ){
            @Override
            public void onRelease(double mouseX, double mouseY) {
                super.onRelease(mouseX, mouseY);

                setLimitedStat(10, (int) this.getValue());
            }
            @Override
            public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
                super.onDrag(mouseX, mouseY, deltaX, deltaY);
                setLimitedStat(10, (int) this.getValue());
            }
        });

        ForgeSlider vit = addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 90,
                160,
                20,
                VITALITY,
                Component.empty(),
                0.0,
                getStat(3),
                getStat(11),
                true
        ){
            @Override
            public void onRelease(double mouseX, double mouseY) {
                super.onRelease(mouseX, mouseY);

                setLimitedStat(11, (int) this.getValue());
            }
            @Override
            public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
                super.onDrag(mouseX, mouseY, deltaX, deltaY);
                setLimitedStat(11, (int) this.getValue());
            }
        });

        ForgeSlider end = addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 118,
                160,
                20,
                ENDURANCE,
                Component.empty(),
                0.0,
                getStat(4),
                getStat(12),
                true
        ){
            @Override
            public void onRelease(double mouseX, double mouseY) {
                super.onRelease(mouseX, mouseY);

                setLimitedStat(12, (int) this.getValue());
            }
            @Override
            public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
                super.onDrag(mouseX, mouseY, deltaX, deltaY);
                setLimitedStat(12, (int) this.getValue());
            }
        });
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

    private void setLimitedStat(int type, int amount) {
        ModNetwork.sendToServer(new IncreaseStatC2SPacket(type, amount));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
