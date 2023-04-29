package net.pl3x.map.claims.hook.griefprevention;

import java.util.Collection;
import libs.org.checkerframework.checker.nullness.qual.NonNull;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;

public class GPLayer extends WorldLayer {
    public static final String KEY = "griefprevention";

    private final GPHook gpHook;

    public GPLayer(@NonNull GPHook gpHook, @NonNull World world) {
        super(KEY, world, () -> GPConfig.LAYER_LABEL);
        this.gpHook = gpHook;

        setShowControls(GPConfig.LAYER_SHOW_CONTROLS);
        setDefaultHidden(GPConfig.LAYER_DEFAULT_HIDDEN);
        setUpdateInterval(GPConfig.LAYER_UPDATE_INTERVAL);
        setPriority(GPConfig.LAYER_PRIORITY);
        setZIndex(GPConfig.LAYER_ZINDEX);
    }

    @Override
    public @NonNull Collection<@NonNull Marker<@NonNull ?>> getMarkers() {
        return this.gpHook.getClaims(getWorld());
    }
}
