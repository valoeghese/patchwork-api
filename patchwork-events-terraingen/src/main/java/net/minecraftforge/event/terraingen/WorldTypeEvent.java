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

package net.minecraftforge.event.terraingen;

import net.minecraftforge.eventbus.api.Event;

import net.minecraft.world.biome.layer.BiomeLayerSampler;
import net.minecraft.world.level.LevelGeneratorType;

public class WorldTypeEvent extends Event {
	private final LevelGeneratorType worldType;

	public WorldTypeEvent(LevelGeneratorType worldType) {
		this.worldType = worldType;
	}

	public LevelGeneratorType getWorldType() {
		return worldType;
	}

	/**
	 * BiomeSize is fired when vanilla Minecraft attempts to generate biomes.<br>
	 * This event is fired during biome generation, in {@link net.minecraft.world.biome.layer.BiomeLayers#build}
	 * <br>
	 * {@link #originalSize} the original size of the Biome. <br>
	 * {@link #newSize} the new size of the biome. Initially set to the {@link #originalSize}. <br>
	 * If {@link #newSize} is set to a new value, that value will be used for the Biome size. <br>
	 * <br>
	 * This event is not {@link Cancelable}.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult} <br>
	 */
	public static class BiomeSize extends WorldTypeEvent {
		private final int originalSize;
		private int newSize;

		public BiomeSize(LevelGeneratorType worldType, int original) {
			super(worldType);
			originalSize = original;
			setNewSize(original);
		}

		public int getOriginalSize() {
			return originalSize;
		}

		public int getNewSize() {
			return newSize;
		}

		public void setNewSize(int newSize) {
			this.newSize = newSize;
		}
	}

	/**
	 * InitBiomeGens is fired when vanilla Minecraft attempts to initialize the biome providers.<br>
	 * This event is fired just during biome provider initialization
	 * <br>
	 * {@link #seed} the seed of the world. <br>
	 * {@link #originalBiomeGens} the array of GenLayers original intended for this Biome generation. <br>
	 * {@link #newBiomeGens} the array of GenLayers that will now be used for this Biome generation. <br>
	 * If {@link #newBiomeGens} is set to a new value, that value will be used for the Biome generator. <br>
	 * <br>
	 * This event is not {@link Cancelable}.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult} <br>
	 */
	public static class InitBiomeGens extends WorldTypeEvent {
		private final long seed;
		private final BiomeLayerSampler[] originalBiomeGens;
		private BiomeLayerSampler[] newBiomeGens;

		public InitBiomeGens(LevelGeneratorType worldType, long seed, BiomeLayerSampler[] original) {
			super(worldType);
			this.seed = seed;
			originalBiomeGens = original;
			setNewBiomeGens(original.clone());
		}

		public long getSeed() {
			return seed;
		}

		public BiomeLayerSampler[] getOriginalBiomeGens() {
			return originalBiomeGens;
		}

		public BiomeLayerSampler[] getNewBiomeGens() {
			return newBiomeGens;
		}

		public void setNewBiomeGens(BiomeLayerSampler[] newBiomeGens) {
			this.newBiomeGens = newBiomeGens;
		}
	}
}
