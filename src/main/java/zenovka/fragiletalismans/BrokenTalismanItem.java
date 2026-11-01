package zenovka.fragiletalismans;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import java.util.List;

/**
 * Non-equippable remnants. Metadata records the original talisman type.
 * Keep these metadata values stable for save compatibility.
 */
public final class BrokenTalismanItem extends Item {
    private static final String[] TYPES = {
        "Flame", "Water", "Light", "Movement", "Mining", "Invisible", "Witherless"
    };
    private final IIcon[] icons = new IIcon[TYPES.length];

    public BrokenTalismanItem() {
        setUnlocalizedName("BrokenTalisman");
        setHasSubtypes(true);
        setMaxDamage(0);
        setMaxStackSize(64);
    }

    public static int typeOf(ItemStack original) {
        if (original == null || !(original.getItem() instanceof TalismanItem)) return -1;
        String type = ((TalismanItem) original.getItem()).getKind();
        for (int i = 0; i < TYPES.length; i++) {
            if (TYPES[i].equals(type)) return i;
        }
        return -1;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        return meta >= 0 && meta < TYPES.length
            ? "item.Broken" + TYPES[meta] + "Talisman"
            : "item.BrokenTalisman";
    }

    @Override
    public void registerIcons(IIconRegister register) {
        for (int i = 0; i < TYPES.length; i++) {
            icons[i] = register.registerIcon("talismans2:" + TYPES[i] + "_Crack");
        }
        itemIcon = icons[0];
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return meta >= 0 && meta < icons.length ? icons[meta] : itemIcon;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < TYPES.length; i++) list.add(new ItemStack(item, 1, i));
    }
}
