package mooklabs.nightfall.bag;

import mooklabs.nightfall.NFMain;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiItemInventory extends GuiContainer {

	/**
	 * x and y size of the inventory window in pixels. Defined as float, passed as int These are used for drawing the player model.
	 */
	private float xSize_lo;
	private float ySize_lo;

	/** ResourceLocation takes 2 parameters: ModId, path to texture at the location: */
	private static final ResourceLocation iconLocation = new ResourceLocation(NFMain.modid, "textures/gui/inventoryitem.png");

	/** The inventory to render on screen */
	private final InventoryItemBag inventory;

	public GuiItemInventory(ContainerItem containerItem) {
		super(containerItem);
		this.inventory = containerItem.inventory;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		this.xSize_lo = par1;
		this.ySize_lo = par2;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = this.inventory.hasCustomInventoryName() ? this.inventory.getInventoryName() : this.inventory.getInventoryName();
		//TODO fix pos this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 0, 4210752);
		//TODO fix pos this.fontRendererObj.drawString("inventory", 26, this.ySize - 96 + 4, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(iconLocation);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		int i1;
		int slotCount = 0;
		for (int x = 0; x < 5; x++){
			for (int y = 0; y < 4; y++) {
				this.drawTexturedModalRect(x * 18 + k + 79, y * 18 + l + 7, 7, 7, 18, 18);
				if (slotCount++ > inventory.INV_SIZE+1) break;
			}
			if (slotCount++ > inventory.INV_SIZE+1) break;
		}
		drawPlayerModel(k + 51, l + 75, 30, k + 51 - this.xSize_lo, l + 75 - 50 - this.ySize_lo, this.mc.thePlayer);
	}

	// If using 1.7.2 (you can do this in 1.6.4 too, but it wasn't absolutely necessary)
	// you can override keyTyped to allow your custom keybinding to close the gui
	@Override
	protected void keyTyped(char c, int keyCode) {
		// make sure you call super!!!
		super.keyTyped(c, keyCode);
		// 1 is the Esc key, and we made our keybinding array public and static so we can access it here
		// if (c == 1 || keyCode == KeyHandler.keys[KeyHandler.CUSTOM_INV].getKeyCode()) {
		// mc.thePlayer.closeScreen();
		// }
	}

	/**
	 * This renders the player model in standard inventory position; copied straight from vanilla code but with renamed method parameters
	 */
	public static void drawPlayerModel(int x, int y, int scale, float yaw, float pitch, EntityLivingBase entity) {
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 50.0F);
		GL11.glScalef(-scale, scale, scale);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float f2 = entity.renderYawOffset;
		float f3 = entity.rotationYaw;
		float f4 = entity.rotationPitch;
		float f5 = entity.prevRotationYawHead;
		float f6 = entity.rotationYawHead;
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-((float) Math.atan(pitch / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
		entity.renderYawOffset = (float) Math.atan(yaw / 40.0F) * 20.0F;
		entity.rotationYaw = (float) Math.atan(yaw / 40.0F) * 40.0F;
		entity.rotationPitch = -((float) Math.atan(pitch / 40.0F)) * 20.0F;
		entity.rotationYawHead = entity.rotationYaw;
		entity.prevRotationYawHead = entity.rotationYaw;
		GL11.glTranslatef(0.0F, entity.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		entity.renderYawOffset = f2;
		entity.rotationYaw = f3;
		entity.rotationPitch = f4;
		entity.prevRotationYawHead = f5;
		entity.rotationYawHead = f6;
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
}