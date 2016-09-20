package com.github.atomicblom.shearmadness.library;

public enum GuiLibrary {
    CONFIGURE_BREEDING;

    private static final GuiLibrary[] cache = values();

    public int getID()
    {
        // Not used for persistent data, so ordinal is perfect here!
        return ordinal();
    }

    public static GuiLibrary fromId(int id)
    {
        return cache[id];
    }
}
