package net.pl3x.map.claims.listener;

import libs.org.checkerframework.checker.nullness.qual.NonNull;
import net.pl3x.map.claims.hook.Hook;
import net.pl3x.map.claims.hook.griefprevention.GPLayer;
import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.event.EventHandler;
import net.pl3x.map.core.event.EventListener;
import net.pl3x.map.core.event.server.ServerLoadedEvent;
import net.pl3x.map.core.event.world.WorldLoadedEvent;
import net.pl3x.map.core.event.world.WorldUnloadedEvent;
import net.pl3x.map.core.world.World;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class Pl3xMapListener implements EventListener, Listener {
    public Pl3xMapListener() {
        Pl3xMap.api().getEventRegistry().register(this);
    }

    @org.bukkit.event.EventHandler
    public void onPluginEnabled(PluginEnableEvent event) {
        Hook.add(event.getPlugin().getName());
    }

    @org.bukkit.event.EventHandler
    public void onPluginDisabled(PluginDisableEvent event) {
        Hook.remove(event.getPlugin().getName());
    }

    @EventHandler
    public void onServerLoaded(@NonNull ServerLoadedEvent event) {
        Pl3xMap.api().getWorldRegistry().forEach(this::registerWorld);
    }

    @EventHandler
    public void onWorldLoaded(@NonNull WorldLoadedEvent event) {
        registerWorld(event.getWorld());
    }

    @EventHandler
    public void onWorldUnloaded(@NonNull WorldUnloadedEvent event) {
        event.getWorld().getLayerRegistry().unregister(GPLayer.KEY);
    }

    private void registerWorld(@NonNull World world) {
        Hook.hooks().forEach(hook -> hook.registerWorld(world));
    }
}
