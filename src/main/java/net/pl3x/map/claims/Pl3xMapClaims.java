package net.pl3x.map.claims;

import java.util.Arrays;
import net.pl3x.map.claims.hook.Hook;
import net.pl3x.map.claims.listener.Pl3xMapListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Pl3xMapClaims extends JavaPlugin {
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

        pm.registerEvents(new Pl3xMapListener(), this);
    }

    @Override
    public void onDisable() {
        Hook.clear();
    }
}
