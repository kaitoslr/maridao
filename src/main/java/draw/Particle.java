package draw;

import io.pluginteste.maridao.Maridao;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class Particle {

    public static void Line(Player player, Location start, Location end, org.bukkit.Particle particle, int points) {
        if (start.getWorld() == null || end.getWorld() == null || !start.getWorld().equals(end.getWorld())) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i <= points; i++) {
                    double t = (double) i / points;
                    double x = start.getX() + (end.getX() - start.getX()) * t;
                    double y = start.getY() + (end.getY() - start.getY()) * t;
                    double z = start.getZ() + (end.getZ() - start.getZ()) * t;

                    start.getWorld().spawnParticle(particle, new Location(start.getWorld(), x, y, z), 0);
                }
            }
        }.runTaskLater(Maridao.getInstance(), 0L);
    }
}