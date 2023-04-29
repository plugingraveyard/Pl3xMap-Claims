package net.pl3x.map.claims.hook.worldguard;

import java.nio.file.Path;
import net.pl3x.map.claims.Pl3xMapClaims;
import net.pl3x.map.core.configuration.AbstractConfig;

@SuppressWarnings("CanBeFinal")
public final class WGConfig extends AbstractConfig {
    @Key("settings.layer.label")
    @Comment("Label for map layer")
    public static String LAYER_LABEL = "WorldGuard";
    @Key("settings.layer.show-controls")
    @Comment("Show controls for map layer")
    public static boolean LAYER_SHOW_CONTROLS = true;
    @Key("settings.layer.default-hidden")
    @Comment("Whether map layer is hidden by default")
    public static boolean LAYER_DEFAULT_HIDDEN = false;
    @Key("settings.layer.update-interval")
    @Comment("Update interval for map layer")
    public static int LAYER_UPDATE_INTERVAL = 300;
    @Key("settings.layer.priority")
    @Comment("Priority for map layer")
    public static int LAYER_PRIORITY = 10;
    @Key("settings.layer.z-index")
    @Comment("zIndex for map layer")
    public static int LAYER_ZINDEX = 10;

    @Key("settings.claim.stroke.color")
    @Comment("Stroke color (#AARRGGBB)")
    public static String MARKER_STROKE_COLOR = "#FF00FF00";
    @Key("settings.claim.stroke.weight")
    @Comment("Stroke weight")
    public static int MARKER_STROKE_WEIGHT = 3;
    @Key("settings.claim.fill.color")
    @Comment("Fill color (#AARRGGBB)")
    public static String MARKER_FILL_COLOR = "#3300FF00";
    @Key("settings.claim.popup")
    @Comment("Popup for claims")
    public static String MARKER_POPUP = """
            <span style="font-size:120%;"><regionname></span><br/>
            Owner <span style="font-weight:bold;"><playerowners></span><br/>
            Flags<br/><span style="font-weight:bold;"><flags></span>""";

    private static final WGConfig WG_CONFIG = new WGConfig();

    public static void reload() {
        Path mainDir = Pl3xMapClaims.getPlugin(Pl3xMapClaims.class).getDataFolder().toPath();
        WG_CONFIG.reload(mainDir.resolve("worldguard.yml"), WGConfig.class);
    }
}
