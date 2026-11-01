package zenovka.fragiletalismans;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TalismanItem extends Item implements IBauble {
    private final boolean wearable;

    public TalismanItem(String registryName, String texture, boolean wearable) {
        this.wearable = wearable;
        setUnlocalizedName(registryName.replace(' ', '_'));
        setTextureName("talismans2:" + texture);
        setMaxStackSize(1);
        if (wearable && FragileTalismans.maxDurability > 0) setMaxDamage(FragileTalismans.maxDurability);
    }

    @Override public BaubleType getBaubleType(ItemStack stack) { return BaubleType.AMULET; }
    @Override public void onWornTick(ItemStack stack, EntityLivingBase player) { }
    @Override public void onEquipped(ItemStack stack, EntityLivingBase player) { }
    @Override public void onUnequipped(ItemStack stack, EntityLivingBase player) { }
    @Override public boolean canEquip(ItemStack stack, EntityLivingBase player) { return wearable; }
    @Override public boolean canUnequip(ItemStack stack, EntityLivingBase player) { return true; }
}
