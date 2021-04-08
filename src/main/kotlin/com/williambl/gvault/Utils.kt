package com.williambl.gvault

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag

fun createVaultInventoryList(): List<Inventory> = List(128) { SimpleInventory(27) }

fun Inventory.toTag(): Tag {
    return ListTag().also { tag ->
        for (i in 0 until size()) {
            val stack = getStack(i)
            if (!stack.isEmpty) {
                tag.add(CompoundTag().also {
                    it.putByte("Slot", i.toByte())
                    stack.toTag(it)
                })
            }
        }
    }
}

fun Inventory.fromTag(tag: ListTag) = tag.filterIsInstance<CompoundTag>().forEach {
    this.setStack(it.getByte("Slot").toInt(), ItemStack.fromTag(it))
}

fun PlayerEntity.getVault(index: Int): Inventory = (this as VaultOwner).getVault(index)