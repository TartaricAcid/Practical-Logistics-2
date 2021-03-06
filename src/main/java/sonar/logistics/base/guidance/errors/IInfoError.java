package sonar.logistics.base.guidance.errors;

import sonar.core.api.nbt.INBTSyncable;
import sonar.logistics.api.core.tiles.displays.info.IInfo;
import sonar.logistics.api.core.tiles.displays.info.InfoUUID;

import java.util.List;

public interface IInfoError extends INBTSyncable {

	List<String> getDisplayMessage();
	
	boolean canDisplayInfo(IInfo info);	
	
	List<InfoUUID> getAffectedUUIDs();
	
	default boolean canCombine(IInfoError error){
		return false;
	}
	
	/**if {@link #canCombine(IInfoError)} returns true this should be implemented*/
	default void addError(IInfoError error){}

	/**if {@link #canCombine(IInfoError)} returns true this should be implemented*/
	default void removeError(IInfoError error){}
	
	boolean isValid();
}
