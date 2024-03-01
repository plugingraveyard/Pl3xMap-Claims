package com.ryderbelserion.map.claims.hook.factionsuuid;

import com.ryderbelserion.map.claims.hook.Hook;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;

public class FactionsUUIDHook implements Listener, Hook {


    private boolean isWorldEnabled(@NotNull String name) {

    }

    @Override
    public void registerWorld(@NotNull World world) {

    }

    @Override
    public void unloadWorld(@NotNull World world) {

    }

    @Override
    public @NotNull Collection<Marker<?>> getClaims(@NotNull World world) {
        return null;
    }
}