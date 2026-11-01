package zenovka.fragiletalismans;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.config.Configuration;
import java.io.File;

@Mod(modid = FragileTalismans.MODID, name = "Fragile Talismans", version = "0.1.0", dependencies = "required-after:Baubles")
public final class FragileTalismans {
    public static final String MODID = "talismans2";
    public static boolean enableDurability = true;
    public static int maxDurability = 256;
    public static Item brokenTalisman;
    public static double damageMultiplier = 1.0D;
    private static final String[] LOOT_TYPES = {"Blank", "Flame", "Water", "Light", "Movement", "Mining", "Invisible", "Witherless"};
    private static final Item[] lootItems = new Item[LOOT_TYPES.length];
    private static final boolean[] lootEnabled = new boolean[LOOT_TYPES.length];
    private static final int[] lootWeights = new int[LOOT_TYPES.length];
    private static boolean generateLoot = true;
    private static boolean enableThaumcraft = true;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        try {
            config.load();
            // Honour legacy zero-valued settings when upgrading older configurations.
            int requestedDurability = config.get("durability", "maxTalismanDurability", 256,
                    "Maximum durability of talismans (32 to 1024).").getInt(256);
            enableDurability = config.get("durability", "enableDurability", requestedDurability != 0,
                    "Whether equipped talismans lose durability when taking damage.").getBoolean(requestedDurability != 0);
            maxDurability = Math.max(32, Math.min(1024, requestedDurability));
            config.get("durability", "maxTalismanDurability", 256,
                    "Maximum durability of talismans (32 to 1024).").set(maxDurability);
            enableThaumcraft = config.get("integration", "enableThaumcraft", true,
                    "Register Thaumcraft 4 research, crucible and infusion recipes when installed.").getBoolean(true);
            generateLoot = config.get("loot", "enableChestLoot", true,
                    "Add talismans to the original four vanilla chest loot pools.").getBoolean(true);
            for (int i = 0; i < LOOT_TYPES.length; i++) {
                String type = LOOT_TYPES[i];
                lootEnabled[i] = config.get("loot", "enable" + type, true,
                        "Allow " + type + " Talismans to appear in loot chests.").getBoolean(true);
                lootWeights[i] = Math.max(0, config.get("loot", "weight" + type, 7,
                        "Relative chest loot weight for " + type + " Talismans (0 disables).", 0, 1000).getInt(7));
            }
            damageMultiplier = Math.max(0.0D, config.get("durability", "damageMultiplier", 1.0D,
                    "Durability loss multiplier applied to armour-style damage.").getDouble(1.0D));
        } finally {
            if (config.hasChanged()) config.save();
        }
        register("Blank Talisman", "Blank", false);
        register("Flame Talisman", "Flame", true);
        register("Water Talisman", "Water", true);
        register("Light Talisman", "Light", true);
        register("Movement Talisman", "Movement", true);
        register("Mining Talisman", "Mining", true);
        register("Invisible Talisman", "Invisible", true);
        register("Witherless Talisman", "Witherless", true);
        brokenTalisman = new BrokenTalismanItem();
        GameRegistry.registerItem(brokenTalisman, "Broken Talisman");
        // Magnet and Crafting are deliberately withheld pending behaviour audit.
    }

    private static void register(String registryName, String texture, boolean wearable) {
        Item item = new TalismanItem(registryName, texture, wearable);
        GameRegistry.registerItem(item, registryName);
        for (int i = 0; i < LOOT_TYPES.length; i++) {
            if (LOOT_TYPES[i].equals(texture)) lootItems[i] = item;
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new TalismanDamageHandler());
        registerChestLoot();
        if (enableThaumcraft && Loader.isModLoaded("Thaumcraft")) {
            ThaumcraftIntegration.register(lootItems);
        }
    }
    private static void registerChestLoot() {
        if (!generateLoot) return;
        String[] pools = {
            ChestGenHooks.DUNGEON_CHEST,
            ChestGenHooks.MINESHAFT_CORRIDOR,
            ChestGenHooks.PYRAMID_DESERT_CHEST,
            ChestGenHooks.STRONGHOLD_CORRIDOR
        };
        for (int i = 0; i < LOOT_TYPES.length; i++) {
            if (!lootEnabled[i] || lootWeights[i] <= 0 || lootItems[i] == null) continue;
            for (String pool : pools) {
                ChestGenHooks.getInfo(pool).addItem(new WeightedRandomChestContent(
                        new ItemStack(lootItems[i]), 0, 1, lootWeights[i]));
            }
        }
    }
}
