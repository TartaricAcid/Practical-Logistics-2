package sonar.logistics.core.tiles.readers.fluids;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import sonar.core.api.fluids.StoredFluidStack;
import sonar.core.client.gui.GuiHelpOverlay;
import sonar.core.client.gui.SonarTextField;
import sonar.core.network.FlexibleGuiHandler;
import sonar.logistics.PL2Constants;
import sonar.logistics.PL2Translate;
import sonar.logistics.base.gui.GuiSelectionGrid;
import sonar.logistics.base.gui.buttons.LogisticsButton;
import sonar.logistics.core.tiles.displays.info.types.fluids.InfoNetworkFluid;
import sonar.logistics.core.tiles.readers.fluids.FluidReader.Modes;

import java.util.ArrayList;
import java.util.List;

public class GuiFluidReader extends GuiSelectionGrid<InfoNetworkFluid> {

	public static final ResourceLocation stackBGround = new ResourceLocation(PL2Constants.MODID + ":textures/gui/inventoryReader_stack.png");
	public static final ResourceLocation clearBGround = new ResourceLocation(PL2Constants.MODID + ":textures/gui/inventoryReader_clear.png");

	public TileFluidReader part;
	private SonarTextField slotField;
	private SonarTextField searchField;
	public static final int STACK = 0, POS = 1, INV = 2, STORAGE = 3;

	public EntityPlayer player;

	public GuiFluidReader(TileFluidReader tileFluidReader, EntityPlayer player) {
		super(new ContainerFluidReader(tileFluidReader, player), tileFluidReader);
		this.part = tileFluidReader;
		this.player = player;
		this.eWidth = 27;
		this.eHeight = 27;		
		this.gWidth = 8;
		this.gHeight = 5;
	}

	public FluidReader.Modes getSetting() {
		return part.setting.getObject();
	}

	public void initGui() {
		super.initGui();
		initButtons();
		switch (getSetting()) {
		case POS:
			slotField = new SonarTextField(2, this.fontRenderer, 63, 10, 32, 14);
			slotField.setMaxStringLength(7);
			slotField.setText("" + part.posSlot.getObject());
			slotField.setDigitsOnly(true);
			fieldList.add(slotField);
			break;
		default:
			break;
		}
		searchField = new SonarTextField(3, this.fontRenderer, 135, 10, 104, 14);
		searchField.setMaxStringLength(20);
		fieldList.add(searchField);
	}

	public void initButtons() {
		super.initButtons();

		int start = 8;
		this.buttonList.add(new LogisticsButton.CHANNELS(this, 2, guiLeft + 8, guiTop + 9));
		this.buttonList.add(new LogisticsButton.HELP(this, 3, guiLeft + start + 18, guiTop + 9));
		this.buttonList.add(new LogisticsButton(this, -1, guiLeft + start + 18 * 2, guiTop + 9, 128, 16 * part.setting.getObject().ordinal(), getSetting().getClientName(), getSetting().getDescription()));

		this.buttonList.add(new LogisticsButton(this, 0, guiLeft + xSize - 168 + 18, guiTop + 9, 32, 16 * part.sortingOrder.getObject().ordinal(), PL2Translate.BUTTON_SORTING_ORDER.t(), ""));
		this.buttonList.add(new LogisticsButton(this, 1, guiLeft + xSize - 168 + 18 * 2, guiTop + 9, 64 + 48, 16 * part.sortingType.getObject().ordinal(), part.sortingType.getObject().getClientName(), ""));

	}

	public void onTextFieldChanged(SonarTextField field) {
		super.onTextFieldChanged(field);
		if (field == slotField) {
			part.posSlot.setObject(field.getIntegerFromText());
			part.sendByteBufPacket(4);
		}
	}

	public void actionPerformed(GuiButton button) {
		if (button != null) {
			switch (button.id) {
			case -1:
				part.setting.incrementEnum();
				part.sendByteBufPacket(2);
				reset();
				break;
			case 0:
				part.sortingOrder.incrementEnum();
				part.sendByteBufPacket(5);
				initButtons();
				break;
			case 1:
				part.sortingType.incrementEnum();
				part.sendByteBufPacket(6);
				initButtons();
				break;
			case 2:
				FlexibleGuiHandler.changeGui(part, 1, 0, player.getEntityWorld(), player);
				break;
			case 3:
				GuiHelpOverlay.enableHelp = !GuiHelpOverlay.enableHelp;
				reset();
				break;
			}
		}
	}

	@Override
	public List<InfoNetworkFluid> getGridList() {
		String search = searchField.getText();
		if (search == null || search.isEmpty() || search.equals(" "))
			return part.getMonitoredList().createSaveableList(part.getSorter());
		else {
			List<InfoNetworkFluid> searchlist = new ArrayList<>();
			List<InfoNetworkFluid> cached = part.getMonitoredList().createSaveableList(part.getSorter());
			for (InfoNetworkFluid stack : cached) {
				StoredFluidStack fluidStack = stack.getStoredStack();
				if (fluidStack.fluid != null && fluidStack.fluid.getLocalizedName().toLowerCase().contains(searchField.getText().toLowerCase())) {
					searchlist.add(stack);
				}
			}
			return searchlist;
		}
	}

	@Override
	public void onGridClicked(InfoNetworkFluid selection, int x, int y, int pos, int button, boolean empty) {
		if (empty) {
			return;
		}
		if (getSetting() == Modes.SELECTED) {
			part.selected.setInfo(selection);
			part.sendByteBufPacket(1);
		}
		if (getSetting() == Modes.POS) {
			List<InfoNetworkFluid> currentList = getGridList();
			int position = 0;
			for (InfoNetworkFluid stack : currentList) {
				if (stack != null) {
					if (stack.equals(selection)) {
						String posString = String.valueOf(position);
						slotField.setText(posString);
						part.posSlot.setObject(slotField.getIntegerFromText());
						part.sendByteBufPacket(4);
					}
				}
				position++;
			}

		}
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		if (this.getSetting() == FluidReader.Modes.SELECTED) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(playerInv);
			drawTexturedModalRect(guiLeft + 62, guiTop + 8, 0, 0, 18, 18);
		}
		super.drawGuiContainerBackgroundLayer(var1, var2, var3);
	}

	public void preRender() {
		final int br = 16 << 20 | 16 << 4;
		final int var11 = br % 65536;
		final int var12 = br / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var11 * 0.8F, var12 * 0.8F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (getGridList() != null) {
			GlStateManager.disableLighting();
			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		}

	}

	public void postRender() {
		if (this.getSetting() == FluidReader.Modes.SELECTED && part.selected.getMonitoredInfo() != null) {
			InfoNetworkFluid stack = part.selected.getMonitoredInfo();
			if (stack != null) {
				StoredFluidStack fluidStack = stack.getStoredStack();
				final int br = 16 << 20 | 16 << 4;
				final int var11 = br % 65536;
				final int var12 = br / 65536;
				GL11.glPushMatrix();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluidStack.fluid.getFluid().getStill().toString());
				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				this.drawTexturedModalRect(63, 9, sprite, 16, 16);
				GL11.glPopMatrix();
			}
		}
	}

	@Override
	public void renderGridElement(InfoNetworkFluid selection, int x, int y, int slot) {
		StoredFluidStack fluidStack = selection.getStoredStack();
		if (fluidStack.fluid != null) {
			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluidStack.fluid.getFluid().getStill().toString());
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			drawTexturedModalRect((x * eWidth), (y * eHeight), sprite, eWidth-2, eHeight-2);
			GL11.glPopMatrix();
		}
	}

	@Override
	public void renderElementToolTip(InfoNetworkFluid element, int x, int y) {
		StoredFluidStack fluidStack = element.getStoredStack();
		List list = new ArrayList<>();
		list.add(fluidStack.fluid.getFluid().getLocalizedName(fluidStack.fluid));
		if (fluidStack.stored != 0) {
			list.add(TextFormatting.GRAY + PL2Translate.BUTTON_STORED.t() + ": " + fluidStack.stored + " mB");
		}
		drawHoveringText(list, x, y, fontRenderer);
	}

	@Override
	public ResourceLocation getBackground() {
		if (getSetting() == Modes.SELECTED) {
			return stackBGround;
		}
		return clearBGround;
	}

	@Override
	public void renderStrings(int x, int y) {}
}
