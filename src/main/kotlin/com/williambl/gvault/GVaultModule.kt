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

import com.mojang.brigadier.arguments.IntegerArgumentType.getInteger
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.gunpowder.api.GunpowderMod
import io.github.gunpowder.api.GunpowderModule
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText

class GVaultModule : GunpowderModule {
    override val name = "gvault"
    override val toggleable = true
    val gunpowder: GunpowderMod
        get() = GunpowderMod.instance

    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, isDedicated ->
            dispatcher.register(literal<ServerCommandSource>("vault").then(
                    argument<ServerCommandSource, Int>("vaultNumber", integer(1, 128)).executes { ctx ->
                        val vaultNumber = getInteger(ctx, "vaultNumber")
                        ctx.source.player.openHandledScreen(SimpleNamedScreenHandlerFactory(
                                { syncId, playerInv, _ -> GenericContainerScreenHandler.createGeneric9x3(syncId, playerInv, ctx.source.player.getVault(vaultNumber-1)) },
                                LiteralText("Vault $vaultNumber")
                        ))
                        1
                    }
            ))
        }
    }
}