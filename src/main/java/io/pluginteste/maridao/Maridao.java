package io.pluginteste.maridao;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.LongConsumer;

public final class Maridao extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("ola").setExecutor(new ComandoOla());
        getCommand("acessorios").setExecutor(new Acessorios(this));
        getServer().getPluginManager().registerEvents(new EventoEntrada(), this);
        getCommand("espadatrovao").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                Player jogador = (Player) sender;
                jogador.getInventory().addItem(EspadaTrovão.criarEspada());
                jogador.sendMessage("§aVocê recebeu a Espada do Trovão!");
            }
            return true;
        });

        getServer().getPluginManager().registerEvents(new EspadaTrovão(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static class Acessorios implements CommandExecutor {
        private final JavaPlugin plugin;


        public Acessorios(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
            if(command.getName().equalsIgnoreCase("acessorios")){
                
                if(args.length < 1 ){
                    sender.sendMessage("Voce deve passar um tipo de acessorio!!!");
                }

                Player player = (Player) sender;

                sender.sendMessage(args);

                try{
                    if(args[0].equals("aureola")){

                        sender.sendMessage("Acesserio aureola equipado!!!");


                        Particle particula = Particle.valueOf("HEART");

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Location loc = player.getLocation();

                                double newY = loc.getY() + 2;

                                Location newLoc = new Location(loc.getWorld(), loc.getX(), newY, loc.getZ());

                                for (int i = 0; i < 50; i++){
                                    double angle = 360/50;
                                    double raio = 1.5;
                                    Location raius = new Location(loc.getWorld(), loc.getX() + Math.acos(angle * i), newY, loc.getZ() + Math.cos(angle * i));
                                    player.getWorld().spawnParticle(particula, raius, 10);

                                }

                            }
                        }.runTaskTimer(plugin, 0L, 1L);

                    }
                } catch (CommandException e) {
                    throw new CommandException();
                }
            }
            return true;
        }

    }

    public static class ComandoOla implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player jogador = (Player) sender;
                jogador.sendMessage("§aOlá, " + jogador.getName() + "! Bem-vindo ao servidor!");
            } else {
                sender.sendMessage("Este comando só pode ser usado por jogadores.");
            }
            return true;
        }
    }

    public class EventoEntrada implements Listener {
        @EventHandler
        public void aoEntrar(PlayerJoinEvent event) {
            Player jogador = event.getPlayer();
            jogador.sendMessage("§aBem-vindo ao servidor, " + jogador.getName() + "!");
        }
    }

    public class EspadaTrovão implements Listener {
        @EventHandler
        public void aoUsar(EntityDamageByEntityEvent event) {
            if(event.getDamager() instanceof Player){
                Player jogador = (Player) event.getDamager();
                ItemStack item = jogador.getInventory().getItemInMainHand();
                Entity target = event.getEntity();

                if (item.getType() == Material.DIAMOND_SWORD && item.hasItemMeta() &&
                        item.getItemMeta().getDisplayName().equals("§bEspada do Trovão") &&
                        target instanceof LivingEntity) {
                    jogador.getWorld().strikeLightning(target.getLocation());
                }
            }
        }

        public static ItemStack criarEspada() {
            ItemStack espada = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta meta = espada.getItemMeta();
            meta.setDisplayName("§bEspada do Trovão");
            espada.setItemMeta(meta);
            return espada;
        }
    }
}


