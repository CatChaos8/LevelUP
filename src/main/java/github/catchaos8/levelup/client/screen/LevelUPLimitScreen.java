package github.catchaos8.levelup.client.screen;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.attributes.ModAttributes;
import github.catchaos8.levelup.client.ClientStatData;
import github.catchaos8.levelup.lib.StatType;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.networking.packet.SetLimitedStatC2SPacket;
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


        StatType constitution = getStatTypes(0);
        StatType dexterity = getStatTypes(1);
        StatType strength = getStatTypes(2);
        StatType vitality = getStatTypes(3);
        StatType endurance = getStatTypes(4);

        //Con
        addRenderableWidget(new ForgeSlider(
                leftPos + 8,
                topPos + 18,
                160,
                20,
                CONSTITUTION,
                Component.empty(),
                0.0,
                getAttribute(0),
                constitution.getLimited(),
                true
        ) {
            @Override
            public void onRelease(double mouseX, double mouseY) {
                super.onRelease(mouseX, mouseY);

                setLimitedStat(0, (float) this.getValue());
            }
            @Override
            public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
                super.onDrag(mouseX, mouseY, deltaX, deltaY);
                setLimitedStat(0, (float) this.getValue());
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
                getAttribute(1),
                dexterity.getLimited(),
                true
        ){
            @Override
            public void onRelease(double mouseX, double mouseY) {
                super.onRelease(mouseX, mouseY);

                setLimitedStat(1, (float) this.getValue());
            }
            @Override
            public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
                super.onDrag(mouseX, mouseY, deltaX, deltaY);
                setLimitedStat(1, (float) this.getValue());
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
                0.0f,
                getAttribute(2),
                strength.getLimited(),
                true
        ){
            @Override
            public void onRelease(double mouseX, double mouseY) {
                super.onRelease(mouseX, mouseY);

                setLimitedStat(2, (float) this.getValue());
            }
            @Override
            public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
                super.onDrag(mouseX, mouseY, deltaX, deltaY);
                setLimitedStat(2, (float) this.getValue());
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
                 getAttribute(3),
                vitality.getLimited(),
                true
        ){
            @Override
            public void onRelease(double mouseX, double mouseY) {
                super.onRelease(mouseX, mouseY);

                setLimitedStat(3, (float) this.getValue());
            }
            @Override
            public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
                super.onDrag(mouseX, mouseY, deltaX, deltaY);
                setLimitedStat(3, (float) this.getValue());
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
                getAttribute(4),
                endurance.getLimited(),
                true
        ){
            @Override
            public void onRelease(double mouseX, double mouseY) {
                super.onRelease(mouseX, mouseY);

                setLimitedStat(4, (float) this.getValue());
            }
            @Override
            public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
                super.onDrag(mouseX, mouseY, deltaX, deltaY);
                setLimitedStat(4, (float) this.getValue());
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

    private StatType getStatTypes(int type) {
        StatType[] statTypes = ClientStatData.getStatTypes();
        return statTypes[type];
    }

    private float getAttribute(int type) {
        if (this.minecraft != null && this.minecraft.player != null) {
            var player = this.minecraft.player;
            return switch (type) {
                case 0 -> (float) player.getAttributeValue(ModAttributes.CONSTITUTION.get());
                case 1 -> (float) player.getAttributeValue(ModAttributes.DEXTERITY.get());
                case 2 -> (float) player.getAttributeValue(ModAttributes.STRENGTH.get());
                case 3 -> (float) player.getAttributeValue(ModAttributes.VITALITY.get());
                case 4 -> (float) player.getAttributeValue(ModAttributes.ENDURANCE.get());
                default -> 0.0f;

            };
        }
        return 0.0f;
    }

    private void setLimitedStat(int type, float amount) {
        ModNetwork.sendToServer(new SetLimitedStatC2SPacket(type, amount));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
