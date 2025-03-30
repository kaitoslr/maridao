package io.pluginteste.maridao;
import ComandoHome.ComandoHome;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import HGclasses.Classes;
import org.bukkit.configuration.file.FileConfiguration;
import ComandoHome.ComandoSetHome;

public final class Maridao extends JavaPlugin {

    private static int tickCounter = 0;
    private FileConfiguration config;
    private static Maridao instance;
    public static Maridao getInstance() {return instance;}

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        getCommand("ola").setExecutor(new ComandoOla());

        //----------------------------------------------------------------------

        getCommand("acessorios").setExecutor(new Acessorios(this));

        //---------------------------------------------------------------------

        getCommand("stomper").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                Player jogador = (Player) sender;
                jogador.getInventory().addItem(Classes.getBoots());
                jogador.sendMessage("§aVocê recebeu a botas de stomper!");
            }
            return true;
        });
        getServer().getPluginManager().registerEvents(new Classes(), this);

        //-----------------------------------------------------------------------

        getCommand("kangaroo").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                Player jogador = (Player) sender;
                jogador.getInventory().addItem(Classes.getKangaroo());
                jogador.sendMessage("§aVocê recebeu a botas de stomper!");
            }
            return true;
        });
        //-------------------------------------------------------------------

        getCommand("superpoderosa").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                Player jogador = (Player) sender;
                for(ItemStack item : Classes.initSuperGirls()){
                    jogador.getInventory().addItem(item);
                }
                jogador.sendMessage("§aVocê se tornou uma menina super poderosa!!!");
            }
            return true;
        });

        //-------------------------------------------------------------------

        getCommand("espadatrovao").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                Player jogador = (Player) sender;
                jogador.getInventory().addItem(EspadaTrovão.criarEspada());
                jogador.sendMessage("§aVocê recebeu a Espada do Trovão!");
            }
            return true;
        });

        getServer().getPluginManager().registerEvents(new EspadaTrovão(), this);

        //---------------------------------------------------

        getCommand("home").setExecutor(new ComandoHome(this));

        getCommand("sethome").setExecutor(new ComandoSetHome(this));

        //arquivo de configuração
        saveDefaultConfig();
        config = getConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //salva a o arquivo de configuração
        saveConfig();
    }
    public String getHomeLocation(String playerName) {
        return config.getString("homes." + playerName, null);
    }
    public void saveHomeLocation (String playerName, String loc){
        config.set("homes." + playerName, loc);
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

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Location loc = player.getLocation();

                                for (int i = 0; i < 10; i++) {
                                    double angle = 360.0 / 10.0 * i;
                                    double raio = 0.3;
                                    double radian = Math.toRadians(angle);

                                    double circY = loc.getY() + 2.2;

                                    Location raius = new Location(loc.getWorld(), loc.getX() + (Math.sin(radian) * raio), circY, loc.getZ() + (Math.cos(radian) * raio));
                                    player.getWorld().spawnParticle(Particle.CRIT, raius, 0);

                                }

                            }
                        }.runTaskTimer(plugin, 0L, 1L);

                    }

                    if(args[0].equals("asas")){

                        sender.sendMessage("Acesserio asas equipado!!!");

                        new BukkitRunnable() {

                            @Override
                            public void run() {

                                tickCounter++;

                                if(tickCounter % 100 == 0){
                                    tickCounter = 0;
                                }

                                Location loc = player.getLocation();

                                asas(loc, player, tickCounter);

                            }
                        }.runTaskTimer(plugin, 0L, 1L);

                    }
                } catch (CommandException e) {
                    throw new CommandException();
                }
            }
            return true;
        }

        public double asasBater(double bater){
            return Math.pow(Math.E, Math.cos(bater)) - 2 * Math.cos(bater * 4) + Math.pow(Math.sin(bater/12), 5);
        }

        public void asas(Location location, Player player, int time){

            int max = 1000;

            for (double i = 0; i < max; i++) {

                double x = 0;

                if(time <= 50){
                    x = time/100 * asasBater(i) * Math.sin(i);
                }
                if(time > 50){
                    x = (10-(time/100)) * asasBater(i) * Math.sin(i);
                }
                double y = asasBater(i) * Math.cos(i);

                Location newLoc = new Location(location.getWorld(), location.getX() + x, location.getY() + y, location.getZ());

                player.getWorld().spawnParticle(Particle.SMOKE, newLoc, 0);

            }
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


