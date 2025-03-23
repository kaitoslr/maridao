package HGclasses;
import io.pluginteste.maridao.Maridao;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.List;

public class Classes implements Listener {

    @EventHandler
    public void useBoots(EntityDamageEvent event){
        if (event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            ItemStack boots = player.getInventory().getBoots();

            if (event.getCause() == EntityDamageEvent.DamageCause.FALL && boots.getType() == Material.LEATHER_BOOTS && boots.getItemMeta().getDisplayName().equals("Stomper boots")) {
                double damage = event.getDamage();
                event.setCancelled(true);
                World world = player.getWorld();

                List<Entity> nearbyEntities = player.getNearbyEntities(1.5,1.5,1.5);

                for(Entity entity : nearbyEntities){
                    if(entity instanceof LivingEntity){
                        entity.setVelocity(new Vector(0,damage,0).normalize());
                        player.spawnParticle(Particle.ANGRY_VILLAGER, player.getLocation(), 1);
                        world.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 2.0f, 1.0f);
                        Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(Maridao.class) , ()->{((LivingEntity) entity).damage(damage);}, 15);
                    }
                }
            }
        }

    }

    public void useKangaroo(EntityDamageEvent event){
        if (event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();

            player.getCurrentInput().equals()
        }
    }

    public static ItemStack getBoots(){
        ItemStack stomperBoots = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta meta = stomperBoots.getItemMeta();

        meta.setDisplayName("Stomper boots");
        stomperBoots.setItemMeta(meta);
        return stomperBoots;
    }

    public static ItemStack getKangaroo(){
        ItemStack kangaroo = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta meta = kangaroo.getItemMeta();

        meta.setDisplayName("Kangaroo");
        kangaroo.setItemMeta(meta);
        return kangaroo;
    }
}
