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
