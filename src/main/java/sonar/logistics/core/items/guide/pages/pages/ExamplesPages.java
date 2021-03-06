package sonar.logistics.core.items.guide.pages.pages;

import net.minecraft.client.gui.GuiButton;
import sonar.core.helpers.FontHelper;
import sonar.logistics.base.gui.buttons.LogisticsButton;
import sonar.logistics.core.items.guide.GuiGuide;
import sonar.logistics.core.items.guide.pages.elements.Element3DRenderer;
import sonar.logistics.core.items.guide.pages.elements.ElementInfo;
import sonar.logistics.core.items.guide.pages.elements.IGuidePageElement;
import sonar.logistics.core.items.guide.pages.pages.ExampleConfigurations.FurnaceProgress;
import sonar.logistics.core.items.guide.pages.pages.ExampleConfigurations.InventoryExample;

import java.util.List;

public class ExamplesPages extends BaseInfoPage {

	public ExamplesPages(int pageID) {
		super(pageID);
	}

	@Override
	public String getDisplayName() {
		return FontHelper.translate("guide.ExampleConfig.name");
	}

	//// CREATE \\\\

	@Override
	public List<ElementInfo> getPageInfo(GuiGuide gui, List<ElementInfo> pageInfo) {
		pageInfo.add(new ElementInfo("guide.FurnaceProgress.name", new String[0]));
		pageInfo.add(new ElementInfo("guide.ChestMonitoring.name", new String[0]).setRequiresNewPage());
		pageInfo.add(new ElementInfo("guide.WirelessRedstone.name", new String[0]).setRequiresNewPage());
		pageInfo.add(new ElementInfo("guide.MultipleInventory.name", new String[0]).setRequiresNewPage());
		return pageInfo;
	}

	public List<IGuidePageElement> getElements(GuiGuide gui, List<IGuidePageElement> elements) {
		try {
			elements.add(new Element3DRenderer(new FurnaceProgress(), 0, 48, 4, 16, 106, 160));
			elements.add(new Element3DRenderer(new InventoryExample(), 1, 48, 4, 16, 106, 160));
			elements.add(new Element3DRenderer(new ExampleConfigurations.MultipleInventory(), 3, 32, 4, 16, 160, 160));
		} catch (Throwable t) {

		}
		return elements;
	}

	public void initGui(GuiGuide gui, int subPage) {
		super.initGui(gui, subPage);
		int centreX = 13;
		int centreY = 24;
		guideButtons.add(new LogisticsButton(gui, 0, centreX, centreY, 512 - 8, 0, 8, 8, "", ""));
		guideButtons.add(new LogisticsButton(gui, 1, centreX, centreY - 8, 512 - 8, 8, 8, 8, "", ""));
		guideButtons.add(new LogisticsButton(gui, 2, centreX - 8, centreY, 512 - 8, 32, 8, 8, "", ""));
		guideButtons.add(new LogisticsButton(gui, 3, centreX + 8, centreY, 512 - 8, 24, 8, 8, "", ""));
		guideButtons.add(new LogisticsButton(gui, 4, centreX, centreY + 8, 512 - 8, 16, 8, 8, "", ""));
		guideButtons.add(new LogisticsButton(gui, 5, centreX - 8, centreY - 8, 512 - 8, 40, 8, 8, "", ""));
	}

	public void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			Element3DRenderer.canRotate = !Element3DRenderer.canRotate;
			break;
		case 1:
			Element3DRenderer.rotateY += 10;
			if (Element3DRenderer.rotateY >= 360) {
				Element3DRenderer.rotateY = 0;
			}
			break;
		case 2:
			Element3DRenderer.rotate -= 10;
			if (Element3DRenderer.rotate <= 1) {
				Element3DRenderer.rotate = 360;
			}
			break;
		case 3:
			Element3DRenderer.rotate += 10;
			if (Element3DRenderer.rotate >= 360) {
				Element3DRenderer.rotate = 0;
			}
			break;
		case 4:
			Element3DRenderer.rotateY -= 10;
			if (Element3DRenderer.rotateY <= 1) {
				Element3DRenderer.rotateY = 360;
			}
			break;
		case 5:
			Element3DRenderer.rotateY = 0;
			Element3DRenderer.rotate = 180;
			break;

		}
	}
}