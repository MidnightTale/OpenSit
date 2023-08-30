package illa4257.opensit;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class OpenSit extends JavaPlugin {
    public static OpenSit instance;
    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new OpenSitListener(), this);
        getCommand("sit").setExecutor(new SitCommand());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}
