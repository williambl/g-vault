/*
 * MIT License
 *
 * Copyright (c) 2020 Will BL
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
