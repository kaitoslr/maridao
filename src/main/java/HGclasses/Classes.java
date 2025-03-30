package HGclasses;
import io.pluginteste.maridao.Maridao;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import draw.Particle.*;


public class Classes implements Listener {

    LocalDateTime expirationTime = null;

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

    @EventHandler
    public void useSuperSpeedBoots(PlayerMoveEvent event){
        Player player = event.getPlayer();
        ItemStack boots = player.getInventory().getBoots();

        if(boots == null){
            return;
        }

        if (boots.getType() == Material.LEATHER_BOOTS && boots.getItemMeta().getDisplayName().equals("Superspeed")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,60, 3));
        }
    }

    @EventHandler
    public void flyWithSuperElytra(EntityToggleGlideEvent event){
        Player player = (Player) event.getEntity();
        ItemStack elytra = player.getInventory().getChestplate();

        if(event.isGliding() && elytra.getItemMeta().getDisplayName().equals("Fly")){
            player.sendMessage("voando");
            Vector v = player.getLocation().getDirection();
            player.setVelocity(new Vector(v.getX() * 1.7, v.getY()*1.7, v.getZ()*1.7));
        }
    }

    @EventHandler
    public void useLaserEyes(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.BLAZE_ROD || !item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName() || !meta.getDisplayName().equals("Raio Lazer")) {
            return;
        }

        Player player = event.getPlayer();

        Vector direction = player.getEyeLocation().getDirection();

        RayTraceResult blockResult = player.getWorld().rayTraceBlocks(
                player.getEyeLocation(), direction, 100, FluidCollisionMode.NEVER, true);

        RayTraceResult entityResult = player.getWorld().rayTraceEntities(
                player.getEyeLocation(), direction, 100, (entity) -> entity instanceof LivingEntity && entity != player);

        if (entityResult != null && entityResult.getHitEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) entityResult.getHitEntity();
            entity.damage(10000);
            Entity ent = player.getTargetEntity(100);
            if(ent == null) return;
            draw.Particle.Line(player, player.getEyeLocation(), ent.getLocation(), Particle.CRIT, 1000);
            player.getWorld().spawnParticle(Particle.CRIT, ent.getLocation(), 10);
        }
    }

    @EventHandler
    public void useKangaroo(PlayerInteractEvent event){
        if(event.getAction().isRightClick() && event.getItem() != null){
            Player player = event.getPlayer();

            ItemStack kangaroo = player.getInventory().getItemInMainHand();

            LocalDateTime agora = LocalDateTime.now();

            if(!kangaroo.getItemMeta().getDisplayName().equals("Kangaroo")){
                return;
            }
            event.setCancelled(true);

            if(expirationTime == null || agora.isAfter(expirationTime)){
                expirationTime = agora.plusSeconds(5);
            }
            Duration cooldown = Duration.between(agora, expirationTime);

            if(cooldown.getNano() > 0){
                player.sendMessage("Calma calabreso: " + cooldown.getSeconds());
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 0.1F );
                return;
            }

            if(player.isSneaking()){
                player.setVelocity(new Vector(player.getLocation().getDirection().getX() * 1.5,player.getVelocity().getY() + 1, player.getLocation().getDirection().getZ() * 1.5));
                return;
            }
            player.setVelocity(new Vector(player.getVelocity().getX(),player.getVelocity().getY() + 2 , player.getVelocity().getZ()));
        }
    }

    public static List<ItemStack> initSuperGirls(){
        List<ItemStack> superGirls = new ArrayList<>();
        superGirls.add(getSuperSpeed());
        superGirls.add(getFly());
        superGirls.add(getLazer());

        return superGirls;
    }
    
    public static ItemStack getBoots(){
        ItemStack stomperBoots = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta meta = stomperBoots.getItemMeta();

        meta.setDisplayName("Stomper boots");
        stomperBoots.setItemMeta(meta);
        return stomperBoots;
    }

    public static ItemStack getSuperSpeed(){
        ItemStack stomperBoots = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta meta = stomperBoots.getItemMeta();

        meta.setDisplayName("Superspeed");
        stomperBoots.setItemMeta(meta);
        return stomperBoots;
    }

    public static ItemStack getFly(){
        ItemStack superElytra = new ItemStack(Material.ELYTRA);
        ItemMeta meta = superElytra.getItemMeta();

        meta.setDisplayName("Fly");
        superElytra.setItemMeta(meta);
        return superElytra;
    }

    public static ItemStack getLazer(){
        ItemStack item = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Raio Lazer");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getKangaroo(){
        ItemStack kangaroo = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta meta = kangaroo.getItemMeta();

        meta.setDisplayName("Kangaroo");
        kangaroo.setItemMeta(meta);
        return kangaroo;
    }
}
