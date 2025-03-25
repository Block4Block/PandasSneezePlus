package hasjamon.block4block;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Panda;
import org.bukkit.plugin.java.JavaPlugin;

public class PandasSneezePlus extends JavaPlugin {

    private static PandasSneezePlus instance;
    private int babySneezeInterval;
    private double adultSneezeChance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadConfigValues();

        // Register event listener
        Bukkit.getPluginManager().registerEvents(new PandasSneezeListener(this), this);

        // Schedule tasks for existing pandas
        scheduleExistingPandas();

        getLogger().info("PandasSneezePlus has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PandasSneezePlus has been disabled!");
    }

    public static PandasSneezePlus getInstance() {
        return instance;
    }

    private double babySneezeChance;

    private double slimeballDropChance;
    private int slimeballMinDrop;
    private int slimeballMaxDrop;

    public void loadConfigValues() {
        FileConfiguration config = getConfig();
        babySneezeInterval = config.getInt("baby-sneeze-interval", 10);
        babySneezeChance = config.getDouble("baby-sneeze-chance", 1.0);
        adultSneezeChance = config.getDouble("adult-sneeze-chance", 0.05);
        slimeballDropChance = config.getDouble("slimeball-drop-chance", 0.142857); // 1/7 chance
        slimeballMinDrop = config.getInt("slimeball-min-drop", 1);
        slimeballMaxDrop = config.getInt("slimeball-max-drop", 3);
    }

    public double getSlimeballDropChance() {
        return slimeballDropChance;
    }

    public int getSlimeballMinDrop() {
        return slimeballMinDrop;
    }

    public int getSlimeballMaxDrop() {
        return slimeballMaxDrop;
    }

    public double getBabySneezeChance() {
        return babySneezeChance;
    }

    public int getBabySneezeInterval() {
        return babySneezeInterval;
    }

    public double getAdultSneezeChance() {
        return adultSneezeChance;
    }

    // Schedule tasks for existing pandas already in the world
    public void scheduleExistingPandas() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            Bukkit.getWorlds().forEach(world -> {
                world.getEntities().stream()
                        .filter(entity -> entity instanceof Panda)
                        .forEach(entity -> {
                            Panda panda = (Panda) entity;
                            new PandasSneezeListener(this).scheduleSneezeTask(panda);
                        });
            });
        }, 20L); // Delay by 1 second to allow entities to load
    }
}
