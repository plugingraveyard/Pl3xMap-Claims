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
        try {
            event.getWorld().getLayerRegistry().unregister(GPLayer.KEY);
        } catch (Throwable ignore) {
        }
    }

    private void registerWorld(@NonNull World world) {
        Hook.hooks().forEach(hook -> hook.registerWorld(world));
    }
}
