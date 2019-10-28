package com.github.atomicblom.shearmadness.variations;

import com.github.atomicblom.shearmadness.configuration.Settings;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraftforge.common.ForgeConfigSpec;

public class IntegrationSettingsDefinition {
    public static void buildCommonConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Integration with Vanilla Minecraft").push("vanilla");
        {
            IntegrationSettings.Vanilla vanilla = IntegrationSettings.Vanilla.INSTANCE;
            vanilla.allowRedstone =
                    builder.comment(Reference.Configuration.ALLOW_REDSTONE_COMMENT)
                            .define("allowRedstone", true);
            vanilla.allowBookshelf =
                    builder.comment(Reference.Configuration.ALLOW_BOOKSHELF_COMMENT)
                            .define("allowBookshelf", true);
            vanilla.allowGlowstone =
                    builder.comment(Reference.Configuration.ALLOW_GLOWSTONE_COMMENT)
                            .define("allowGlowstone", false);
            vanilla.allowCactus =
                    builder.comment(Reference.Configuration.ALLOW_CACTUS_COMMENT)
                            .define("allowCactus", true);
            vanilla.allowTNT =
                    builder.comment(Reference.Configuration.ALLOW_TNT_COMMENT)
                            .define("allowTNT", true);
            vanilla.allowFireDamage =
                    builder.comment(Reference.Configuration.ALLOW_FIRE_DAMAGE_COMMENT)
                            .define("allowFireDamage", true);
            vanilla.allowAutoCrafting =
                    builder.comment(Reference.Configuration.ALLOW_AUTO_CRAFTING)
                            .define("allowAutoCrafting", true);
        }
        builder.pop();

        builder.comment("Integration with Chance Cubes by Turkey").push("chancecubes");
        {
            IntegrationSettings.ChanceCubes behaviours = IntegrationSettings.ChanceCubes.INSTANCE;
            behaviours.enabled =
                    builder.comment("Should chance cube sheep cause trouble")
                    .define("enabled", true);
            behaviours.distance =
                    builder.comment("The distance a sheep can affect players")
                            .defineInRange("distance", 500, 0, 10000);
        }
        builder.pop();

    }
}
