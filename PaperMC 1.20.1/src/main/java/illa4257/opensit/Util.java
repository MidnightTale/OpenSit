package illa4257.opensit;

import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.xml.stream.Location;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Util {

    private static Boolean IS_FOLIA = null;

    private static boolean tryFolia() {
        try {
            Bukkit.getAsyncScheduler();
            return true;
        } catch (Throwable ignored) {
        }
        return false;
    }

    public static Boolean isFolia() {
        if (IS_FOLIA == null) IS_FOLIA = tryFolia();
        return IS_FOLIA;
    }
    public static void runAsyncSchedulerDelay(Plugin plugin, Consumer<Player> playerTask, int initialDelayTicks) {
        if (isFolia()) {
            Bukkit.getAsyncScheduler().runDelayed(plugin, task -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    playerTask.accept(player);
                }
            }, initialDelayTicks, TimeUnit.SECONDS);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    playerTask.accept(player);
                }
            }, initialDelayTicks * 20L);
        }
    }
    public static void runAsyncSchedulerNow(Plugin plugin, Consumer<Player> playerTask) {
        if (isFolia()) {
            Bukkit.getAsyncScheduler().runNow(plugin, task -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    playerTask.accept(player);
                }
            });
        } else {
            Bukkit.getScheduler().runTask(plugin, () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    playerTask.accept(player);
                }
            });
        }
    }
    public static void runTaskForEntity(Entity entity, Plugin plugin, Runnable entityTask) {
        if (isFolia()) {
            entity.getScheduler().run(plugin, task -> entityTask.run(), null);
        } else {
            Bukkit.getScheduler().runTask(plugin, entityTask);
        }
    }
    public static void runGlobal(Plugin plugin, Runnable task, long initialDelayTicks) {
        if (isFolia()) {
            GlobalRegionScheduler globalRegionScheduler = Bukkit.getGlobalRegionScheduler();
            Consumer<ScheduledTask> taskWrapper = scheduledTask -> task.run();
            globalRegionScheduler.runDelayed(plugin, taskWrapper, initialDelayTicks);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, initialDelayTicks);
        }
    }
}