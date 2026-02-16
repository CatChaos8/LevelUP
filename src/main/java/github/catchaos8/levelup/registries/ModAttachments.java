package github.catchaos8.levelup.registries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Arrays;
import java.util.function.Supplier;

import static github.catchaos8.levelup.LevelUP.MOD_ID;

public class ModAttachments {

    public record ProgressData(int level, double xp, double freePoints) {
        public static final Codec<ProgressData> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("level").forGetter(ProgressData::level),
                        Codec.DOUBLE.fieldOf("xp").forGetter(ProgressData::xp),
                        Codec.DOUBLE.fieldOf("freepoints").forGetter(ProgressData::freePoints)
                ).apply(instance, ProgressData::new)
        );

        public static final ProgressData DEFAULT = new ProgressData(0, 0.0, 0.0);
    }

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

    private static final Codec<int[]> INT_ARRAY_CODEC = Codec.INT.listOf()
            .xmap(
                    list -> list.stream().mapToInt(Integer::intValue).toArray(),
                    arr -> Arrays.stream(arr).boxed().toList()
            );

    public static final Supplier<AttachmentType<int[]>> STATS =
            ATTACHMENT_TYPES.register("stats",
                    () -> AttachmentType.builder(() -> new int[]{
                                    0, //CON
                                    0, //DEX
                                    0, //STR
                                    0, //VIT
                                    0, //WIS
                                    0  //INT
                            })
                            .serialize(INT_ARRAY_CODEC)
                            .copyOnDeath()
                            .build()
            );

    public static final Supplier<AttachmentType<int[]>> LIMITED_STATS =
            ATTACHMENT_TYPES.register("limited_stats",
                    () -> AttachmentType.builder(() -> new int[]{
                                    0, //CON
                                    0, //DEX
                                    0, //STR
                                    0, //VIT
                                    0, //WIS
                                    0  //INT
                            })
                            .serialize(INT_ARRAY_CODEC)
                            .copyOnDeath()
                            .build()
            );

    public static final Supplier<AttachmentType<ProgressData>> PROGRESS =
            ATTACHMENT_TYPES.register("progress",
                    () -> AttachmentType.builder(() -> ProgressData.DEFAULT)
                            .serialize(ProgressData.CODEC)
                            .copyOnDeath()
                            .build()
                    );



}
