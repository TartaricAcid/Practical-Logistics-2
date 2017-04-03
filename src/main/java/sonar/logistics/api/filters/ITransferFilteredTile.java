package sonar.logistics.api.filters;

import sonar.core.helpers.FluidHelper.ITankFilter;
import sonar.logistics.api.nodes.BlockConnection;
import sonar.logistics.api.nodes.IConnectionNode;
import sonar.logistics.api.nodes.NodeTransferMode;
import sonar.logistics.api.nodes.TransferType;

public interface ITransferFilteredTile extends IFilteredTile, IConnectionNode, ITankFilter {

	public NodeTransferMode getTransferMode();
	
	/**if this can transfer the given transfer type. this doesn't disable other nodes transferring to it though, Unless connection is disabled is on.*/
	public boolean isTransferEnabled(TransferType type);
	
	public void setTransferType(TransferType type, boolean enable);
	
	public BlockConnection getConnected();
	
	public boolean canConnectToNodeConnection();
	
	public void incrementTransferMode();
}