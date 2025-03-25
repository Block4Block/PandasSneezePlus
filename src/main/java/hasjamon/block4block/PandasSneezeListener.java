package hasjamon.block4block;

import org.bukkit.Sound;
import org.bukkit.entity.Panda;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PandasSneezeListener implements Listener {

    private final PandasSneezePlus plugin;
    private final Random random = new Random();

    public PandasSneezeListener(PandasSneezePlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPandaSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Panda) {
            Panda panda = (Panda) event.getEntity();
            scheduleSneezeTask(panda);
        }
    }

    public void scheduleSneezeTask(Panda panda) {
        int interval = plugin.getBabySneezeInterval();
        double babyChance = plugin.getBabySneezeChance();
        double adultChance = plugin.getAdultSneezeChance();
        double slimeballDropChance = plugin.getSlimeballDropChance();
        int minDrop = plugin.getSlimeballMinDrop();
        int maxDrop = plugin.getSlimeballMaxDrop();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!panda.isDead()) {
                    boolean sneezed = false;

                    // Check if the panda sneezes
                    if (panda.getAge() < 0) {
                        if (random.nextDouble() < babyChance) {
                            panda.getWorld().playSound(panda.getLocation(), Sound.ENTITY_PANDA_SNEEZE, 1.0f, 1.0f);
                            sneezed = true;
                        }
                    } else {
                        if (random.nextDouble() < adultChance) {
                            panda.getWorld().playSound(panda.getLocation(), Sound.ENTITY_PANDA_SNEEZE, 1.0f, 1.0f);
                            sneezed = true;
                        }
                    }

                    // If a panda sneezes, roll for slimeball drop
                    if (sneezed && random.nextDouble() < slimeballDropChance) {
                        int slimeballAmount = random.nextInt(maxDrop - minDrop + 1) + minDrop;
                        dropSlimeball(panda, slimeballAmount);
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20L, interval * 20L); // Interval in seconds
    }

    // Drop slimeballs at the panda's location
    private void dropSlimeball(Panda panda, int amount) {
        ItemStack slimeball = new ItemStack(Material.SLIME_BALL, amount);
        panda.getWorld().dropItemNaturally(panda.getLocation(), slimeball);
    }


}
