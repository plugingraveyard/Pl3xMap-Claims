package net.pl3x.map.claims;

import java.util.Arrays;
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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Pl3xMapClaims extends JavaPlugin implements EventListener, Listener {
    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();

        if (!pm.isPluginEnabled("Pl3xMap")) {
            getLogger().severe("Pl3xMap not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Arrays.stream(Hook.Impl.values()).forEach(impl -> {
            if (pm.isPluginEnabled(impl.getPluginName())) {
                Hook.add(impl);
            }
        });

        Pl3xMap.api().getEventRegistry().register(this);
        pm.registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        Hook.clear();
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
