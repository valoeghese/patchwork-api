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

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.WorldTypeEvent.InitBiomeGens;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.biome.layer.BiomeLayerSampler;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSourceConfig;

@Mixin(VanillaLayeredBiomeSource.class)
public class MixinVanillaLayeredBiomeSource {
	@Shadow
	@Final
	@Mutable
	private BiomeLayerSampler noiseLayer;

	@Shadow
	@Final
	@Mutable
	private BiomeLayerSampler biomeLayer;

	@Inject(at = @At("RETURN"), method = "<init>")
	private void hookCreateBiomeLayers(VanillaLayeredBiomeSourceConfig config, CallbackInfo info) {
		InitBiomeGens event = new InitBiomeGens(config.getLevelProperties().getGeneratorType(), 0, new BiomeLayerSampler[] {this.noiseLayer, this.biomeLayer});
		MinecraftForge.EVENT_BUS.post(event);
		BiomeLayerSampler[] alternateBiomeLayers = event.getNewBiomeGens();
		int alternateBiomeLayersLength = alternateBiomeLayers.length;

		if (alternateBiomeLayersLength > 0) {
			if (alternateBiomeLayers[0] != this.noiseLayer) {
				this.noiseLayer = alternateBiomeLayers[0];
			}

			if (alternateBiomeLayersLength > 1) {
				if (alternateBiomeLayers[1] != this.biomeLayer) {
					this.biomeLayer = alternateBiomeLayers[1];
				}
			}
		}
	}
}
