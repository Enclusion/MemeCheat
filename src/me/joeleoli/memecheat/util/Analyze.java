package me.joeleoli.memecheat.util;

import me.joeleoli.memecheat.event.player.PacketedMovementEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyze {

    private static List<Material> BLOCKED = Arrays.asList(Material.ACTIVATOR_RAIL, Material.AIR, Material.BOAT, Material.BROWN_MUSHROOM, Material.DIODE, Material.DIODE_BLOCK_ON, Material.DIODE_BLOCK_OFF, Material.DEAD_BUSH, Material.DETECTOR_RAIL, Material.DOUBLE_PLANT, Material.DRAGON_EGG, Material.PAINTING, Material.GOLD_PLATE, Material.STONE_PLATE, Material.IRON_PLATE, Material.IRON_PLATE, Material.ITEM_FRAME, Material.JUKEBOX, Material.LADDER, Material.LEVER, Material.LONG_GRASS, Material.MELON_STEM, Material.PUMPKIN_STEM, Material.RAILS,  Material.RED_MUSHROOM, Material.RED_ROSE, Material.SAPLING,  Material.SEEDS, Material.SIGN, Material.SIGN_POST, Material.SKULL, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_LAMP_OFF, Material.REDSTONE_LAMP_ON, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON, Material.REDSTONE_WIRE, Material.ACACIA_STAIRS, Material.SUGAR_CANE, Material.SUGAR_CANE_BLOCK, Material.SOUL_SAND, Material.TORCH, Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.WALL_SIGN, Material.VINE, Material.WATER_LILY, Material.YELLOW_FLOWER, Material.PISTON_EXTENSION, Material.PISTON_STICKY_BASE, Material.SNOW);
    private static List<Material> ICE = Arrays.asList(Material.PACKED_ICE, Material.ICE);
    private static List<Material> FENCE = Arrays.asList(Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER);
    private static List<Material> SLABS = Arrays.asList(Material.STEP, Material.WOOD_STEP, Material.DOUBLE_STEP, Material.WOOD_DOUBLE_STEP);
    private static List<Material> STAIRS = Arrays.asList(Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.BRICK_STAIRS, Material.COBBLESTONE_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS, Material.NETHER_BRICK_STAIRS, Material.SANDSTONE_STAIRS, Material.SMOOTH_STAIRS, Material.WOOD_STAIRS, Material.SPRUCE_WOOD_STAIRS);
    private static List<Material> WITHIN = Arrays.asList(Material.SAND, Material.GRAVEL, Material.WOOD_DOOR, Material.TRAP_DOOR, Material.IRON_DOOR, Material.IRON_DOOR_BLOCK, Material.FENCE, Material.FENCE_GATE, Material.NETHER_FENCE, Material.IRON_FENCE, Material.STAINED_GLASS_PANE, Material.SIGN, Material.WALL_SIGN, Material.COBBLE_WALL, Material.SIGN_POST);
    private static List<Material> INSTANT_BREAK = new ArrayList<>();
    private static List<Material> FOOD = new ArrayList<>();
    private static List<Material> INTERACTIVE = new ArrayList<>();
    private static Map<Material, Material> COMBO = new HashMap<>();
    private static HashSet<Byte> blockPassSet = new HashSet<>();
    private static HashSet<Byte> blockAirFoliageSet = new HashSet<>();
    private static HashSet<Byte> fullSolid = new HashSet<>();
    private static HashSet<Byte> blockUseSet = new HashSet<>();

    public static boolean isSafeSetbackLocation(Player player) {
        return (isInWeb(player) || isInWater(player) || !cantStandAtSingle(player.getLocation().getBlock())) && !player.getEyeLocation().getBlock().getType().isSolid();
    }

    public static double getXDelta(Location one, Location two) {
        return Math.abs(one.getX() - two.getX());
    }

    public static double getZDelta(Location one, Location two) {
        return Math.abs(one.getZ() - two.getZ());
    }

    public static double getDistance3D(Location one, Location two) {
        double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
        double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        double sqrt = Math.sqrt(xSqr + ySqr + zSqr);
        return Math.abs(sqrt);
    }

    public static double getVerticalDistance(Location one, Location two) {
        double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
        double sqrt = Math.sqrt(ySqr);
        return Math.abs(sqrt);
    }

    public static double getHorizontalDistance(Location one, Location two) {
        double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        double sqrt = Math.sqrt(xSqr + zSqr);
        return Math.abs(sqrt);
    }

    public static boolean cantStandAtBetter(Block block) {
        Block otherBlock = block.getRelative(BlockFace.DOWN);

        boolean center1 = otherBlock.getType() == Material.AIR;
        boolean north1 = otherBlock.getRelative(BlockFace.NORTH).getType() == Material.AIR;
        boolean east1 = otherBlock.getRelative(BlockFace.EAST).getType() == Material.AIR;
        boolean south1 = otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.AIR;
        boolean west1 = otherBlock.getRelative(BlockFace.WEST).getType() == Material.AIR;
        boolean northeast1 = otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.AIR;
        boolean northwest1 = otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.AIR;
        boolean southeast1 = otherBlock.getRelative(BlockFace.SOUTH_EAST).getType() == Material.AIR;
        boolean southwest1 = otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.AIR;
        boolean overAir1 = otherBlock.getRelative(BlockFace.DOWN).getType() == Material.AIR || otherBlock.getRelative(BlockFace.DOWN).getType() == Material.WATER || otherBlock.getRelative(BlockFace.DOWN).getType() == Material.LAVA;

        return center1 && north1 && east1 && south1 && west1 && northeast1 && southeast1 && northwest1 && southwest1 && overAir1;
    }

    public static boolean isBlocked(Material check) {
        return BLOCKED.contains(check);
    }

    public static boolean isIce(Material check) {
        return ICE.contains(check);
    }

    public static boolean isStair(Material check) {
        return STAIRS.contains(check);
    }

    public static boolean isFence(Material check) {
        return FENCE.contains(check);
    }

    public static boolean onGround(Material BELOW, Material HALF_BELOW) {
        return BLOCKED.contains(BELOW) || !BLOCKED.contains(HALF_BELOW) || FENCE.contains(BELOW);
    }

    public static boolean onIce(final Material BELOW, final Material DOUBLEBELOW) {
        return ICE.contains(BELOW) || ICE.contains(DOUBLEBELOW);
    }

    public static boolean onStairs(final Material BELOW, final Material DOUBLEBELOW) {
        return STAIRS.contains(BELOW) || STAIRS.contains(DOUBLEBELOW);
    }

    public static boolean onSlabs(final Material BELOW, final Material DOUBLEBELOW) {
        return SLABS.contains(BELOW) || SLABS.contains(DOUBLEBELOW);
    }

    public static boolean canStandAt(final Material UPPEREYES) {
        return WITHIN.contains(UPPEREYES);
    }

    public static boolean cantStandAtSingle(Block block) {
        return block.getRelative(BlockFace.DOWN).getType() == Material.AIR;
    }

    public static boolean cantStandAtWater(Block block) {
        Block otherBlock = block.getRelative(BlockFace.DOWN);

        boolean isHover = block.getType() == Material.AIR;
        boolean n = otherBlock.getRelative(BlockFace.NORTH).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER;
        boolean s = otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.WATER || otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.STATIONARY_WATER;
        boolean e = otherBlock.getRelative(BlockFace.EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.EAST).getType() == Material.STATIONARY_WATER;
        boolean w = otherBlock.getRelative(BlockFace.WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.WEST).getType() == Material.STATIONARY_WATER;
        boolean ne = otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.STATIONARY_WATER;
        boolean nw = otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.STATIONARY_WATER;
        boolean se = otherBlock.getRelative(BlockFace.SOUTH_EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER;
        boolean sw = otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.STATIONARY_WATER;

        return n && s && e && w && ne && nw && se && sw && isHover;
    }

    public static boolean canStandWithin(Block block) {
        boolean isSand = block.getType() == Material.SAND;
        boolean isGravel = block.getType() == Material.GRAVEL;
        boolean solid = block.getType().isSolid() && !block.getType().name().toLowerCase().contains("door") && !block.getType().name().toLowerCase().contains("fence") && !block.getType().name().toLowerCase().contains("bars") && !block.getType().name().toLowerCase().contains("sign");

        return !isSand && !isGravel && !solid;
    }

    public static boolean isJustAir(Set<Material> materialSet) {
        for (Material material : materialSet) {
            if (material != Material.AIR) {
                return false;
            }
        }

        return true;
    }

    public static Vector getRotation(Location one, Location two) {
        double dx = two.getX() - one.getX();
        double dy = two.getY() - one.getY();
        double dz = two.getZ() - one.getZ();

        double distanceXZ = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float)(Math.atan2(dz, dx) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(-(Math.atan2(dy, distanceXZ) * 180.0 / 3.141592653589793));

        return new Vector(yaw, pitch, 0.0f);
    }

    public static double clamp180(double theta) {
        theta %= 360.0;

        if (theta >= 180.0) {
            theta -= 360.0;
        }

        if (theta < -180.0) {
            theta += 360.0;
        }

        return theta;
    }

    public static int getLevelForEnchantment(Player player, String enchantment) {
        try {
            Enchantment theEnchantment = Enchantment.getByName(enchantment);

            for (ItemStack item : player.getInventory().getContents()) {
                if (item.containsEnchantment(theEnchantment)) {
                    return item.getEnchantmentLevel(theEnchantment);
                }
            }
        }
        catch (Exception e) {
            return -1;
        }

        return -1;
    }

    public static boolean cantStandAt(Block block) {
        return !canStand(block) && cantStandClose(block) && cantStandFar(block);
    }

    public static boolean cantStandAtExp(Location location) {
        return cantStandAt(new Location(location.getWorld(), fixXAxis(location.getX()), location.getY() - 0.01, (double)location.getBlockZ()).getBlock());
    }

    public static boolean cantStandClose(Block block) {
        return !canStand(block.getRelative(BlockFace.NORTH)) && !canStand(block.getRelative(BlockFace.EAST)) && !canStand(block.getRelative(BlockFace.SOUTH)) && !canStand(block.getRelative(BlockFace.WEST));
    }

    public static boolean cantStandFar(Block block) {
        return !canStand(block.getRelative(BlockFace.NORTH_WEST)) && !canStand(block.getRelative(BlockFace.NORTH_EAST)) && !canStand(block.getRelative(BlockFace.SOUTH_WEST)) && !canStand(block.getRelative(BlockFace.SOUTH_EAST));
    }

    public static boolean canStand(Block block) {
        return !block.isLiquid() && block.getType() != Material.AIR;
    }

    public static boolean isFullyInWater(Location player) {
        double touchedX = fixXAxis(player.getX());
        return new Location(player.getWorld(), touchedX, player.getY(), (double)player.getBlockZ()).getBlock().isLiquid() && new Location(player.getWorld(), touchedX, Math.round(player.getY()), (double)player.getBlockZ()).getBlock().isLiquid();
    }

    public static double fixXAxis(double x) {
        double touchedX = x;
        double rem = touchedX - Math.round(touchedX) + 0.01;

        if (rem < 0.3) {
            touchedX = NumberConversions.floor(x) - 1;
        }

        return touchedX;
    }

    public static boolean isHoveringOverWater(Location player, int blocks) {
        for (int i = player.getBlockY(); i > player.getBlockY() - blocks; --i) {
            Block block = new Location(player.getWorld(), player.getBlockX(), i, player.getBlockZ()).getBlock();

            if (block.getType() != Material.AIR) {
                return block.isLiquid();
            }
        }

        return false;
    }

    public static boolean isHoveringOverWater(Location player) {
        return isHoveringOverWater(player, 25);
    }

    public static boolean isInstantBreak(Material m) {
        return INSTANT_BREAK.contains(m);
    }

    public static boolean isFood(Material m) {
        return FOOD.contains(m);
    }

    public static boolean isSlab(Block block) {
        Material type = block.getType();

        switch (type) {
            case CLAY:
            case CLAY_BALL:
            case GOLD_BLOCK:
            case GOLD_BOOTS: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public static boolean isStair(Block block) {
        Material type = block.getType();

        switch (type) {
            case COMMAND:
            case DIAMOND_BARDING:
            case FISHING_ROD:
            case FURNACE:
            case GOLD_HELMET:
            case GOLD_PICKAXE:
            case GOLD_PLATE:
            case GOLD_RECORD:
            case IRON_DOOR: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public static boolean isInteractable(Material m) {
        return INTERACTIVE.contains(m);
    }

    public static boolean sprintFly(Player player) {
        return player.isSprinting() || player.isFlying();
    }

    public static boolean isOnLilyPad(LivingEntity player) {
        Block block = player.getLocation().getBlock();
        Material lily = Material.WATER_LILY;
        return block.getType() == lily || block.getRelative(BlockFace.NORTH).getType() == lily || block.getRelative(BlockFace.SOUTH).getType() == lily || block.getRelative(BlockFace.EAST).getType() == lily || block.getRelative(BlockFace.WEST).getType() == lily;
    }

    public static boolean isSubmersed(LivingEntity player) {
        return player.getLocation().getBlock().isLiquid() && player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid();
    }

    public static boolean isInWater(LivingEntity player) {
        return player.getLocation().getBlock().isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid();
    }

    public static boolean isInWeb(LivingEntity player) {
        return player.getLocation().getBlock().getType() == Material.WEB || player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WEB || player.getLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.WEB;
    }

    public static boolean isClimbableBlock(Block block) {
        return block.getType() == Material.VINE || block.getType() == Material.LADDER || block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER;
    }

    public static boolean isOnVine(Player player) {
        return player.getLocation().getBlock().getType() == Material.VINE;
    }

    public static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public static boolean blocksNear(Player player) {
        return blocksNear(player.getLocation());
    }

    public static boolean blocksAbove(Player player) {
        Block b = player.getLocation().add(0.0, player.getEyeHeight() + 0.5, 0.0).getBlock();
        if (!BLOCKED.contains(b.getType()) || !BLOCKED.contains(b.getRelative(BlockFace.UP).getType())) {
            return true;
        }
        for (BlockFace blockFace : Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH)) {
            Block check = b.getRelative(blockFace);
            if (!BLOCKED.contains(check.getType())) {
                return true;
            }
            check = check.getRelative(BlockFace.UP);
            if (!BLOCKED.contains(check.getType())) {
                return true;
            }
        }
        return false;
    }

    public static boolean blocksBehind(Player player, Vector velocity) {
        velocity = velocity.clone().normalize();
        velocity.setY(0);
        Location location = player.getLocation();
        Location newlocation = location.subtract(velocity);
        Block b = newlocation.getBlock();

        if (!BLOCKED.contains(b.getType()) || !BLOCKED.contains(b.getRelative(BlockFace.UP).getType())) {
            return true;
        }

        for (BlockFace blockFace : Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH)) {
            Block check = b.getRelative(blockFace);

            if (!BLOCKED.contains(check.getType())) {
                return true;
            }

            check = check.getRelative(BlockFace.UP);

            if (!BLOCKED.contains(check.getType())) {
                return true;
            }
        }

        return false;
    }

    public static boolean blocksNear(Location loc) {
        boolean nearBlocks = false;

        for (Block block : getSurrounding(loc.getBlock(), true)) {
            if (block.getType() != Material.AIR) {
                nearBlocks = true;
                break;
            }
        }

        for (Block block : getSurrounding(loc.getBlock(), false)) {
            if (block.getType() != Material.AIR) {
                nearBlocks = true;
                break;
            }
        }

        loc.setY(loc.getY() - 0.5);

        if (loc.getBlock().getType() != Material.AIR) {
            nearBlocks = true;
        }

        if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER })) {
            nearBlocks = true;
        }

        return nearBlocks;
    }

    public static boolean slabsNear(Location loc) {
        boolean nearBlocks = false;

        for (Block bl : getSurrounding(loc.getBlock(), true)) {
            if (bl.getType().equals((Object)Material.STEP) || bl.getType().equals((Object)Material.DOUBLE_STEP) || bl.getType().equals((Object)Material.WOOD_DOUBLE_STEP) || bl.getType().equals((Object)Material.WOOD_STEP)) {
                nearBlocks = true;
                break;
            }
        }

        for (Block bl : getSurrounding(loc.getBlock(), false)) {
            if (bl.getType().equals((Object)Material.STEP) || bl.getType().equals((Object)Material.DOUBLE_STEP) || bl.getType().equals((Object)Material.WOOD_DOUBLE_STEP) || bl.getType().equals((Object)Material.WOOD_STEP)) {
                nearBlocks = true;
                break;
            }
        }

        if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.STEP, Material.DOUBLE_STEP, Material.WOOD_DOUBLE_STEP, Material.WOOD_STEP })) {
            nearBlocks = true;
        }

        return nearBlocks;
    }

    public static boolean isBlock(Block block, Material[] materials) {
        Material type = block.getType();

        for (Material m : materials) {
            if (m == type) {
                return true;
            }
        }

        return false;
    }

    public static String[] getCommands(String command) {
        return command.replaceAll("COMMAND\\[", "").replaceAll("]", "").split(";");
    }

    public static String removeWhitespace(String string) {
        return string.replaceAll(" ", "");
    }

    public static boolean hasArmorEnchantment(Player player, Enchantment e) {
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null && item.containsEnchantment(e)) {
                return true;
            }
        }

        return false;
    }

    public static String listToCommaString(List<String> list) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < list.size(); ++i) {
            b.append(list.get(i));
            if (i < list.size() - 1) {
                b.append(",");
            }
        }
        return b.toString();
    }

    public static long lifeToSeconds(String string) {
        if (string.equals("0") || string.equals("")) {
            return 0L;
        }

        String[] lifeMatch = { "d", "h", "m", "s" };
        int[] lifeInterval = { 86400, 3600, 60, 1 };
        long seconds = 0L;

        for (int i = 0; i < lifeMatch.length; ++i) {
            Matcher matcher = Pattern.compile("([0-9]*)" + lifeMatch[i]).matcher(string);

            while (matcher.find()) {
                seconds += Integer.parseInt(matcher.group(1)) * lifeInterval[i];
            }
        }

        return seconds;
    }

    public static double[] cursor(Player player, LivingEntity entity) {
        Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        Location playerLoc = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);

        Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        Vector expectedRotation = getRotation(playerLoc, entityLoc);

        double deltaYaw = clamp180(playerRotation.getX() - expectedRotation.getX());
        double deltaPitch = clamp180(playerRotation.getY() - expectedRotation.getY());

        double horizontalDistance = getHorizontalDistance(playerLoc, entityLoc);
        double distance = getDistance3D(playerLoc, entityLoc);

        double offsetX = deltaYaw * horizontalDistance * distance;
        double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;

        return new double[] { Math.abs(offsetX), Math.abs(offsetY) };
    }

    public static double getAimbotoffset(Location playerLocLoc, double playerEyeHeight, LivingEntity entity) {
        Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        Location playerLoc = playerLocLoc.add(0.0, playerEyeHeight, 0.0);

        Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        Vector expectedRotation = getRotation(playerLoc, entityLoc);

        double deltaYaw = clamp180(playerRotation.getX() - expectedRotation.getX());
        double horizontalDistance = getHorizontalDistance(playerLoc, entityLoc);
        double distance = getDistance3D(playerLoc, entityLoc);
        double offsetX = deltaYaw * horizontalDistance * distance;

        return offsetX;
    }

    public static double getAimbotoffset2(Location playerLocLoc, double playerEyeHeight, LivingEntity entity) {
        Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        Location playerLoc = playerLocLoc.add(0.0, playerEyeHeight, 0.0);

        Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        Vector expectedRotation = getRotation(playerLoc, entityLoc);

        double deltaPitch = clamp180(playerRotation.getY() - expectedRotation.getY());

        double distance = getDistance3D(playerLoc, entityLoc);

        double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;
        return offsetY;
    }

    public static double[] getOffsetsOffCursor(Player player, LivingEntity entity) {
        Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        Location playerLoc = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);

        Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        Vector expectedRotation = getRotation(playerLoc, entityLoc);

        double deltaYaw = clamp180(playerRotation.getX() - expectedRotation.getX());
        double deltaPitch = clamp180(playerRotation.getY() - expectedRotation.getY());

        double horizontalDistance = getHorizontalDistance(playerLoc, entityLoc);
        double distance = getDistance3D(playerLoc, entityLoc);

        double offsetX = deltaYaw * horizontalDistance * distance;
        double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;

        return new double[] { Math.abs(offsetX), Math.abs(offsetY) };
    }

    public static double getOffsetOffCursor(Player player, LivingEntity entity) {
        double offset = 0.0;

        double[] offsets = getOffsetsOffCursor(player, entity);
        offset += offsets[0];
        offset += offsets[1];

        return offset;
    }

    public static Block getLowestBlockAt(Location Location) {
        Block Block = Location.getWorld().getBlockAt((int)Location.getX(), 0, (int)Location.getZ());

        if (Block == null || Block.getType().equals(Material.AIR)) {
            Block = Location.getBlock();

            for (int y = (int)Location.getY(); y > 0; --y) {
                Block Current = Location.getWorld().getBlockAt((int)Location.getX(), y, (int)Location.getZ());
                Block Below = Current.getLocation().subtract(0.0, 1.0, 0.0).getBlock();

                if (Below == null || Below.getType().equals(Material.AIR)) {
                    Block = Current;
                }
            }
        }

        return Block;
    }

    public static boolean containsBlock(Location Location, Material Material) {
        for (int y = 0; y < 256; ++y) {
            Block Current = Location.getWorld().getBlockAt((int)Location.getX(), y, (int)Location.getZ());

            if (Current != null && Current.getType().equals(Material)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsBlock(Location Location) {
        for (int y = 0; y < 256; ++y) {
            Block Current = Location.getWorld().getBlockAt((int)Location.getX(), y, (int)Location.getZ());

            if (Current != null && !Current.getType().equals(Material.AIR)) {
                return true;
            }
        }

        return false;
    }

    public static boolean containsBlockBelow(Location Location) {
        for (int y = 0; y < (int)Location.getY(); ++y) {
            Block Current = Location.getWorld().getBlockAt((int)Location.getX(), y, (int)Location.getZ());

            if (Current != null && !Current.getType().equals(Material.AIR)) {
                return true;
            }
        }

        return false;
    }

    public static ArrayList<Block> getBlocksAroundCenter(Location loc, int radius) {
        ArrayList<Block> blocks = new ArrayList<>();

        for (int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; ++x) {
            for (int y = loc.getBlockY() - radius; y <= loc.getBlockY() + radius; ++y) {
                for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; ++z) {
                    Location l = new Location(loc.getWorld(), (double)x, (double)y, (double)z);

                    if (l.distance(loc) <= radius) {
                        blocks.add(l.getBlock());
                    }
                }
            }
        }

        return blocks;
    }

    public static boolean solid(Block block) {
        return block != null && solid(block.getTypeId());
    }

    public static boolean solid(int block) {
        return solid((byte)block);
    }

    public static boolean solid(byte block) {
        if (blockPassSet.isEmpty()) {
            blockPassSet.add((byte)0);
            blockPassSet.add((byte)6);
            blockPassSet.add((byte)8);
            blockPassSet.add((byte)9);
            blockPassSet.add((byte)10);
            blockPassSet.add((byte)11);
            blockPassSet.add((byte)26);
            blockPassSet.add((byte)27);
            blockPassSet.add((byte)28);
            blockPassSet.add((byte)30);
            blockPassSet.add((byte)31);
            blockPassSet.add((byte)32);
            blockPassSet.add((byte)37);
            blockPassSet.add((byte)38);
            blockPassSet.add((byte)39);
            blockPassSet.add((byte)40);
            blockPassSet.add((byte)50);
            blockPassSet.add((byte)51);
            blockPassSet.add((byte)55);
            blockPassSet.add((byte)59);
            blockPassSet.add((byte)63);
            blockPassSet.add((byte)64);
            blockPassSet.add((byte)65);
            blockPassSet.add((byte)66);
            blockPassSet.add((byte)68);
            blockPassSet.add((byte)69);
            blockPassSet.add((byte)70);
            blockPassSet.add((byte)71);
            blockPassSet.add((byte)72);
            blockPassSet.add((byte)75);
            blockPassSet.add((byte)76);
            blockPassSet.add((byte)77);
            blockPassSet.add((byte)78);
            blockPassSet.add((byte)83);
            blockPassSet.add((byte)90);
            blockPassSet.add((byte)92);
            blockPassSet.add((byte)93);
            blockPassSet.add((byte)94);
            blockPassSet.add((byte)96);
            blockPassSet.add((byte)101);
            blockPassSet.add((byte)102);
            blockPassSet.add((byte)104);
            blockPassSet.add((byte)105);
            blockPassSet.add((byte)106);
            blockPassSet.add((byte)107);
            blockPassSet.add((byte)111);
            blockPassSet.add((byte)115);
            blockPassSet.add((byte)116);
            blockPassSet.add((byte)117);
            blockPassSet.add((byte)118);
            blockPassSet.add((byte)119);
            blockPassSet.add((byte)120);
            blockPassSet.add((byte)(-85));
        }

        return !blockPassSet.contains(block);
    }

    public static boolean airFoliage(Block block) {
        return block != null && airFoliage(block.getTypeId());
    }

    public static boolean airFoliage(int block) {
        return airFoliage((byte)block);
    }

    public static boolean airFoliage(byte block) {
        if (blockAirFoliageSet.isEmpty()) {
            blockAirFoliageSet.add((byte)0);
            blockAirFoliageSet.add((byte)6);
            blockAirFoliageSet.add((byte)31);
            blockAirFoliageSet.add((byte)32);
            blockAirFoliageSet.add((byte)37);
            blockAirFoliageSet.add((byte)38);
            blockAirFoliageSet.add((byte)39);
            blockAirFoliageSet.add((byte)40);
            blockAirFoliageSet.add((byte)51);
            blockAirFoliageSet.add((byte)59);
            blockAirFoliageSet.add((byte)104);
            blockAirFoliageSet.add((byte)105);
            blockAirFoliageSet.add((byte)115);
            blockAirFoliageSet.add((byte)(-115));
            blockAirFoliageSet.add((byte)(-114));
        }

        return blockAirFoliageSet.contains(block);
    }

    public static boolean fullSolid(Block block) {
        return block != null && fullSolid(block.getTypeId());
    }

    public static boolean fullSolid(int block) {
        return fullSolid((byte)block);
    }

    public static boolean fullSolid(byte block) {
        if (fullSolid.isEmpty()) {
            fullSolid.add((byte)1);
            fullSolid.add((byte)2);
            fullSolid.add((byte)3);
            fullSolid.add((byte)4);
            fullSolid.add((byte)5);
            fullSolid.add((byte)7);
            fullSolid.add((byte)12);
            fullSolid.add((byte)13);
            fullSolid.add((byte)14);
            fullSolid.add((byte)15);
            fullSolid.add((byte)16);
            fullSolid.add((byte)17);
            fullSolid.add((byte)19);
            fullSolid.add((byte)20);
            fullSolid.add((byte)21);
            fullSolid.add((byte)22);
            fullSolid.add((byte)23);
            fullSolid.add((byte)24);
            fullSolid.add((byte)25);
            fullSolid.add((byte)29);
            fullSolid.add((byte)33);
            fullSolid.add((byte)35);
            fullSolid.add((byte)41);
            fullSolid.add((byte)42);
            fullSolid.add((byte)43);
            fullSolid.add((byte)44);
            fullSolid.add((byte)45);
            fullSolid.add((byte)46);
            fullSolid.add((byte)47);
            fullSolid.add((byte)48);
            fullSolid.add((byte)49);
            fullSolid.add((byte)56);
            fullSolid.add((byte)57);
            fullSolid.add((byte)58);
            fullSolid.add((byte)60);
            fullSolid.add((byte)61);
            fullSolid.add((byte)62);
            fullSolid.add((byte)73);
            fullSolid.add((byte)74);
            fullSolid.add((byte)79);
            fullSolid.add((byte)80);
            fullSolid.add((byte)82);
            fullSolid.add((byte)84);
            fullSolid.add((byte)86);
            fullSolid.add((byte)87);
            fullSolid.add((byte)88);
            fullSolid.add((byte)89);
            fullSolid.add((byte)91);
            fullSolid.add((byte)95);
            fullSolid.add((byte)97);
            fullSolid.add((byte)98);
            fullSolid.add((byte)99);
            fullSolid.add((byte)100);
            fullSolid.add((byte)103);
            fullSolid.add((byte)110);
            fullSolid.add((byte)112);
            fullSolid.add((byte)121);
            fullSolid.add((byte)123);
            fullSolid.add((byte)124);
            fullSolid.add((byte)125);
            fullSolid.add((byte)126);
            fullSolid.add((byte)(-127));
            fullSolid.add((byte)(-123));
            fullSolid.add((byte)(-119));
            fullSolid.add((byte)(-118));
            fullSolid.add((byte)(-104));
            fullSolid.add((byte)(-103));
            fullSolid.add((byte)(-101));
            fullSolid.add((byte)(-98));
        }

        return fullSolid.contains(block);
    }

    public static boolean usable(Block block) {
        return block != null && usable(block.getTypeId());
    }

    public static boolean usable(int block) {
        return usable((byte)block);
    }

    public static boolean usable(byte block) {
        if (blockUseSet.isEmpty()) {
            blockUseSet.add((byte)23);
            blockUseSet.add((byte)26);
            blockUseSet.add((byte)33);
            blockUseSet.add((byte)47);
            blockUseSet.add((byte)54);
            blockUseSet.add((byte)58);
            blockUseSet.add((byte)61);
            blockUseSet.add((byte)62);
            blockUseSet.add((byte)64);
            blockUseSet.add((byte)69);
            blockUseSet.add((byte)71);
            blockUseSet.add((byte)77);
            blockUseSet.add((byte)93);
            blockUseSet.add((byte)94);
            blockUseSet.add((byte)96);
            blockUseSet.add((byte)107);
            blockUseSet.add((byte)116);
            blockUseSet.add((byte)117);
            blockUseSet.add((byte)(-126));
            blockUseSet.add((byte)(-111));
            blockUseSet.add((byte)(-110));
            blockUseSet.add((byte)(-102));
            blockUseSet.add((byte)(-98));
        }

        return blockUseSet.contains(block);
    }

    public static HashMap<Block, Double> getInRadius(Location loc, double dR) {
        return getInRadius(loc, dR, 999.0);
    }

    public static HashMap<Block, Double> getInRadius(Location loc, double dR, double heightLimit) {
        HashMap<Block, Double> blockList = new HashMap<>();

        for (int iR = (int)dR + 1, x = -iR; x <= iR; ++x) {
            for (int z = -iR; z <= iR; ++z) {
                for (int y = -iR; y <= iR; ++y) {
                    if (java.lang.Math.abs(y) <= heightLimit) {
                        Block curBlock = loc.getWorld().getBlockAt((int)(loc.getX() + x), (int)(loc.getY() + y), (int)(loc.getZ() + z));
                        double offset = Mathematics.offset(loc, curBlock.getLocation().add(0.5, 0.5, 0.5));
                        if (offset <= dR) {
                            blockList.put(curBlock, 1.0 - offset / dR);
                        }
                    }
                }
            }
        }

        return blockList;
    }

    public static HashMap<Block, Double> getInRadius(Block block, double dR) {
        HashMap<Block, Double> blockList = new HashMap<>();

        for (int iR = (int)dR + 1, x = -iR; x <= iR; ++x) {
            for (int z = -iR; z <= iR; ++z) {
                for (int y = -iR; y <= iR; ++y) {
                    Block curBlock = block.getRelative(x, y, z);
                    double offset = Mathematics.offset(block.getLocation(), curBlock.getLocation());

                    if (offset <= dR) {
                        blockList.put(curBlock, 1.0 - offset / dR);
                    }
                }
            }
        }

        return blockList;
    }

    public static boolean isBlock(ItemStack item) {
        return item != null && item.getTypeId() > 0 && item.getTypeId() < 256;
    }

    public static Block getHighest(Location locaton) {
        return getHighest(locaton, null);
    }

    public static Block getHighest(Location location, HashSet<Material> ignore) {
        location.setY(0.0);

        for (int i = 0; i < 256; ++i) {
            location.setY((double)(256 - i));

            if (solid(location.getBlock())) {
                break;
            }
        }

        return location.getBlock().getRelative(BlockFace.UP);
    }

    public static boolean isInAir(Player player) {
        boolean nearBlocks = false;

        for (Block block : getSurrounding(player.getLocation().getBlock(), true)) {
            if (block.getType() != Material.AIR) {
                nearBlocks = true;
                break;
            }
        }

        return nearBlocks;
    }

    public static Set<Material> getSurrounding(PacketedMovementEvent event, Vector location) {
        Set<Material> materials = new HashSet<Material>();

        for (int x = -1; x <= 1; ++x) {
            for (int y = -1; y <= 1; ++y) {
                for (int z = -1; z <= 1; ++z) {
                    materials.add(event.getBlockAt(location.clone().add(new Vector(x, y, z))).getItemType());
                }
            }
        }

        return materials;
    }

    public static ArrayList<Block> getSurrounding(Block block, boolean diagonals) {
        ArrayList<Block> blocks = new ArrayList<>();

        if (diagonals) {
            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    for (int z = -1; z <= 1; ++z) {
                        if (x != 0 || y != 0 || z != 0) {
                            blocks.add(block.getRelative(x, y, z));
                        }
                    }
                }
            }
        }
        else {
            blocks.add(block.getRelative(BlockFace.UP));
            blocks.add(block.getRelative(BlockFace.DOWN));
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }

        return blocks;
    }

    public static ArrayList<Block> getSurroundingXZ(Block block) {
        ArrayList<Block> blocks = new ArrayList<>();

        blocks.add(block.getRelative(BlockFace.NORTH));
        blocks.add(block.getRelative(BlockFace.NORTH_EAST));
        blocks.add(block.getRelative(BlockFace.NORTH_WEST));
        blocks.add(block.getRelative(BlockFace.SOUTH));
        blocks.add(block.getRelative(BlockFace.SOUTH_EAST));
        blocks.add(block.getRelative(BlockFace.SOUTH_WEST));
        blocks.add(block.getRelative(BlockFace.EAST));
        blocks.add(block.getRelative(BlockFace.WEST));

        return blocks;
    }

    public static String serializeLocation(Location location) {
        int x = (int) location.getX();
        int y = (int) location.getY();
        int z = (int) location.getZ();
        int pitch = (int) location.getPitch();
        int yaw = (int) location.getYaw();

        return location.getWorld().getName() + "," + x + "," + y + "," + z + "," + pitch + "," + yaw;
    }

    public static Location deserializeLocation(String string) {
        if (string == null) {
            return null;
        }

        String[] parts = string.split(",");

        World world = Bukkit.getServer().getWorld(parts[0]);
        Double LX = Double.parseDouble(parts[1]);
        Double LY = Double.parseDouble(parts[2]);
        Double LZ = Double.parseDouble(parts[3]);
        Float P = Float.parseFloat(parts[4]);
        Float Y = Float.parseFloat(parts[5]);

        Location result = new Location(world, LX, LY, LZ);

        result.setPitch(P);
        result.setYaw(Y);

        return result;
    }

    public static boolean isVisible(Block block) {
        for (Block other : getSurrounding(block, false)) {
            if (!other.getType().isOccluding()) {
                return true;
            }
        }

        return false;
    }

    static {
        INSTANT_BREAK.add(Material.RED_MUSHROOM);
        INSTANT_BREAK.add(Material.RED_ROSE);
        INSTANT_BREAK.add(Material.BROWN_MUSHROOM);
        INSTANT_BREAK.add(Material.YELLOW_FLOWER);
        INSTANT_BREAK.add(Material.REDSTONE);
        INSTANT_BREAK.add(Material.REDSTONE_TORCH_OFF);
        INSTANT_BREAK.add(Material.REDSTONE_TORCH_ON);
        INSTANT_BREAK.add(Material.REDSTONE_WIRE);
        INSTANT_BREAK.add(Material.LONG_GRASS);
        INSTANT_BREAK.add(Material.PAINTING);
        INSTANT_BREAK.add(Material.WHEAT);
        INSTANT_BREAK.add(Material.SUGAR_CANE);
        INSTANT_BREAK.add(Material.SUGAR_CANE_BLOCK);
        INSTANT_BREAK.add(Material.DIODE);
        INSTANT_BREAK.add(Material.DIODE_BLOCK_OFF);
        INSTANT_BREAK.add(Material.DIODE_BLOCK_ON);
        INSTANT_BREAK.add(Material.SAPLING);
        INSTANT_BREAK.add(Material.TORCH);
        INSTANT_BREAK.add(Material.CROPS);
        INSTANT_BREAK.add(Material.SNOW);
        INSTANT_BREAK.add(Material.TNT);
        INSTANT_BREAK.add(Material.POTATO);
        INSTANT_BREAK.add(Material.CARROT);
        INTERACTIVE.add(Material.STONE_BUTTON);
        INTERACTIVE.add(Material.LEVER);
        INTERACTIVE.add(Material.CHEST);
        FOOD.add(Material.COOKED_BEEF);
        FOOD.add(Material.COOKED_CHICKEN);
        FOOD.add(Material.COOKED_FISH);
        FOOD.add(Material.GRILLED_PORK);
        FOOD.add(Material.PORK);
        FOOD.add(Material.MUSHROOM_SOUP);
        FOOD.add(Material.RAW_BEEF);
        FOOD.add(Material.RAW_CHICKEN);
        FOOD.add(Material.RAW_FISH);
        FOOD.add(Material.APPLE);
        FOOD.add(Material.GOLDEN_APPLE);
        FOOD.add(Material.MELON);
        FOOD.add(Material.COOKIE);
        FOOD.add(Material.BREAD);
        FOOD.add(Material.SPIDER_EYE);
        FOOD.add(Material.ROTTEN_FLESH);
        FOOD.add(Material.POTATO_ITEM);
        COMBO.put(Material.SHEARS, Material.WOOL);
        COMBO.put(Material.IRON_SWORD, Material.WEB);
        COMBO.put(Material.DIAMOND_SWORD, Material.WEB);
        COMBO.put(Material.STONE_SWORD, Material.WEB);
        COMBO.put(Material.WOOD_SWORD, Material.WEB);
    }

}