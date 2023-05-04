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
package net.pl3x.map.claims.hook.claimchunk;

import java.nio.file.Path;
import net.pl3x.map.claims.Pl3xMapClaims;
import net.pl3x.map.core.configuration.AbstractConfig;

@SuppressWarnings("CanBeFinal")
public final class ClaimChunkConfig extends AbstractConfig {
    @Key("settings.layer.label")
    @Comment("Label for map layer")
    public static String LAYER_LABEL = "ClaimChunk";
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

    @Key("settings.claim.show-chunks")
    @Comment("Show all the chunks in each claim.")
    public static boolean SHOW_CHUNKS = true;
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
    @Comment("Popup for basic claims")
    public static String MARKER_POPUP = """
            Claim Owner: <span style="font-weight:bold;"><owner></span>""";

    private static final ClaimChunkConfig CONFIG = new ClaimChunkConfig();

    public static void reload() {
        Path mainDir = Pl3xMapClaims.getPlugin(Pl3xMapClaims.class).getDataFolder().toPath();
        CONFIG.reload(mainDir.resolve("claimchunk.yml"), ClaimChunkConfig.class);
    }
}
