package sonar.logistics.client.gui;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import sonar.core.client.gui.GuiHelpOverlay;
import sonar.core.helpers.FontHelper;
import sonar.core.network.FlexibleGuiHandler;
import sonar.logistics.PL2Translate;
import sonar.logistics.api.info.IProvidableInfo;
import sonar.logistics.client.HelpOverlays;
import sonar.logistics.client.LogisticsButton;
import sonar.logistics.client.LogisticsColours;
import sonar.logistics.client.gui.generic.GuiSelectionList;
import sonar.logistics.common.containers.ContainerInfoReader;
import sonar.logistics.common.multiparts.InfoReaderPart;
import sonar.logistics.connections.monitoring.MonitoredList;
import sonar.logistics.helpers.InfoRenderer;

public class GuiInfoReader extends GuiSelectionList<IProvidableInfo> {

	public InfoReaderPart part;
	public EntityPlayer player;

	public GuiHelpOverlay<GuiInfoReader> overlay = HelpOverlays.infoReader;

	public GuiInfoReader(EntityPlayer player, InfoReaderPart tile) {
		super(new ContainerInfoReader(player, tile), tile);
		this.player = player;
		this.part = tile;
		this.xSize = 182 + 66;
	}

	public void initGui() {
		super.initGui();
		overlay.initGui(this);
		this.buttonList.add(new LogisticsButton(this, 1, guiLeft + 9, guiTop + 7, 32, 96 + 16, "Channels", "button.Channels"));
		this.buttonList.add(new LogisticsButton(this, 2, guiLeft + xSize - 9 - 16, guiTop + 7, 32, 160 + 32 + (GuiHelpOverlay.enableHelp ? 16 : 0), "Help Enabled: " + GuiHelpOverlay.enableHelp, "button.HelpButton"));
	}

	public void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if (button != null) {
			if (button.id == 1) {
				FlexibleGuiHandler.changeGui(part, 1, 0, player.getEntityWorld(), player);
			}
			if (button.id == 2) {
				GuiHelpOverlay.enableHelp = !GuiHelpOverlay.enableHelp;
				reset();
			}
		}
	}

	@Override
	public void mouseClicked(int x, int y, int button) throws IOException {
		super.mouseClicked(x, y, button);
		overlay.mouseClicked(this, x, y, button);
	}

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		super.drawGuiContainerForegroundLayer(x, y);
		FontHelper.textCentre(PL2Translate.INFO_READER.t(), xSize, 6, LogisticsColours.white_text);
		FontHelper.textCentre(String.format("Select the data you wish to monitor"), xSize, 18, LogisticsColours.grey_text);
		overlay.drawOverlay(this, x, y);
	}

	public void setInfo() {
		if (!part.getChannels().hasChannels()) {
			infoList = MonitoredList.newMonitoredList(part.getNetworkID());
		} else {
			infoList = part.getMonitoredList().cloneInfo();
		}
	}

	@Override
	public void selectionPressed(GuiButton button, int infoPos, int buttonID, IProvidableInfo info) {
		if (info.isValid() && !info.isHeader()) {
			part.selectedInfo.setInfo(info);
			part.sendByteBufPacket(buttonID == 0 ? -9 : -10);
		}
	}

	@Override
	public boolean isCategoryHeader(IProvidableInfo info) {
		return info.isHeader();
	}

	@Override
	public boolean isSelectedInfo(IProvidableInfo info) {
		if (!info.isValid() || info.isHeader()) {
			return false;
		}
		ArrayList<IProvidableInfo> selectedInfo = part.getSelectedInfo();
		for (IProvidableInfo selected : selectedInfo) {
			if (selected != null && !selected.isHeader() && info.isMatchingType(selected) && info.isMatchingInfo((IProvidableInfo) selected)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isPairedInfo(IProvidableInfo info) {
		if (!info.isValid() || info.isHeader()) {
			return false;
		}
		ArrayList<IProvidableInfo> pairedInfo = part.getPairedInfo();
		for (IProvidableInfo selected : pairedInfo) {
			if (selected != null && !selected.isHeader() && info.isMatchingType(selected) && info.isMatchingInfo((IProvidableInfo) selected)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void renderInfo(IProvidableInfo info, int yPos) {
		InfoRenderer.renderMonitorInfoInGUI(info, yPos + 1, LogisticsColours.white_text.getRGB());
	}

	@Override
	public int getColour(int i, int type) {
		IProvidableInfo info = (IProvidableInfo) infoList.get(i + start);
		if (info == null || info.isHeader()) {
			return LogisticsColours.layers[1].getRGB();
		}
		ArrayList<IProvidableInfo> selectedInfo = type == 0 ? part.getSelectedInfo() : part.getPairedInfo();
		int pos = 0;
		for (IProvidableInfo selected : selectedInfo) {
			if (selected != null && !selected.isHeader() && info.isMatchingType(selected) && info.isMatchingInfo(selected)) {
				return LogisticsColours.infoColours[pos].getRGB();
			}
			pos++;
		}
		return LogisticsColours.layers[1].getRGB();
	}

	@Override
	public void keyTyped(char c, int i) throws IOException {
		super.keyTyped(c, i);
		overlay.onTileChanged(this);
	}

}
