package net.sivils.moreExplosiveWindCharges;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Collection;

public final class MoreExplosiveWindCharges extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);


    }

    @EventHandler
    public void onWindChargeHit(ProjectileHitEvent e) {
        if (e.isCancelled()) return;
        if (!(e.getEntity() instanceof WindCharge windCharge)) return;

        Collection<LivingEntity> entities = e.getEntity().getLocation().getNearbyLivingEntities(getConfig().getDouble("Radius", 1.0));
        for (LivingEntity entity : entities) {
            Vector direction = entity.getLocation().toVector().subtract(windCharge.getLocation().toVector()).normalize();
            double knockbackStrength = getConfig().getDouble("ExplosionMult", 0.5);
            Vector knockback = direction.multiply(knockbackStrength);

            entity.setVelocity(knockback);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("windcharge")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("windcharge.reload")) {
                    sender.sendMessage(Component.text("You don't have permission to reload config", NamedTextColor.RED));
                    return true;
                }

                reloadConfig();
                sender.sendMessage(Component.text("Config reloaded", NamedTextColor.GREEN));
                return true;
            }
        }
        return false;
    }
}
