package sonar.logistics.core.tiles.misc.hammer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import sonar.core.client.gui.GuiSonarTile;
import sonar.core.helpers.FontHelper;
import sonar.logistics.PL2Constants;
import sonar.logistics.PL2Translate;

public class GuiHammer extends GuiSonarTile {
	public static final ResourceLocation bground = new ResourceLocation(PL2Constants.MODID + ":textures/gui/hammer.png");
	
	public TileEntityHammer entity;
	
	public GuiHammer(EntityPlayer player, TileEntityHammer entity) {
		super(new ContainerHammer(player, entity), entity);
		this.entity = entity;
		this.xSize = 176;
		this.ySize = 143;
	}

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		super.drawGuiContainerForegroundLayer(x, y);
		FontHelper.textCentre(PL2Translate.HAMMER.t(), xSize, 6, 0);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		super.drawGuiContainerBackgroundLayer(var1, var2, var3);
		int l = entity.getProgress() * 23 / entity.getSpeed();
		drawTexturedModalRect(guiLeft + 76, guiTop + 24, 176, 0, l, 16);
	}

	@Override
	public ResourceLocation getBackground() {
		return bground;
	}
}
