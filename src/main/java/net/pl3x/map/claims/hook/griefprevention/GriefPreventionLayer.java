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
package net.pl3x.map.claims.hook.griefprevention;

import java.util.Collection;
import libs.org.checkerframework.checker.nullness.qual.NonNull;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;

public class GriefPreventionLayer extends WorldLayer {
    public static final String KEY = "griefprevention";

    private final GriefPreventionHook griefPreventionHook;

    public GriefPreventionLayer(@NonNull GriefPreventionHook griefPreventionHook, @NonNull World world) {
        super(KEY, world, () -> GriefPreventionConfig.LAYER_LABEL);
        this.griefPreventionHook = griefPreventionHook;

        setShowControls(GriefPreventionConfig.LAYER_SHOW_CONTROLS);
        setDefaultHidden(GriefPreventionConfig.LAYER_DEFAULT_HIDDEN);
        setUpdateInterval(GriefPreventionConfig.LAYER_UPDATE_INTERVAL);
        setPriority(GriefPreventionConfig.LAYER_PRIORITY);
        setZIndex(GriefPreventionConfig.LAYER_ZINDEX);
    }

    @Override
    public @NonNull Collection<@NonNull Marker<@NonNull ?>> getMarkers() {
        return this.griefPreventionHook.getClaims(getWorld());
    }
}
