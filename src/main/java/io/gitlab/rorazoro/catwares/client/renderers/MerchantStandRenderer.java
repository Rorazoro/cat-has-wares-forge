package io.gitlab.rorazoro.catwares.client.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;

import io.gitlab.rorazoro.catwares.common.blocks.BaseHorizontalBlock;
import io.gitlab.rorazoro.catwares.common.blocks.MerchantStandBlock;
import io.gitlab.rorazoro.catwares.common.tileentities.MerchantStandTileEntity;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MerchantStandRenderer extends TileEntityRenderer<MerchantStandTileEntity> {

    private ItemRenderer itemRenderer;
    private FontRenderer fontRenderer;

    public MerchantStandRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(MerchantStandTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
            IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        if (tileEntityIn == null)
            return;

        World world = tileEntityIn.getWorld();
        if (world == null)
            return;

        BlockState blockState = world.getBlockState(tileEntityIn.getPos());
        if (!(blockState.getBlock() instanceof MerchantStandBlock))
            return;

        itemRenderer = Minecraft.getInstance().getItemRenderer();
        fontRenderer = Minecraft.getInstance().fontRenderer;
        Direction side = blockState.get(BaseHorizontalBlock.HORIZONTAL_FACING);

        ItemStack price = tileEntityIn.getStackInSlot(MerchantStandTileEntity.PRICE_SLOT_INDEX);
        ItemStack offer = tileEntityIn.getStackInSlot(MerchantStandTileEntity.OFFER_SLOT_INDEX);

        if (!price.isEmpty()) {
            float priceX = 2.5f;
            float priceY = 8.6f;
            float priceZ = 0.95f;
            float sizeX = 0.33f;
            float sizeY = 0.33f;

            renderItem(price, matrixStackIn, sizeX, sizeY, priceX, priceY, priceZ, bufferIn, combinedLightIn,
                    combinedOverlayIn, side);

            float textX = 3.5f;
            float textY = 7.7f;
            float textZ = 0.9f;
            float textSizeX = 0.3f;
            float textSizeY = 0.3f;
            String priceText = String.valueOf(price.getCount());
            int color = 0xfff92f41;
            // priceText = "64";

            renderText(priceText, matrixStackIn, textSizeX, textSizeY, textX, textY, textZ, color, bufferIn,
                    combinedLightIn, side);
            renderText(priceText, matrixStackIn, textSizeX, textSizeY, textX + 0.11f, textY - 0.11f, textZ + 0.001f,
                    0xff333333, bufferIn, combinedLightIn, side);
        }

        if (!offer.isEmpty()) {
            float offerX = 8f;
            float offerY = 13.2f;
            float offerZ = 0.95f;
            float sizeX = 0.33f;
            float sizeY = 0.33f;

            renderItem(offer, matrixStackIn, sizeX, sizeY, offerX, offerY, offerZ, bufferIn, combinedLightIn,
                    combinedOverlayIn, side);

            float textX = 9.2f;
            float textY = 12.5f;
            float textZ = 0.9f;
            float textSizeX = 0.3f;
            float textSizeY = 0.3f;
            String offerText = String.valueOf(offer.getCount());
            // int color = 0xff0097fa;
            int color = 0xff2ecc71;
            // offerText = "64";

            renderText(offerText, matrixStackIn, textSizeX, textSizeY, textX, textY, textZ, color, bufferIn,
                    combinedLightIn, side);
            renderText(offerText, matrixStackIn, textSizeX, textSizeY, textX + 0.11f, textY - 0.11f, textZ + 0.001f,
                    0xff333333, bufferIn, combinedLightIn, side);
        }
    }

    private void renderItem(ItemStack stackIn, MatrixStack matrixStackIn, float sizeX, float sizeY, float x, float y,
            float z, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, Direction side) {

        float scaleX = sizeX;
        float scaleY = sizeY;
        float moveX = x + (8 * scaleX);
        float moveY = 16f - y + (8 * scaleY);
        float moveZ = z * .0625f;

        matrixStackIn.push();

        alignRendering(matrixStackIn, side);
        moveRendering(matrixStackIn, scaleX, scaleY, moveX, moveY, moveZ);

        Consumer<IRenderTypeBuffer> finish = (IRenderTypeBuffer buf) -> {
            if (buf instanceof IRenderTypeBuffer.Impl)
                ((IRenderTypeBuffer.Impl) buf).finish();
        };

        matrixStackIn.scale(1, -1, 1);
        matrixStackIn.scale(16, 16, 16);

        IBakedModel itemModel = itemRenderer.getItemModelWithOverrides(stackIn, null, null);
        boolean render3D = itemModel.isGui3d();
        finish.accept(bufferIn);

        if (render3D)
            RenderHelper.setupGui3DDiffuseLighting();
        else
            RenderHelper.setupGuiFlatDiffuseLighting();

        matrixStackIn.getLast().getNormal().set(Matrix3f.makeScaleMatrix(1, -1, 1));
        itemRenderer.renderItem(stackIn, TransformType.GUI, false, matrixStackIn, bufferIn, combinedLightIn,
                combinedOverlayIn, itemModel);
        finish.accept(bufferIn);

        matrixStackIn.pop();
    }

    private void renderText(String text, MatrixStack matrixStackIn, float sizeX, float sizeY, float x, float y, float z,
            int color, IRenderTypeBuffer bufferIn, int combinedLightIn, Direction side) {
        float scaleX = sizeX;
        float scaleY = sizeY;
        float moveX = x + (8 * scaleX);
        float moveY = 16f - y + (8 * scaleY);
        float moveZ = z * .0625f;
        int textWidth = fontRenderer.getStringWidth(text);

        matrixStackIn.push();

        alignRendering(matrixStackIn, side);
        moveRendering(matrixStackIn, scaleX, scaleY, moveX, moveY, moveZ);

        fontRenderer.renderString(text, -textWidth / 2f, 0.5f, color, false, matrixStackIn.getLast().getMatrix(),
                bufferIn, false, 0, combinedLightIn);
        matrixStackIn.pop();
    }

    private void alignRendering(MatrixStack matrix, Direction side) {
        // Rotate to face the correct direction for the drawer's orientation.

        matrix.translate(.5f, .5f, .5f);
        matrix.rotate(new Quaternion(Vector3f.YP, getRotationYForSide2D(side), true));
        matrix.translate(-.5f, -.5f, -.5f);
    }

    private void moveRendering(MatrixStack matrix, float scaleX, float scaleY, float offsetX, float offsetY,
            float offsetZ) {
        // NOTE: RenderItem expects to be called in a context where Y increases toward
        // the bottom of the screen
        // However, for in-world rendering the opposite is true. So we translate up by 1
        // along Y, and then flip
        // along Y. Since the item is drawn at the back of the drawer, we also translate
        // by `1-offsetZ` to move
        // it to the front.

        // The 0.00001 for the Z-scale both flattens the item and negates the 32.0
        // Z-scale done by RenderItem.

        matrix.translate(0, 1, 1 - offsetZ);
        matrix.scale(1 / 16f, -1 / 16f, 0.00005f);

        matrix.translate(offsetX, offsetY, 0);
        matrix.scale(scaleX, scaleY, 1);
    }

    private static final float[] sideRotationY2D = { 0, 0, 2, 0, 3, 1 };

    private float getRotationYForSide2D(Direction side) {
        return sideRotationY2D[side.ordinal()] * 90;
    }
}
