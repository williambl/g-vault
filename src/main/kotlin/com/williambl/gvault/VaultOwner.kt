package com.williambl.gvault

import net.minecraft.inventory.Inventory

interface VaultOwner {
    fun getVault(index: Int): Inventory
}