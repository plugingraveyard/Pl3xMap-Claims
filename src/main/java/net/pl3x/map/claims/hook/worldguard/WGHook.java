package net.pl3x.map.claims.hook.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.util.profile.cache.ProfileCache;
import java.util.Collection;
import java.util.stream.Collectors;
import libs.org.checkerframework.checker.nullness.qual.NonNull;
import net.pl3x.map.claims.hook.Hook;
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
                .map(claim -> {
                    String key = "wg-claim-" + claim.getID();
                    return Marker.rectangle(key, claim.getMin(), claim.getMax())
                            .setOptions(getOptions(claim));
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
        ProfileCache pc = WorldGuard.getInstance().getProfileCache();
        return popup.replace("<world>", claim.getWorld().getName())
                .replace("<id>", claim.getID())
                .replace("<owner>", claim.getOwners().toPlayersString())
                .replace("<regionname>", claim.getID())
                .replace("<playerowners>", claim.getOwners().toPlayersString(pc))
                .replace("<groupowners>", claim.getOwners().toGroupsString())
                .replace("<playermembers>", claim.getMembers().toPlayersString(pc))
                .replace("<groupmembers>", claim.getMembers().toGroupsString())
                .replace("<parent>", claim.getParent() == null ? "" : claim.getParent().getId())
                .replace("<priority>", String.valueOf(claim.getPriority()))
                .replace("<flags>", claim.getFlagsString());
    }
}
