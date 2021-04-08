package com.williambl.gvault.mixin;

import com.williambl.gvault.UtilsKt;
import com.williambl.gvault.VaultOwner;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements VaultOwner {
    @Unique
    private List<Inventory> vaultInventories = null;
    @Unique
    private List<Inventory> getVaultInventories() {
        if (vaultInventories == null)
            vaultInventories = UtilsKt.createVaultInventoryList((PlayerEntity) (Object) this);
        return vaultInventories;
    }

    @Inject(method = "readCustomDataFromTag", at=@At("TAIL"))
    void gVault$readVaultDataFromTag(CompoundTag tag, CallbackInfo ci) {
        ListTag vaultTag = tag.getList("GVaults", 9);
        for (int i = 0; i < getVaultInventories().size(); i++) {
            UtilsKt.fromTag(getVaultInventories().get(i), vaultTag.getList(i));
        }
    }

    @Inject(method = "writeCustomDataToTag", at=@At("TAIL"))
    void gVault$writeVaultDataToTag(CompoundTag tag, CallbackInfo ci) {
        ListTag vaultTag = new ListTag();
        for (int i = 0; i < getVaultInventories().size(); i++) {
            vaultTag.add(i, UtilsKt.toTag(getVaultInventories().get(i)));
        }
        tag.put("GVaults", vaultTag);
    }

    @NotNull
    @Override
    public Inventory getVault(int index) {
        List<Inventory> inventories = getVaultInventories();
        if (index >= inventories.size()) {
            vaultInventories = UtilsKt.enlargeVaultInventoryList((PlayerEntity) (Object) this, inventories);
        }
        return getVaultInventories().get(index);
    }
}
