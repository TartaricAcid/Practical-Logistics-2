package sonar.logistics.api.wireless;

import sonar.core.helpers.FontHelper;
import sonar.core.utils.Localisation;
import sonar.logistics.PL2;
import sonar.logistics.PL2Translate;

public enum DataEmitterSecurity {
	PUBLIC(PL2Translate.DATA_EMITTER_PUBLIC), PRIVATE(PL2Translate.DATA_EMITTER_PRIVATE);
	public Localisation l;

	DataEmitterSecurity(Localisation l) {
		this.l = l;
	}

	public String getClientName() {
		return l.t();
	}
}
