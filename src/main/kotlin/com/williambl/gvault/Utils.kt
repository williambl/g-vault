package com.williambl.gvault

import com.williambl.gvault.configs.GVaultConfig
import io.github.gunpowder.api.GunpowderMod
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag

fun createVaultInventoryList(): List<Inventory> = List(config.vaultCount) { SimpleInventory(if (config.isDoubleChest) 54 else 27) }

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

fun Inventory.fromTag(tag: ListTag) = tag.asSequence()
    .filterIsInstance<CompoundTag>()
    .filter { it.getByte("Slot") < this.size() }
    .forEach {
        this.setStack(it.getByte("Slot").toInt(), ItemStack.fromTag(it))
    }

fun PlayerEntity.getVault(index: Int): Inventory = (this as VaultOwner).getVault(index)

val config: GVaultConfig
    get() = GunpowderMod.instance.registry.getConfig(GVaultConfig::class.java)
