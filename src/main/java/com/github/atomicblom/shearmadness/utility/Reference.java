package com.github.atomicblom.shearmadness.utility;

import com.github.atomicblom.shearmadness.api.CommonReference;
import net.minecraft.util.ResourceLocation;

public final class Reference {
    public static final ResourceLocation CHANNEL_NAME = resource("shear_madness");
    public static final ResourceLocation NOT_A_CHISEL_CONTAINER_TYPE = resource("not_a_chisel_container_type");

    private Reference() {}

    public static class Configuration {
        public static final String BEHAVIOUR_COMMENT = "Sets the behaviour when a sheep is sheared\n" +
                "* RevertSheep - will change the sheep back to a normal sheep (default).\n" +
                "* ChiselFarm  - will allow the sheep to produce chiseled blocks (warning, this currently allows duping ores).\n" +
                "* CannotShear - You cannot shear the sheep while chiseled.\n";
        public static final String ALLOW_REDSTONE_COMMENT = "Chiseled redstone sheep will trigger Redstone circuits.";
        public static final String ALLOW_BOOKSHELF_COMMENT = "Chiseled bookshelf sheep will affect enchanting tables.";
        public static final String ALLOW_GLOWSTONE_COMMENT = "Chiseled glowstone sheep will light up the area around them.\n" +
                "WARNING: testing shows this creates a lot of chunk recalculation. I do not recommend this option.";
        public static final String ALLOW_CACTUS_COMMENT = "Chiseled Cactus sheep will deal damage players and destroy items.";
        public static final String ALLOW_TNT_COMMENT = "Chiseled TNT Sheep will be explode if exposed to active redstone.";
        public static final String ALLOW_FIRE_DAMAGE_COMMENT = "Chiseled Magma sheep will deal fire damage when touched.";
        public static final String DEBUG_MODELS = "Models will be regenerated every frame drawn.";
        public static final String DEBUG_INVISIBLE_BLOCKS = "Invisible Blocks will be shown in-game.";
        public static final String ALLOW_AUTO_CRAFTING = "Crafting Table sheep will use their recipe to consume and produce items.";
        public static final String BREEDING_BEHAVIOUR = "Sets the behaviour of when two sheep breed\n" +
                "* Unchiseled     - Baby sheep will be born without the parent's block (default)\n" +
                "* SimpleBreeding - Baby sheep will have a 50% chance of being chiseled the same as one parent or the other";

        private Configuration() {}
    }

    private static ResourceLocation resource(final String resourceName) {
        return new ResourceLocation(CommonReference.MOD_ID, resourceName);
    }

    public static class Items {
        public static final ResourceLocation NOT_A_CHISEL = resource("not_a_chisel");
        public static final String NOT_A_CHISEL_GUI_NAME = "gui.not_a_chisel";

        private Items() {}
    }

    public static final class Blocks {
        public static final String NORMAL_VARIANT = "normal";

        public static final ResourceLocation InvisibleRedstone = resource("invisible_redstone");
        public static final ResourceLocation InvisibleGlowstone = resource("invisible_glowstone");
        public static final ResourceLocation InvisibleBookshelf = resource("invisible_bookshelf");

        private Blocks() {}
    }

    public static final class TileEntities {

        private TileEntities() {}
    }

    public static class Sounds {
        public static final ResourceLocation SheepChiseled = resource("sheepchiseled");

        private Sounds() {}
    }

    public static class Textures {
        //Not a Shear Madness texture
        public static final ResourceLocation SHEARED_SHEEP_TEXTURE = new ResourceLocation("minecraft", "textures/entity/sheep/sheep.png");
        public static final ResourceLocation SHEEP_WOOL_TEXTURE = new ResourceLocation("minecraft", "textures/entity/sheep/sheep_fur.png");
        public static final ResourceLocation BAD_RENDER = resource("textures/bad_render.png");
        public static final ResourceLocation NOT_A_CHISEL_GUI_BACKGROUND = resource("textures/gui/not_a_chisel_bg.png");

        private Textures() {}
    }

    public static class Capability {
        public static final ResourceLocation CHISELED_SHEEP = resource("chiseled_sheep");

        private Capability() {}
    }

    public static class Gui {
        public static final String NOT_A_CHISEL_TITLE = "gui.not_a_chisel";

        private Gui() {}
    }
}
