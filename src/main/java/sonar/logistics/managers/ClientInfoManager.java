package sonar.logistics.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import sonar.core.helpers.NBTHelper;
import sonar.core.helpers.NBTHelper.SyncType;
import sonar.logistics.PL2;
import sonar.logistics.api.IInfoManager;
import sonar.logistics.api.info.IMonitorInfo;
import sonar.logistics.api.tiles.displays.ConnectedDisplay;
import sonar.logistics.api.tiles.displays.IDisplay;
import sonar.logistics.api.tiles.displays.ILargeDisplay;
import sonar.logistics.api.tiles.readers.ClientViewable;
import sonar.logistics.api.tiles.readers.IInfoProvider;
import sonar.logistics.api.utils.InfoUUID;
import sonar.logistics.api.utils.MonitoredList;
import sonar.logistics.api.viewers.ILogicListenable;
import sonar.logistics.api.wireless.ClientDataEmitter;
import sonar.logistics.helpers.InfoHelper;

public class ClientInfoManager implements IInfoManager {

	public Map<Integer, ConnectedDisplay> connectedDisplays = new ConcurrentHashMap<Integer, ConnectedDisplay>();
	public List<IDisplay> displays = Lists.newArrayList();

	// public LinkedHashMap<InfoUUID, IMonitorInfo> lastInfo = Maps.newLinkedHashMap();
	public Map<InfoUUID, IMonitorInfo> info = Maps.newLinkedHashMap();

	public Map<Integer, List<Object>> sortedLogicMonitors = new ConcurrentHashMap<Integer, List<Object>>();
	public Map<Integer, List<ClientViewable>> clientLogicMonitors = new ConcurrentHashMap<Integer, List<ClientViewable>>();

	public Map<InfoUUID, MonitoredList<?>> monitoredLists = Maps.newLinkedHashMap();
	public Map<Integer, ILogicListenable> monitors = Maps.newLinkedHashMap();
	public Map<Integer, MonitoredList<IMonitorInfo>> channelMap = new ConcurrentHashMap<Integer, MonitoredList<IMonitorInfo>>();

	// emitters
	public List<ClientDataEmitter> clientEmitters = new ArrayList<ClientDataEmitter>();

	@Override
	public void removeAll() {
		connectedDisplays.clear();
		info.clear();
		sortedLogicMonitors.clear();
		clientLogicMonitors.clear();
		monitoredLists.clear();
		monitors.clear();
		channelMap.clear();
		clientEmitters.clear();
	}

	public void onInfoPacket(NBTTagCompound packetTag, SyncType type) {
		NBTTagList packetList = packetTag.getTagList("infoList", NBT.TAG_COMPOUND);
		boolean save = type.isType(SyncType.SAVE);
		for (int i = 0; i < packetList.tagCount(); i++) {
			NBTTagCompound infoTag = packetList.getCompoundTagAt(i);
			InfoUUID id = NBTHelper.instanceNBTSyncable(InfoUUID.class, infoTag);
			if (save) {
				info.put(id, InfoHelper.readInfoFromNBT(infoTag));
				// info.replace(id, );
			} else {
				IMonitorInfo currentInfo = info.get(id);
				if (currentInfo != null) {
					currentInfo.readData(infoTag, type);
					info.put(id, currentInfo);
				}
			}
		}
	}


	public void addIdentityTile(ILogicListenable infoProvider) {
		if (monitors.containsValue(infoProvider) || infoProvider.getIdentity() == -1) {
			return;
		}
		monitors.put(infoProvider.getIdentity(), infoProvider);
	}

	public void removeIdentityTile(ILogicListenable monitor) {
		monitors.remove(monitor.getIdentity());
	}
	
	public void addDisplay(IDisplay display) {
		if (!displays.contains(display)) {
			displays.add(display);
		}
	}

	public void removeDisplay(IDisplay display) {
		displays.remove(display);
	}
	
	@Override
	public Map<Integer,ILogicListenable> getMonitors() {
		return monitors;
	}

	@Override
	public Map<InfoUUID, IMonitorInfo> getInfoList() {
		return info;
	}
	
	public void onMonitoredListChanged(InfoUUID uuid){
		for(IDisplay display : displays){
			if(display.container().monitorsUUID(uuid)){
				
			}
		}
	}

	public <T extends IMonitorInfo> MonitoredList<T> getMonitoredList(int networkID, InfoUUID uuid) {
		MonitoredList<T> list = MonitoredList.<T>newMonitoredList(networkID);
		monitoredLists.putIfAbsent(uuid, list);
		for (Entry<InfoUUID, MonitoredList<?>> entry : monitoredLists.entrySet()) {
			if (entry.getValue().networkID == networkID && entry.getKey().equals(uuid)) {
				return (MonitoredList<T>) entry.getValue();
			}
		}
		return list;
	}

	public ConnectedDisplay getOrCreateDisplayScreen(World world, ILargeDisplay display, int registryID) {
		Map<Integer, ConnectedDisplay> displays = getConnectedDisplays();
		ConnectedDisplay toSet = displays.get(registryID);
		if (toSet == null) {
			displays.put(registryID, new ConnectedDisplay(display));
			toSet = displays.get(registryID);
		}
		return toSet;
	}

	@Override
	public Map<Integer, ConnectedDisplay> getConnectedDisplays() {
		return connectedDisplays;
	}
}