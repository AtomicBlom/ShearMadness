package com.github.atomicblom.shearmadness.configuration;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.variations.IntegrationSettingsDefinition;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class ConfigurationHandler {
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static void build() {

        CLIENT_BUILDER.comment("Debugging tools").push("Debugging");
        {
            Settings debugging = Settings.INSTANCE;
            debugging.debugModels =
                    CLIENT_BUILDER.comment(Reference.Configuration.DEBUG_MODELS)
                            .define("debugModels", false);
            debugging.debugInvisibleBlocks =
                    CLIENT_BUILDER.comment(Reference.Configuration.DEBUG_INVISIBLE_BLOCKS)
                            .define("debugInvisibleBlocks", false);
        }
        CLIENT_BUILDER.pop();

        COMMON_BUILDER.comment("Shearing").push("shearing");
        {
            Settings.Shearing shearing = Settings.Shearing.INSTANCE;
            shearing.shearBehaviour =
                    COMMON_BUILDER.comment(Reference.Configuration.BEHAVIOUR_COMMENT)
                    .defineEnum("behaviour", ShearBehaviour.RevertSheep);
        }
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Behaviours").push("behaviours");
        {
            Settings.Behaviours behaviours = Settings.Behaviours.INSTANCE;

            behaviours.breedingBehaviour =
                    COMMON_BUILDER.comment(Reference.Configuration.ALLOW_AUTO_CRAFTING)
                            .defineEnum("breedingBehaviour", BreedingBehaviour.Unchiseled);
        }
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Integration with other mods").push("integrations");
        IntegrationSettingsDefinition.buildCommonConfig(COMMON_BUILDER);
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }
}
