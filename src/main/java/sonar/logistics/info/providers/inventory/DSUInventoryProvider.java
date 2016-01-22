package sonar.logistics.info.providers.inventory;

import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;
import sonar.core.inventory.StoredItemStack;
import sonar.logistics.api.providers.InventoryProvider;

public class DSUInventoryProvider extends InventoryProvider {

	public static String name = "DSU-Inventory";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean canProvideItems(World world, int x, int y, int z, ForgeDirection dir) {
		TileEntity tile = world.getTileEntity(x, y, z);
		return tile != null && tile instanceof IDeepStorageUnit;
	}

	@Override
	public boolean getItems(List<StoredItemStack> storedStacks, World world, int x, int y, int z, ForgeDirection dir) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof IDeepStorageUnit) {
			IDeepStorageUnit inv = (IDeepStorageUnit) tile;
			if (inv.getStoredItemType() != null) {
				storedStacks.add(new StoredItemStack(inv.getStoredItemType()));
				return true;
			}
		}
		return false;

	}
}