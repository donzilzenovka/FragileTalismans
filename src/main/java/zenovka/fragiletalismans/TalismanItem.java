package zenovka.fragiletalismans;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class TalismanItem extends Item implements IBauble {
    private final boolean wearable;
    private final String kind;

    public TalismanItem(String registryName, String texture, boolean wearable) {
        this.wearable = wearable;
        this.kind = texture;
        setUnlocalizedName(registryName);
        setTextureName("talismans2:" + texture);
        setMaxStackSize(1);
        if (wearable && FragileTalismans.maxDurability > 0) setMaxDamage(FragileTalismans.maxDurability);
    }

    public String getKind() { return kind; }

    @Override public BaubleType getBaubleType(ItemStack stack) { return BaubleType.AMULET; }

    @Override public void onWornTick(ItemStack stack, EntityLivingBase player) {
        if (player.worldObj.isRemote) return;
        if ("Witherless".equals(kind)) {
            player.removePotionEffect(Potion.wither.id);
            return;
        }
        if ("Movement".equals(kind)) {
            apply(player, Potion.moveSpeed);
            apply(player, Potion.jump);
        } else if ("Flame".equals(kind)) apply(player, Potion.fireResistance);
        else if ("Water".equals(kind)) apply(player, Potion.waterBreathing);
        else if ("Light".equals(kind)) apply(player, Potion.nightVision);
        else if ("Mining".equals(kind)) apply(player, Potion.digSpeed);
        else if ("Invisible".equals(kind)) apply(player, Potion.invisibility);
    }

    private static void apply(EntityLivingBase player, Potion potion) {
        // Short effects expire naturally after breakage and do not erase stronger effects.
        PotionEffect current = player.getActivePotionEffect(potion);
        if (current == null || (current.getAmplifier() == 0 && current.getDuration() < 30)) {
            player.addPotionEffect(new PotionEffect(potion.id, 60, 0, true));
        }
    }

    @Override public void onEquipped(ItemStack stack, EntityLivingBase player) { }
    @Override public void onUnequipped(ItemStack stack, EntityLivingBase player) {
        // Short-duration effects expire within three seconds, even on forced removal.
    }
    @Override public boolean canEquip(ItemStack stack, EntityLivingBase player) { return wearable; }
    @Override public boolean canUnequip(ItemStack stack, EntityLivingBase player) { return true; }
}
