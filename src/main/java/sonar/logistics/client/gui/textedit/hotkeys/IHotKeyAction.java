package sonar.logistics.client.gui.textedit.hotkeys;

import sonar.logistics.client.gui.textedit.GuiEditStyledStrings;

public interface IHotKeyAction {

	public void trigger(GuiEditStyledStrings s, char c, int i);
}