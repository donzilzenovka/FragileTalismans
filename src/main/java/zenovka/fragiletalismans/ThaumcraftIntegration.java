package zenovka.fragiletalismans;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

/** Loaded only when Thaumcraft is present and integration is enabled. */
public final class ThaumcraftIntegration {
    private ThaumcraftIntegration() {}

    public static void register(Item[] items) {
        if (items[0] == null) return;
        final String category = "FRAGILE_TALISMANS";
        ResearchCategories.registerCategory(category,
                new ResourceLocation("thaumcraft", "textures/items/wandcasting.png"),
                new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png"));

        CrucibleRecipe blank = ThaumcraftApi.addCrucibleRecipe("Blank_Talisman",
                new ItemStack(items[0]), new ItemStack(Blocks.stone),
                new AspectList().add(Aspect.MAGIC, 12).add(Aspect.AIR, 10));
        new ResearchItem("Blank_Talisman", category,
                new AspectList().add(Aspect.MAGIC, 3).add(Aspect.AIR, 3),
                0, 0, 1, new ItemStack(items[0]))
                .setPages(new ResearchPage("ft.research.blank"), new ResearchPage(blank))
                .registerResearchItem();

        // Original progression: blank talisman in the centre, instability 5,
        // 15 Praecantatio and 15 of the corresponding aspect.
        registerInfusion(items, 1, "Flame", Aspect.FIRE, 2, -2,
                new ItemStack(Items.flint_and_steel), new ItemStack(Items.flint_and_steel, 1, 1),
                new ItemStack(Items.blaze_powder), new ItemStack(Items.coal));
        registerInfusion(items, 2, "Water", Aspect.WATER, 4, -1,
                new ItemStack(Items.water_bucket), new ItemStack(Items.fishing_rod),
                new ItemStack(Items.fish), new ItemStack(Items.boat));
        registerInfusion(items, 3, "Light", Aspect.LIGHT, 6, 0,
                new ItemStack(Items.glowstone_dust), new ItemStack(Items.glowstone_dust),
                new ItemStack(Items.golden_carrot));
        registerInfusion(items, 4, "Movement", Aspect.MOTION, 4, 2,
                new ItemStack(Items.sugar), new ItemStack(Items.feather),
                new ItemStack(Items.rabbit_foot), new ItemStack(Items.leather_boots));
        registerInfusion(items, 5, "Mining", Aspect.TOOL, 2, 3,
                new ItemStack(Items.iron_pickaxe), new ItemStack(Items.diamond_pickaxe),
                new ItemStack(Items.redstone), new ItemStack(Items.iron_ingot));
        registerInfusion(items, 6, "Invisible", Aspect.SENSES, -2, 3,
                new ItemStack(Items.fermented_spider_eye), new ItemStack(Items.ender_pearl),
                new ItemStack(Items.glass_bottle), new ItemStack(Items.ghast_tear));
        registerInfusion(items, 7, "Witherless", Aspect.HEAL, -4, 1,
                new ItemStack(Items.milk_bucket), new ItemStack(Items.golden_apple),
                new ItemStack(Items.nether_star), new ItemStack(Items.gunpowder));
    }

    private static void registerInfusion(Item[] items, int index, String name,
                                         Aspect aspect, int x, int y, ItemStack... components) {
        if (items[index] == null) return;
        String key = name + "_Talisman";
        InfusionRecipe recipe = ThaumcraftApi.addInfusionCraftingRecipe(key,
                new ItemStack(items[index]), 5,
                new AspectList().add(Aspect.MAGIC, 15).add(aspect, 15),
                new ItemStack(items[0]), components);
        new ResearchItem(key, "FRAGILE_TALISMANS",
                new AspectList().add(Aspect.MAGIC, 4).add(aspect, 4),
                x, y, 2, new ItemStack(items[index]))
                .setParents("Blank_Talisman")
                .setPages(new ResearchPage("ft.research." + name.toLowerCase()),
                        new ResearchPage(recipe))
                .registerResearchItem();
    }
}
