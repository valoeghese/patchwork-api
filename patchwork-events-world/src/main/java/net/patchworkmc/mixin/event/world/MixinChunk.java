package net.patchworkmc.mixin.event.world;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.chunk.Chunk;

import net.patchworkmc.impl.event.world.ForgeWorldSupplier;

@Mixin(Chunk.class)
public interface MixinChunk extends ForgeWorldSupplier {
}
