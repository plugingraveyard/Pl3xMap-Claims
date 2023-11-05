/*
 * MIT License
 *
 * Copyright (c) 2020-2023 William Blake Galbreath
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ryderbelserion.map.claims.hook.worldguard;

import java.nio.file.Path;
import com.ryderbelserion.map.claims.Pl3xMapClaims;
import net.pl3x.map.core.configuration.AbstractConfig;

@SuppressWarnings("CanBeFinal")
public final class WorldGuardConfig extends AbstractConfig {

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
    public static int LAYER_UPDATE_INTERVAL = 30;
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
    @Key("settings.claim.popup.tooltip")
    @Comment("Popup for claims")
    public static String MARKER_POPUP = """
            <span style="font-weight:bold;"><regionname></span><br/>
            <owners><members><flags>""";

    @Key("settings.claim.popup.owners")
    @Comment("Popup text for owners if present")
    public static String MARKER_POPUP_OWNERS = """
            Owners: <span style="font-weight:bold;"><owners></span><br/>""";
    @Key("settings.claim.popup.members")
    @Comment("Popup text for members if present")
    public static String MARKER_POPUP_MEMBERS = """
            Members: <span style="font-weight:bold;"><members></span><br/>""";
    @Key("settings.claim.popup.flags")
    @Comment("Popup text for flags if present")
    public static String MARKER_POPUP_FLAGS = """
            Flags:<br/><span style="display:inline-block;margin-left:10px"><flags></span><br/>""";
    @Key("settings.claim.popup.flag-entry")
    @Comment("How each flag entry should look")
    public static String MARKER_POPUP_FLAGS_ENTRY = """
            <span style="font-weight:bold;"><flag></span>: <value>""";

    private static final WorldGuardConfig CONFIG = new WorldGuardConfig();

    public static void reload() {
        Path mainDir = Pl3xMapClaims.getPlugin(Pl3xMapClaims.class).getDataFolder().toPath();
        CONFIG.reload(mainDir.resolve("worldguard.yml"), WorldGuardConfig.class);
    }
}