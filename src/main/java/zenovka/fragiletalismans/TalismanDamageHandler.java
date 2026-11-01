package zenovka.fragiletalismans;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class TalismanDamageHandler {
    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        if (event.isCanceled() || event.entityLiving.worldObj.isRemote ||
                !(event.entityLiving instanceof EntityPlayer) ||
                event.source.isUnblockable() || event.ammount <= 0.0F ||
                FragileTalismans.maxDurability <= 0) return;
        EntityPlayer player = (EntityPlayer) event.entityLiving;
        IInventory baubles = BaublesApi.getBaubles(player);
        if (baubles == null) return;
        int wear = Math.max(1, (int) Math.floor(event.ammount / 4.0F));
        wear = Math.max(1, (int) Math.ceil(wear * FragileTalismans.damageMultiplier));
        for (int slot = 0; slot < baubles.getSizeInventory(); slot++) {
            ItemStack stack = baubles.getStackInSlot(slot);
            if (stack == null || !(stack.getItem() instanceof TalismanItem) ||
                    !stack.isItemStackDamageable()) continue;
            stack.damageItem(wear, player);
            if (stack.stackSize <= 0) {
                ((TalismanItem) stack.getItem()).onUnequipped(stack, player);
                baubles.setInventorySlotContents(slot, null);
            } else {
                baubles.markDirty();
            }
        }
    }
}
