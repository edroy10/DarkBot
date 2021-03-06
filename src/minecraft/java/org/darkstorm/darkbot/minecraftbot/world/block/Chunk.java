package org.darkstorm.darkbot.minecraftbot.world.block;

import java.util.*;

import org.darkstorm.darkbot.minecraftbot.events.EventManager;
import org.darkstorm.darkbot.minecraftbot.events.world.BlockChangeEvent;
import org.darkstorm.darkbot.minecraftbot.world.*;

public final class Chunk {
	private final World world;
	private final ChunkLocation location;
	private final byte[] blocks, metadata, light, skylight;// add, biomes;
	private final Map<BlockLocation, TileEntity> tileEntities;

	public Chunk(World world, ChunkLocation location, byte[] blocks,
			byte[] metadata, byte[] light, byte[] skylight) {
		this.world = world;
		this.location = location;
		this.blocks = blocks;
		this.metadata = metadata;
		this.light = light;
		this.skylight = skylight;
		tileEntities = new HashMap<BlockLocation, TileEntity>();
	}

	public World getWorld() {
		return world;
	}

	public ChunkLocation getLocation() {
		return location;
	}

	public TileEntity getTileEntityAt(int x, int y, int z) {
		return getTileEntityAt(new BlockLocation(x, y, z));
	}

	public TileEntity getTileEntityAt(BlockLocation location) {
		synchronized(tileEntities) {
			return tileEntities.get(location);
		}
	}

	public int getBlockIdAt(BlockLocation location) {
		return getBlockIdAt(location.getX(), location.getY(), location.getZ());
	}

	public int getBlockIdAt(int x, int y, int z) {
		int index = y << 8 | z << 4 | x;
		if(index < 0 || index > blocks.length)
			return 0;
		return blocks[index];
	}

	public void setBlockIdAt(int id, BlockLocation location) {
		setBlockIdAt(id, location.getX(), location.getY(), location.getZ());
	}

	public void setBlockIdAt(int id, int x, int y, int z) {
		int index = y << 8 | z << 4 | x;
		if(index < 0 || index > blocks.length)
			return;
		BlockLocation location = new BlockLocation((this.location.getX() * 16)
				+ x, (this.location.getY() * 16) + y,
				(this.location.getZ() * 16) + z);
		Block oldBlock = blocks[index] != 0 ? new Block(world, this, location,
				blocks[index], metadata[index]) : null;
		blocks[index] = (byte) id;
		Block newBlock = id != 0 ? new Block(world, this, location, id,
				metadata[index]) : null;
		EventManager eventManager = world.getBot().getEventManager();
		eventManager.sendEvent(new BlockChangeEvent(world, location, oldBlock,
				newBlock));
	}

	public int getBlockMetadataAt(BlockLocation location) {
		return getBlockMetadataAt(location.getX(), location.getY(),
				location.getZ());
	}

	public int getBlockMetadataAt(int x, int y, int z) {
		int index = (x << 8) + (y << 4) + z;
		if(index < 0 || index > metadata.length)
			return 0;
		return metadata[index];
	}

	public void setBlockMetadataAt(int metadata, BlockLocation location) {
		setBlockMetadataAt(metadata, location.getX(), location.getY(),
				location.getZ());
	}

	public void setBlockMetadataAt(int metadata, int x, int y, int z) {
		int index = (x << 8) + (y << 4) + z;
		if(index < 0 || index > this.metadata.length)
			return;
		BlockLocation location = new BlockLocation((this.location.getX() * 16)
				+ x, (this.location.getY() * 16) + y,
				(this.location.getZ() * 16) + z);
		Block oldBlock = new Block(world, this, location, blocks[index],
				this.metadata[index]);
		this.metadata[index] = (byte) metadata;
		Block newBlock = new Block(world, this, location, blocks[index],
				metadata);
		EventManager eventManager = world.getBot().getEventManager();
		eventManager.sendEvent(new BlockChangeEvent(world, location, oldBlock,
				newBlock));
	}

	public int getBlockLightAt(BlockLocation location) {
		return getBlockLightAt(location.getX(), location.getY(),
				location.getZ());
	}

	public int getBlockLightAt(int x, int y, int z) {
		int index = (x << 8) + (y << 4) + z;
		if(index < 0 || index > light.length)
			return 0;
		return light[index];
	}

	public int getBlockSkylightAt(BlockLocation location) {
		return getBlockSkylightAt(location.getX(), location.getY(),
				location.getZ());
	}

	public int getBlockSkylightAt(int x, int y, int z) {
		int index = (x << 8) + (y << 4) + z;
		if(index < 0 || index > skylight.length)
			return 0;
		return skylight[index];
	}
}
