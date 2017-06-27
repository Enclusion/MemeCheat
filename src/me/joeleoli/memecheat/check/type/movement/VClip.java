package me.joeleoli.memecheat.check.type.movement;

import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.event.player.PacketedMovementEvent;
import me.joeleoli.memecheat.manager.Managers;
import me.joeleoli.memecheat.util.Analyze;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class VClip extends Check {

    private static List<Material> BLOCKED = Arrays.asList(Material.ACTIVATOR_RAIL, Material.TRAP_DOOR, Material.AIR, Material.ANVIL, Material.BED_BLOCK, Material.BIRCH_WOOD_STAIRS, Material.BREWING_STAND, Material.BOAT, Material.BRICK_STAIRS, Material.BROWN_MUSHROOM, Material.CAKE_BLOCK, Material.CARPET, Material.CAULDRON, Material.COBBLESTONE_STAIRS, Material.COBBLE_WALL, Material.DARK_OAK_STAIRS, Material.DIODE, Material.DIODE_BLOCK_ON, Material.DIODE_BLOCK_OFF, Material.DEAD_BUSH, Material.DETECTOR_RAIL, Material.DOUBLE_PLANT, Material.DOUBLE_STEP, Material.DRAGON_EGG, Material.FENCE_GATE, Material.FENCE, Material.PAINTING, Material.FLOWER_POT, Material.GOLD_PLATE, Material.HOPPER, Material.STONE_PLATE, Material.IRON_PLATE, Material.HUGE_MUSHROOM_1, Material.HUGE_MUSHROOM_2, Material.IRON_DOOR_BLOCK, Material.IRON_DOOR, Material.IRON_FENCE, Material.IRON_PLATE, Material.ITEM_FRAME, Material.JUKEBOX, Material.JUNGLE_WOOD_STAIRS, Material.LADDER, Material.LEVER, Material.LONG_GRASS, Material.NETHER_FENCE, Material.NETHER_STALK, Material.NETHER_WARTS, Material.MELON_STEM, Material.PUMPKIN_STEM, Material.QUARTZ_STAIRS, Material.RAILS, Material.RED_MUSHROOM, Material.RED_ROSE, Material.SAPLING, Material.SEEDS, Material.SIGN, Material.SIGN_POST, Material.SKULL, Material.SMOOTH_STAIRS, Material.NETHER_BRICK_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.STAINED_GLASS_PANE, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_LAMP_OFF, Material.REDSTONE_LAMP_ON, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON, Material.REDSTONE_WIRE, Material.SANDSTONE_STAIRS, Material.STEP, Material.ACACIA_STAIRS, Material.SUGAR_CANE, Material.SUGAR_CANE_BLOCK, Material.ENCHANTMENT_TABLE, Material.SOUL_SAND, Material.TORCH, Material.TRAP_DOOR, Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.WALL_SIGN, Material.VINE, Material.WATER_LILY, Material.WEB, Material.WOOD_DOOR, Material.WOOD_DOUBLE_STEP, Material.WOOD_PLATE, Material.WOOD_STAIRS, Material.WOOD_STEP, Material.HOPPER, Material.WOODEN_DOOR, Material.YELLOW_FLOWER, Material.LAVA, Material.WATER, Material.STATIONARY_WATER, Material.STATIONARY_LAVA, Material.CACTUS, Material.CHEST, Material.PISTON_BASE, Material.PISTON_MOVING_PIECE, Material.PISTON_EXTENSION, Material.PISTON_STICKY_BASE, Material.TRAPPED_CHEST, Material.SNOW, Material.ENDER_CHEST, Material.THIN_GLASS, Material.ENDER_PORTAL_FRAME, Material.ENDER_PORTAL);

    public VClip(Plugin plugin) {
        super(plugin, "VClip", "VClip");
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PacketedMovementEvent event) {
        Player player = event.getPlayer();

        if (player.getAllowFlight() || player.getVehicle() == null || player.hasPotionEffect(PotionEffectType.JUMP)) {
            return;
        }

        Location location = player.getLocation();

        if (location.getY() < 0.0 || location.getY() > player.getWorld().getMaxHeight()) {
            return;
        }

        Material FROM = event.getBlockAt(event.getFrom().toVector()).getItemType();
        Material FROM_ABOVE = event.getBlockAt(event.getFrom().toVector().add(new Vector(0, 1, 0))).getItemType();

        Location to = event.getTo().clone();
        Location from = event.getFrom().clone();

        double yDist = to.getY() - from.getY();

        if (Analyze.fullSolid(FROM.getId()) || Analyze.fullSolid(FROM_ABOVE.getId())) {
            return;
        }

        for (int y = (int)Math.round(Math.abs(yDist)), i = 0; i < y; ++i) {
            Location l = (yDist < -0.5) ? to.clone().add(0.0, (double)i, 0.0) : from.clone().add(0.0, (double)i, 0.0);

            MaterialData materialData = event.getBlockAt(l.toVector());
            Material material = materialData.getItemType();

            if (material != null && material.isBlock() && material.isSolid() && material != Material.AIR) {
                if (!VClip.BLOCKED.contains(material)) {
                    Managers.getCheckManager().logCheat(this, player);
                    event.setCancelled(true);
                    return;
                }

                Block block = l.getBlock();
                BlockState blockState = block.getState();

                if (blockState instanceof Openable) {
                    if (!((Openable)blockState).isOpen()) {
                        Managers.getCheckManager().logCheat(this, player);
                        event.setCancelled(true);
                        return;
                    }
                }
                else if (materialData instanceof Openable) {
                    Openable openable = (Openable)blockState.getData();

                    if (material == Material.TRAP_DOOR && !openable.isOpen()) {
                        Managers.getCheckManager().logCheat(this, player);
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

}