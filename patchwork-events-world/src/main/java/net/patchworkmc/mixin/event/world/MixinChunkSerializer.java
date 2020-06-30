package net.patchworkmc.mixin.event.world;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.ChunkTickScheduler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.poi.PointOfInterestStorage;

@Mixin(ChunkSerializer.class)
public class MixinChunkSerializer {
	@Inject(at = @At(value = "NEW", target = "net/minecraft/world/chunk/ReadOnlyChunk"), method = "deserialize", locals = LocalCapture.CAPTURE_FAILHARD)
	private static void loadChunkData(ServerWorld serverWorld, StructureManager structureManager, PointOfInterestStorage pointOfInterestStorage, ChunkPos chunkPos, CompoundTag compoundTag, CallbackInfoReturnable<ProtoChunk> cir, ChunkGenerator<?> chunkGenerator, BiomeSource biomeSource, CompoundTag levelTag, Biome[] biomes,
			UpgradeData upgradeData, ChunkTickScheduler<?> chunkTickScheduler, ChunkTickScheduler<?> chunkTickScheduler2, boolean bl, ListTag listTag, int k, ChunkSection[] chunkSections, boolean bl2, ChunkManager chunkManager, LightingProvider lightingProvider, long n, ChunkStatus.ChunkType chunkType, Chunk chunk) {
		MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Load(chunk, levelTag, chunkType));
	}
}
