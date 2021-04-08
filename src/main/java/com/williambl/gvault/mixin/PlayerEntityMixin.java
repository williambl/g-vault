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
    List<Inventory> vaultInventories = UtilsKt.createVaultInventoryList();

    @Inject(method = "readCustomDataFromTag", at=@At("TAIL"))
    void gVault$readVaultDataFromTag(CompoundTag tag, CallbackInfo ci) {
        ListTag vaultTag = tag.getList("GVaults", 9);
        for (int i = 0; i < vaultInventories.size(); i++) {
            UtilsKt.fromTag(vaultInventories.get(i), vaultTag.getList(i));
        }
    }

    @Inject(method = "writeCustomDataToTag", at=@At("TAIL"))
    void gVault$writeVaultDataToTag(CompoundTag tag, CallbackInfo ci) {
        ListTag vaultTag = new ListTag();
        for (int i = 0; i < vaultInventories.size(); i++) {
            vaultTag.add(i, UtilsKt.toTag(vaultInventories.get(i)));
        }
        tag.put("GVaults", vaultTag);
    }

    @NotNull
    @Override
    public Inventory getVault(int index) {
        return vaultInventories.get(index);
    }
}
