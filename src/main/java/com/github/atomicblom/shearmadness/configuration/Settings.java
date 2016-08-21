package com.github.atomicblom.shearmadness.configuration;

import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraftforge.common.config.Configuration;

@SuppressWarnings("InnerClassFieldHidesOuterClassField")
public enum Settings
{
    INSTANCE;

    public static void syncConfig(Configuration config)
    {
        Shearing.syncConfig(config);
    }

    public enum Shearing
    {
        INSTANCE;

        public static final String CATEGORY = Configuration.CATEGORY_GENERAL + ".shearing";

        @SuppressWarnings("StaticNonFinalField")
        private static ShearBehaviour behaviour = ShearBehaviour.RevertSheep;

        public static ShearBehaviour getBehaviour() { return behaviour; }

        private static void syncConfig(Configuration config) {

            final String[] validNames = new String[ShearBehaviour.values().length];
            for (int i = 0; i < ShearBehaviour.values().length; i++)
            {
                validNames[i] = ShearBehaviour.values()[i].name();
            }

            final String behaviour = config.getString("behaviour", CATEGORY, "RevertSheep", Reference.BEHAVIOUR_COMMENT, validNames);

            Shearing.behaviour = ShearBehaviour.valueOf(behaviour);
        }
    }

}
