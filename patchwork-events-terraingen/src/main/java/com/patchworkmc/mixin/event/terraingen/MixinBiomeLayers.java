/*
 * Minecraft Forge, Patchwork Project
 * Copyright (c) 2016-2020, 2019-2020
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.patchworkmc.mixin.event.terraingen;

import java.util.function.LongFunction;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.WorldTypeEvent.BiomeSize;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

import net.minecraft.world.biome.layer.BiomeLayers;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.minecraft.world.level.LevelGeneratorType;

@Mixin(BiomeLayers.class)
public class MixinBiomeLayers {
	@ModifyConstant(method = "build", constant = @Constant(intValue = 6), slice = @Slice(
			from = @At(value = "CONSTANT", args = "intValue=4"),
			to = @At(value = "CONSTANT", args = "longValue=1000")))
	private static int hookBiomeSize(LevelGeneratorType generatorType, OverworldChunkGeneratorConfig settings, LongFunction<?> contextProvider) {
		System.out.println("e");
		BiomeSize event = new BiomeSize(generatorType, 6);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getNewSize();
	}
}
