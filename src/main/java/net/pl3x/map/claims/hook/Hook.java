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
package net.pl3x.map.claims.hook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import libs.org.checkerframework.checker.nullness.qual.NonNull;
import libs.org.checkerframework.checker.nullness.qual.Nullable;
import net.pl3x.map.claims.hook.griefprevention.GPHook;
import net.pl3x.map.claims.hook.worldguard.WGHook;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;

public interface Hook {
    Collection<@NonNull Marker<@NonNull ?>> EMPTY_LIST = new ArrayList<>();
    Map<@NonNull String, @NonNull Hook> HOOKS = new HashMap<>();

    static @NonNull Collection<@NonNull Hook> hooks() {
        return HOOKS.values();
    }

    static void add(@NonNull String name) {
        add(Impl.get(name));
    }

    static void add(@Nullable Impl impl) {
        if (impl != null) {
            HOOKS.put(impl.name, impl.hook.get());
        }
    }

    static void remove(@NonNull String name) {
        HOOKS.remove(name);
    }

    static void clear() {
        HOOKS.clear();
    }

    void registerWorld(@NonNull World world);

    @NonNull Collection<@NonNull Marker<@NonNull ?>> getClaims(@NonNull World world);

    enum Impl {
        GRIEF_PREVENTION("GriefPrevention", GPHook::new),
        WORLD_GUARD("WorldGuard", WGHook::new);

        private final String name;
        private final Supplier<@NonNull Hook> hook;

        Impl(@NonNull String name, @NonNull Supplier<@NonNull Hook> hook) {
            this.name = name;
            this.hook = hook;
        }

        public String getPluginName() {
            return this.name;
        }

        static final Map<@NonNull String, @NonNull Impl> MAP = new HashMap<>();

        static {
            for (Impl impl : values()) {
                MAP.put(impl.name, impl);
            }
        }

        static @Nullable Impl get(@NonNull String name) {
            return MAP.get(name);
        }
    }
}
