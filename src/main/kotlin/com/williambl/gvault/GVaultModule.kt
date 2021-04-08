/*
 * MIT License
 *
 * Copyright (c) 2020 GunpowderMC
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

package com.williambl.gvault

import com.google.inject.Inject
import com.mojang.brigadier.arguments.IntegerArgumentType.getInteger
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.context.CommandContext
import com.williambl.gvault.configs.GVaultConfig
import io.github.gunpowder.api.GunpowderMod
import io.github.gunpowder.api.GunpowderModule
import io.github.gunpowder.api.builders.Command
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.command.argument.EntityArgumentType.getPlayer
import net.minecraft.command.argument.EntityArgumentType.player
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText

class GVaultModule : GunpowderModule {
    override val name = "gvault"
    override val toggleable = true
    private val gunpowder: GunpowderMod
        get() = GunpowderMod.instance

    private fun openVault(vaultOwner: PlayerEntity, vaultSeer: PlayerEntity, vaultNumber: Int): Int {
        vaultSeer.openHandledScreen(
            SimpleNamedScreenHandlerFactory(
                { syncId, playerInv, _ ->
                    GenericContainerScreenHandler.createGeneric9x3(
                        syncId,
                        playerInv,
                        vaultOwner.getVault(vaultNumber - 1)
                    )
                },
                LiteralText("Vault $vaultNumber")
            )
        )
        return 1
    }

    override fun registerCommands() = gunpowder.registry.registerCommand { dispatcher ->
        Command.builder(dispatcher) {
            command("vault") {
                requires(Permissions.require("gvault.viewVault", 2)::test)

                argument("vaultNumber", integer(1, config.vaultCount)) {
                    executes { ctx -> openVault(ctx.source.player, ctx.source.player, getInteger(ctx, "vaultNumber")) }
                }

                literal("showto") {
                    requires(Permissions.require("gvault.showToOther", 3)::test)

                    argument("vaultOwner", player()) {
                        argument("vaultNumber", integer(1, config.vaultCount)) {
                            executes { ctx ->
                                val player = getPlayer(ctx, "vaultOwner")
                                openVault(player, player, getInteger(ctx, "vaultNumber"))
                            }
                        }
                    }
                }

                literal("spy") {
                    requires(Permissions.require("gvault.spy", 4)::test)

                    argument("vaultOwner", player()) {
                        argument("vaultNumber", integer(1, config.vaultCount)) {
                            executes { ctx -> openVault(getPlayer(ctx, "vaultOwner"), ctx.source.player, getInteger(ctx, "vaultNumber"))}
                        }
                    }
                }
            }
        }
    }

    override fun registerConfigs() {
        super.registerConfigs()
        gunpowder.registry.registerConfig("gvault.yml", GVaultConfig::class.java, GVaultConfig())
    }
}