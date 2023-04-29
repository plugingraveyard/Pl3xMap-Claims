package net.pl3x.map.claims.hook.worldguard;

import java.util.Collection;
import libs.org.checkerframework.checker.nullness.qual.NonNull;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;

public class WGLayer extends WorldLayer {
    public static final String KEY = "worldguard";

    private final WGHook wgHook;

    public WGLayer(@NonNull WGHook wgHook, @NonNull World world) {
        super(KEY, world, () -> WGConfig.LAYER_LABEL);
        this.wgHook = wgHook;

        setShowControls(WGConfig.LAYER_SHOW_CONTROLS);
        setDefaultHidden(WGConfig.LAYER_DEFAULT_HIDDEN);
        setUpdateInterval(WGConfig.LAYER_UPDATE_INTERVAL);
        setPriority(WGConfig.LAYER_PRIORITY);
        setZIndex(WGConfig.LAYER_ZINDEX);
    }

    @Override
    public @NonNull Collection<@NonNull Marker<@NonNull ?>> getMarkers() {
        return this.wgHook.getClaims(getWorld());
    }
}
