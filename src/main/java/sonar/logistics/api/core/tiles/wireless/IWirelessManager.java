package sonar.logistics.api.core.tiles.wireless;

import net.minecraft.entity.player.EntityPlayer;
import sonar.core.listener.ISonarListenable;
import sonar.core.listener.PlayerListener;
import sonar.logistics.api.core.tiles.wireless.emitters.ClientWirelessEmitter;
import sonar.logistics.api.core.tiles.wireless.emitters.IWirelessEmitter;
import sonar.logistics.api.core.tiles.wireless.receivers.IWirelessReceiver;

import java.util.List;

public interface IWirelessManager<E extends IWirelessEmitter, R extends IWirelessReceiver> extends ISonarListenable<PlayerListener> {

	EnumWirelessConnectionType type();
	
	E getEmitter(int identity);

	R getReceiver(int identity);

	void addViewer(EntityPlayer player);

	void removeViewer(EntityPlayer player);

	void onEmitterSecurityChanged(E emitter, EnumWirelessSecurity oldSetting);

	List<ClientWirelessEmitter> getClientEmitters(EntityPlayer player);
}
