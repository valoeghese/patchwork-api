package net.patchworkmc.mixin.event.world;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.WorldChunk;

import net.patchworkmc.impl.event.world.ForgeWorldSupplier;

@Mixin(WorldChunk.class)
public class MixinWorldChunk implements ForgeWorldSupplier {
	@Override
	public IWorld getWorldForge() {
		return ((WorldChunk) (Object) this).getWorld();
	}
}
