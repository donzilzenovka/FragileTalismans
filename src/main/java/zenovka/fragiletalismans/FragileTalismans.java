package zenovka.fragiletalismans;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import java.io.File;

@Mod(modid = FragileTalismans.MODID, name = "Fragile Talismans", version = "0.1.0", dependencies = "required-after:Baubles")
public final class FragileTalismans {
    public static final String MODID = "talismans2";
    public static int maxDurability = 256;
    public static Item brokenTalisman;
    public static double damageMultiplier = 1.0D;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        try {
            config.load();
            maxDurability = config.get("durability", "maxTalismanDurability", 256,
                    "Maximum durability. Set to zero to disable wear.").getInt(256);
            maxDurability = Math.max(0, maxDurability);
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
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new TalismanDamageHandler());
    }
}
