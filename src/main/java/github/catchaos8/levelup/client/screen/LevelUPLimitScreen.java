package github.catchaos8.levelup.client.screen;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.attributes.ModAttributes;
import github.catchaos8.levelup.client.ClientStatData;
import github.catchaos8.levelup.lib.ModNetwork;
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

        //Con
        addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 18,
                160,
                20,
                CONSTITUTION,
                Component.empty(),
                0.0,
                getStat(95),
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

        //dex
        addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 46,
                160,
                20,
                DEXTERITY,
                Component.empty(),
                0.0,
                getStat(96),
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

        //Str
        addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 72,
                160,
                20,
                STRENGTH,
                Component.empty(),
                0.0,
                getStat(97),
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

        //vit
        addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 100,
                160,
                20,
                VITALITY,
                Component.empty(),
                0.0,
                getStat(98),
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

        //end
        addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 128,
                160,
                20,
                ENDURANCE,
                Component.empty(),
                0.0,
                getStat(99),
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
        graphics.drawString(this.font, TITLE,
                this.leftPos + 8, this.topPos + 8, 0x404040, false);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    private int getStat(int type) {
        int[] stats = ClientStatData.getPlayerStats();
        if(this.minecraft != null && this.minecraft.player != null) {
            var player = this.minecraft.player;
            //The high type nums are for getting Attribute. They do not replace low numbers so that u can still display the low numbers
            if (type == 95) {
                return (int) player.getAttributeValue(ModAttributes.CONSTITUTION.get());
            } else if(type == 96) {
                return (int) player.getAttributeValue(ModAttributes.DEXTERITY.get());
            } else if(type == 97) {
                return (int) player.getAttributeValue(ModAttributes.STRENGTH.get());
            } else if(type == 98) {
                return (int) player.getAttributeValue(ModAttributes.VITALITY.get());
            } else if(type == 99) {
                return (int) player.getAttributeValue(ModAttributes.ENDURANCE.get());
            }
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
