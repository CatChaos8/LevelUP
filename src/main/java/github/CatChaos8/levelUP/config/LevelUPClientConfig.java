package github.catchaos8.levelup.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LevelUPClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("Client Configs for LevelUP");

        //Config

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
