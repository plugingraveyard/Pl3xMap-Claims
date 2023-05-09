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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import libs.org.checkerframework.checker.nullness.qual.NonNull;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.pl3x.map.claims.hook.Hook;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.util.Colors;
import net.pl3x.map.core.world.World;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;

public class GriefPreventionHook implements Listener, Hook {
    public GriefPreventionHook() {
        GriefPreventionConfig.reload();
    }

    private boolean isWorldEnabled(@NonNull String name) {
        return GriefPrevention.instance.claimsEnabledForWorld(Bukkit.getWorld(name));
    }

    @Override
    public void registerWorld(@NonNull World world) {
        if (isWorldEnabled(world.getName())) {
            world.getLayerRegistry().register(new GriefPreventionLayer(this, world));
        }
    }

    @Override
    public @NonNull Collection<@NonNull Marker<@NonNull ?>> getClaims(@NonNull World world) {
        if (!isWorldEnabled(world.getName())) {
            return EMPTY_LIST;
        }
        return GriefPrevention.instance.dataStore.getClaims().stream()
                .filter(claim -> claim.getLesserBoundaryCorner().getWorld().getName().equals(world.getName()))
                .map(claim -> new GriefPreventionClaim(world, claim))
                .map(claim -> {
                    String key = "gp-claim-" + claim.getID();
                    return Marker.rectangle(key, claim.getMin(), claim.getMax())
                            .setOptions(getOptions(claim));
                })
                .collect(Collectors.toSet());
    }

    private @NonNull Options getOptions(@NonNull GriefPreventionClaim claim) {
        Options.Builder builder;
        if (claim.isAdminClaim()) {
            builder = Options.builder()
                    .strokeWeight(GriefPreventionConfig.MARKER_ADMIN_STROKE_WEIGHT)
                    .strokeColor(Colors.fromHex(GriefPreventionConfig.MARKER_ADMIN_STROKE_COLOR))
                    .fillColor(Colors.fromHex(GriefPreventionConfig.MARKER_ADMIN_FILL_COLOR))
                    .popupContent(processPopup(GriefPreventionConfig.MARKER_ADMIN_POPUP, claim));
        } else {
            builder = Options.builder()
                    .strokeWeight(GriefPreventionConfig.MARKER_BASIC_STROKE_WEIGHT)
                    .strokeColor(Colors.fromHex(GriefPreventionConfig.MARKER_BASIC_STROKE_COLOR))
                    .fillColor(Colors.fromHex(GriefPreventionConfig.MARKER_BASIC_FILL_COLOR))
                    .popupContent(processPopup(GriefPreventionConfig.MARKER_BASIC_POPUP, claim));
        }
        return builder.build();
    }

    private @NonNull String processPopup(@NonNull String popup, @NonNull GriefPreventionClaim claim) {
        return popup.replace("<world>", claim.getWorld().getName())
                .replace("<id>", Long.toString(claim.getID()))
                .replace("<owner>", claim.getOwnerName())
                .replace("<trusts>", getTrusts(claim))
                .replace("<area>", Integer.toString(claim.getArea()))
                .replace("<width>", Integer.toString(claim.getWidth()))
                .replace("<height>", Integer.toString(claim.getHeight()));
    }

    private @NonNull String getTrusts(@NonNull GriefPreventionClaim claim) {
        ArrayList<String> builders = new ArrayList<>();
        ArrayList<String> containers = new ArrayList<>();
        ArrayList<String> accessors = new ArrayList<>();
        ArrayList<String> managers = new ArrayList<>();
        claim.getPermissions(builders, containers, accessors, managers);
        StringBuilder sb = new StringBuilder();
        if (!builders.isEmpty()) {
            if (sb.isEmpty()) sb.append("<hr/>");
            sb.append(GriefPreventionConfig.MARKER_POPUP_TRUST.replace("<builders>", getNames(builders)));
        }
        if (!containers.isEmpty()) {
            if (sb.isEmpty()) sb.append("<hr/>");
            sb.append(GriefPreventionConfig.MARKER_POPUP_CONTAINER.replace("<containers>", getNames(containers)));
        }
        if (!accessors.isEmpty()) {
            if (sb.isEmpty()) sb.append("<hr/>");
            sb.append(GriefPreventionConfig.MARKER_POPUP_ACCESS.replace("<accessors>", getNames(accessors)));
        }
        if (!managers.isEmpty()) {
            if (sb.isEmpty()) sb.append("<hr/>");
            sb.append(GriefPreventionConfig.MARKER_POPUP_PERMISSION.replace("<managers>", getNames(managers)));
        }
        return sb.toString();
    }

    private @NonNull String getNames(@NonNull List<@NonNull String> list) {
        List<String> names = new ArrayList<>();
        for (String str : list) {
            try {
                UUID uuid = UUID.fromString(str);
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                names.add(offlinePlayer.getName());
            } catch (Exception e) {
                names.add(str);
            }
        }
        return String.join(", ", names);
    }
}