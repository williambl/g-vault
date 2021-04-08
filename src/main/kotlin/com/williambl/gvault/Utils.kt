package com.williambl.gvault

import com.google.common.collect.Lists
import com.williambl.gvault.configs.GVaultConfig
import io.github.gunpowder.api.GunpowderMod
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import java.util.*

fun createVaultInventoryList(player: PlayerEntity): List<Inventory> = List(player.vaultCount) { SimpleInventory(if (player.isVaultDoubleChest) 54 else 27) }

fun enlargeVaultInventoryList(player: PlayerEntity, list: List<Inventory>) = createVaultInventoryList(player).toMutableList().also {
    Collections.copy(it, list)
}

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

val PlayerEntity.vaultCount: Int
    get() = config.configGroups
        .asSequence()
        .filter { Permissions.check(this, "gvault.group.${it.name}") }
        .map { it.vaultCount }
        .sortedDescending()
        .firstOrNull() ?: config.vaultCount

val PlayerEntity.isVaultDoubleChest: Boolean
    get() = config.doubleChest or config.configGroups
        .asSequence()
        .filter { Permissions.check(this, "gvault.group.${it.name}") }
        .map { it.doubleChest }
        .any()

val config: GVaultConfig
    get() = GunpowderMod.instance.registry.getConfig(GVaultConfig::class.java)
