package net.pevori.queencats.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.HumanoidAnimalEntity;
import net.pevori.queencats.gui.menu.HumanoidAnimalMenu;

public class HumanoidAnimalScreen extends AbstractContainerScreen<HumanoidAnimalMenu> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(QueenCats.MOD_ID, "textures/gui/container/humanoid_animal.png");
    private final HumanoidAnimalEntity entity;

    public HumanoidAnimalScreen(HumanoidAnimalMenu handler, Inventory inventory, Component text) {
        super(handler, inventory, text);
        this.entity = handler.getEntity();
    }

    @Override
    protected void renderBg(PoseStack matrices, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // TODO: Add weapon slot eventually.
        /*if (this.entity.hasWeaponSlot()) {
            this.drawTexture(matrices, i + 7, j + 35 - 18, 18, this.backgroundHeight + 54, 18, 18);
        }*/

        // Draws the background of the inventory.
        blit(matrices, x, y, 0, 0, imageWidth, imageHeight);

        // Draws the entity inventory slots.
        this.blit(matrices, x + 61, y + 17, 0, this.imageHeight, 6 * 18, 54);

        // Draws the player inventory and hotbar slots.
        this.blit(matrices, x + 7, y + 35, 0, this.imageHeight + 54, 18, 18);

        // Draws the entity render in the black box.
        InventoryScreen.renderEntityInInventory(x + 42, y + 66, 20, (float)(x + 51) - mouseX, (float)(y + 75 - 50) - mouseY, this.entity);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        renderTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
    }
}