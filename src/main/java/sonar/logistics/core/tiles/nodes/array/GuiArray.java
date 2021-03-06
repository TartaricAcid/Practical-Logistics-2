package sonar.logistics.core.tiles.nodes.array;

import net.minecraft.entity.player.EntityPlayer;
import sonar.core.helpers.FontHelper;
import sonar.logistics.PL2Translate;
import sonar.logistics.base.gui.GuiLogistics;
import sonar.logistics.base.gui.PL2Colours;

public class GuiArray extends GuiLogistics {
	public TileArray part;

	public GuiArray(EntityPlayer player, TileArray tileArray) {
		super(new ContainerArray(player, tileArray), tileArray);
		this.part = tileArray;
		this.ySize = 132;
	}

	//// DRAWING \\\\

	@Override
	public void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		FontHelper.textCentre(PL2Translate.ARRAY.t(), xSize, 6, PL2Colours.white_text.getRGB());
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		super.drawGuiContainerBackgroundLayer(var1, var2, var3);
		renderPlayerInventory(7, 50);
		drawTexturedModalRect(this.guiLeft + 16, this.guiTop + 19, 0, 0, 18 * 8, 18);
	}
}
