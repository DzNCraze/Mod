package Sonicjumper.EnhancedVisuals.src;

public class Sounds {
    private static final String SOUND_RESOURCE_LOCATION = "EnhancedVisuals/DefaultTheme:";//TODO
    private static final String SOUND_PREFIX = "EnhancedVisuals/DefaultTheme:";//TODO

    public static String[] soundFiles = {SOUND_RESOURCE_LOCATION + "heartbeatIn.ogg", SOUND_RESOURCE_LOCATION + "heartbeatOut.ogg", SOUND_RESOURCE_LOCATION + "explosionRing.ogg"};

    public static final String HEARTBEAT_IN = SOUND_PREFIX + "heartbeatIn";
    public static final String HEARTBEAT_OUT = SOUND_PREFIX + "heartbeatOut";
    public static final String EXPLOSION_RING = SOUND_PREFIX + "explosionRing";
}