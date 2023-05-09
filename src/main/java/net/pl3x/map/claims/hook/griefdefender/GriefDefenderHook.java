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
package net.pl3x.map.claims.hook.griefdefender;

import com.griefdefender.api.GriefDefender;
import java.util.Collection;
import java.util.stream.Collectors;
import libs.org.checkerframework.checker.nullness.qual.NonNull;
import net.pl3x.map.claims.hook.Hook;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.util.Colors;
import net.pl3x.map.core.world.World;
import org.bukkit.event.Listener;

public class GriefDefenderHook implements Listener, Hook {
    public GriefDefenderHook() {
        GriefDefenderConfig.reload();
    }

    @Override
    public void registerWorld(@NonNull World world) {
        world.getLayerRegistry().register(new GriefDefenderLayer(this, world));
    }

    @Override
    public void unloadWorld(@NonNull World world) {
        world.getLayerRegistry().unregister(GriefDefenderLayer.KEY);
    }

    @Override
    public @NonNull Collection<@NonNull Marker<@NonNull ?>> getClaims(@NonNull World world) {
        return GriefDefender.getCore().getAllClaims().stream()
                .filter(claim -> claim.getWorldName().equals(world.getName()))
                .map(claim -> new GriefDefenderClaim(world, claim))
                .map(claim -> {
                    String key = "gd-claim-" + claim.getID();
                    return Marker.rectangle(key, claim.getMin(), claim.getMax())
                            .setOptions(getOptions(claim));
                })
                .collect(Collectors.toSet());
    }

    private @NonNull Options getOptions(@NonNull GriefDefenderClaim claim) {
        Options.Builder builder;
        if (claim.isAdminClaim()) {
            builder = Options.builder()
                    .strokeWeight(GriefDefenderConfig.MARKER_ADMIN_STROKE_WEIGHT)
                    .strokeColor(Colors.fromHex(GriefDefenderConfig.MARKER_ADMIN_STROKE_COLOR))
                    .fillColor(Colors.fromHex(GriefDefenderConfig.MARKER_ADMIN_FILL_COLOR))
                    .popupContent(processPopup(GriefDefenderConfig.MARKER_ADMIN_POPUP, claim));
        } else {
            builder = Options.builder()
                    .strokeWeight(GriefDefenderConfig.MARKER_BASIC_STROKE_WEIGHT)
                    .strokeColor(Colors.fromHex(GriefDefenderConfig.MARKER_BASIC_STROKE_COLOR))
                    .fillColor(Colors.fromHex(GriefDefenderConfig.MARKER_BASIC_FILL_COLOR))
                    .popupContent(processPopup(GriefDefenderConfig.MARKER_BASIC_POPUP, claim));
        }
        return builder.build();
    }

    private @NonNull String processPopup(@NonNull String popup, @NonNull GriefDefenderClaim claim) {
        return popup.replace("<world>", claim.getWorld().getName())
                .replace("<id>", claim.getID().toString())
                .replace("<owner>", claim.getOwnerName())
                .replace("<area>", Integer.toString(claim.getArea()))
                .replace("<width>", Integer.toString(claim.getWidth()))
                .replace("<height>", Integer.toString(claim.getHeight()));
    }
}
