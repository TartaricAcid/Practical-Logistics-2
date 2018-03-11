package sonar.logistics.client.gui.textedit;

import sonar.logistics.api.displays.elements.text.StyledStringLine;

public interface ILineCounter {

	public int getLineCount();
	
	public int getLineLength(int line);
	
	public int getLineWidth(int line);
	
	public String getUnformattedLine(int line);
	
	public String getFormattedLine(int line);	

	public StyledStringLine getLine(int line);
	
}
