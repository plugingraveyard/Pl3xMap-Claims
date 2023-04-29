package net.pl3x.map.claims.hook;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import libs.org.checkerframework.checker.nullness.qual.NonNull;
import libs.org.checkerframework.checker.nullness.qual.Nullable;
import net.pl3x.map.claims.hook.griefprevention.GPHook;
import net.pl3x.map.core.world.World;

public interface Hook {
    Map<@NonNull String, @NonNull Hook> hooks = new HashMap<>();

    static @NonNull Collection<@NonNull Hook> hooks() {
        return hooks.values();
    }

    static void add(@NonNull String name) {
        add(Impl.get(name));
    }

    static void add(@Nullable Impl impl) {
        if (impl != null) {
            hooks.put(impl.name, impl.hook.get());
        }
    }

    static void remove(@NonNull String name) {
        hooks.remove(name);
    }

    static void clear() {
        hooks.clear();
    }

    void registerWorld(@NonNull World world);

    enum Impl {
        GRIEF_PREVENTION("GriefPrevention", GPHook::new);

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
