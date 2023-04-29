package net.pl3x.map.claims.hook.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import libs.org.checkerframework.checker.nullness.qual.NonNull;
import net.pl3x.map.claims.hook.Hook;
import net.pl3x.map.core.markers.Point;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.util.Colors;
import net.pl3x.map.core.world.World;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.Nullable;

public class WGHook implements Hook {
    public WGHook() {
        WGConfig.reload();
    }

    private @Nullable RegionManager getRegionManager(@NonNull World world) {
        org.bukkit.World bukkit = Bukkit.getWorld(world.getName());
        return bukkit == null ? null : WorldGuard.getInstance().getPlatform()
                .getRegionContainer().get(BukkitAdapter.adapt(bukkit));
    }

    @Override
    public void registerWorld(@NonNull World world) {
        if (getRegionManager(world) != null) {
            world.getLayerRegistry().register(WGLayer.KEY, new WGLayer(this, world));
        }
    }

    @Override
    public @NonNull Collection<@NonNull Marker<@NonNull ?>> getClaims(@NonNull World world) {
        RegionManager manager = getRegionManager(world);
        if (manager == null) {
            return EMPTY_LIST;
        }
        return manager.getRegions().values().stream()
                .map(region -> new WGClaim(world, region))
                .filter(claim -> claim.getType() == RegionType.CUBOID || claim.getType() == RegionType.POLYGON)
                .map(claim -> {
                    String key = "wg-claim-" + claim.getID();
                    Marker<?> marker;
                    if (claim.getType() == RegionType.POLYGON) {
                        marker = Marker.polygon(key, Marker.polyline(key + "line",
                                claim.getPoints().stream().map(point ->
                                        Point.of(point.getX(), point.getZ())).toList()));
                    } else {
                        marker = Marker.rectangle(key, claim.getMin(), claim.getMax());
                    }
                    return marker.setOptions(getOptions(claim));
                })
                .collect(Collectors.toSet());
    }

    private @NonNull Options getOptions(@NonNull WGClaim claim) {
        return Options.builder()
                .strokeWeight(WGConfig.MARKER_STROKE_WEIGHT)
                .strokeColor(Colors.fromHex(WGConfig.MARKER_STROKE_COLOR))
                .fillColor(Colors.fromHex(WGConfig.MARKER_FILL_COLOR))
                .popupContent(processPopup(WGConfig.MARKER_POPUP, claim))
                .build();
    }

    private @NonNull String processPopup(@NonNull String popup, @NonNull WGClaim claim) {
        return popup.replace("<world>", claim.getWorld().getName())
                .replace("<regionname>", claim.getID())
                .replace("<owners>", getOwners(claim))
                .replace("<members>", getMembers(claim))
                .replace("<parent>", claim.getParent() == null ? "" : claim.getParent().getId())
                .replace("<priority>", String.valueOf(claim.getPriority()))
                .replace("<flags>", getFlags(claim));
    }

    private String getOwners(WGClaim claim) {
        Set<String> set = new HashSet<>();
        set.addAll(claim.getOwners().getPlayers());
        set.addAll(claim.getOwners().getGroups());
        return set.isEmpty() ? "" : WGConfig.MARKER_POPUP_OWNERS
                .replace("<owners>", String.join(", ", set));
    }

    private String getMembers(WGClaim claim) {
        Set<String> set = new HashSet<>();
        set.addAll(claim.getMembers().getPlayers());
        set.addAll(claim.getMembers().getGroups());
        return set.isEmpty() ? "" : WGConfig.MARKER_POPUP_MEMBERS
                .replace("<members>", String.join(", ", set));
    }

    private String getFlags(WGClaim claim) {
        Map<Flag<?>, Object> flags = claim.getFlags();
        Set<String> set = flags.keySet().stream()
                .map(flag -> WGConfig.MARKER_POPUP_FLAGS_ENTRY
                        .replace("<flag>", flag.getName())
                        .replace("<value>", String.valueOf(flags.get(flag))))
                .collect(Collectors.toSet());
        return set.isEmpty() ? "" : WGConfig.MARKER_POPUP_FLAGS
                .replace("<flags>", String.join("<br/>", set));
    }
}
