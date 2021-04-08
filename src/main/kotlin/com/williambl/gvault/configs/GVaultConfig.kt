package com.williambl.gvault.configs

data class GVaultConfig(val vaultCount: Int = 3, val doubleChest: Boolean = true, val configGroups: List<ConfigGroup> = listOf())

data class ConfigGroup(val name: String, val vaultCount: Int, val doubleChest: Boolean)
