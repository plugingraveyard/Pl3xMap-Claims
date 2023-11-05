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

import java.util.Collection;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

public class WorldGuardLayer extends WorldLayer {

    public static final String KEY = "worldguard";

    private final WorldGuardHook worldGuardHook;

    public WorldGuardLayer(@NotNull WorldGuardHook worldGuardHook, @NotNull World world) {
        super(KEY, world, () -> WorldGuardConfig.LAYER_LABEL);
        this.worldGuardHook = worldGuardHook;

        setShowControls(WorldGuardConfig.LAYER_SHOW_CONTROLS);
        setDefaultHidden(WorldGuardConfig.LAYER_DEFAULT_HIDDEN);
        setUpdateInterval(WorldGuardConfig.LAYER_UPDATE_INTERVAL);
        setPriority(WorldGuardConfig.LAYER_PRIORITY);
        setZIndex(WorldGuardConfig.LAYER_ZINDEX);
    }

    @Override
    public @NotNull Collection<Marker<?>> getMarkers() {
        return this.worldGuardHook.getClaims(getWorld());
    }
}