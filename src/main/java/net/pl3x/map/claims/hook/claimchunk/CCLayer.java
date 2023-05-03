package net.pl3x.map.claims.hook.claimchunk;

import java.util.Collection;
import libs.org.checkerframework.checker.nullness.qual.NonNull;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;

public class CCLayer extends WorldLayer {
    public static final String KEY = "claimchunk";

    private final CCHook ccHook;

    public CCLayer(@NonNull CCHook ccHook, @NonNull World world) {
        super(KEY, world, () -> CCConfig.LAYER_LABEL);
        this.ccHook = ccHook;

        setShowControls(CCConfig.LAYER_SHOW_CONTROLS);
        setDefaultHidden(CCConfig.LAYER_DEFAULT_HIDDEN);
        setUpdateInterval(CCConfig.LAYER_UPDATE_INTERVAL);
        setPriority(CCConfig.LAYER_PRIORITY);
        setZIndex(CCConfig.LAYER_ZINDEX);
    }

    @Override
    public @NonNull Collection<@NonNull Marker<@NonNull ?>> getMarkers() {
        return this.ccHook.getClaims(getWorld());
    }
}
