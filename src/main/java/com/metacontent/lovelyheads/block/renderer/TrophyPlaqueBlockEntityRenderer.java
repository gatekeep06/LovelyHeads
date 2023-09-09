package com.metacontent.lovelyheads.block.renderer;

import com.metacontent.lovelyheads.block.LovelyBlocks;
import com.metacontent.lovelyheads.block.custom.TrophyPlaqueBlock;
import com.metacontent.lovelyheads.block.entity.TrophyPlaqueBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class TrophyPlaqueBlockEntityRenderer implements BlockEntityRenderer<TrophyPlaqueBlockEntity> {
    private double[] translation;
    private final ItemRenderer itemRenderer;

    public TrophyPlaqueBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(TrophyPlaqueBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        if (entity.getWorld() != null) {
            ItemStack stack = entity.getItems().get(0);
            BlockState state = entity.getWorld().getBlockState(entity.getPos());
            if (state.getBlock() == LovelyBlocks.TROPHY_PLAQUE_BLOCK) {
                Direction direction = state.get(TrophyPlaqueBlock.FACING);
                translation = switch (direction) {
                    case NORTH -> new double[]{0.5, 0.5625, 0.75, direction.getOpposite().asRotation()};
                    case EAST -> new double[]{0.25, 0.5625, 0.5, direction.asRotation()};
                    case SOUTH -> new double[]{0.5, 0.5625, 0.25, direction.getOpposite().asRotation()};
                    case WEST -> new double[]{0.75, 0.5625, 0.5, direction.asRotation()};
                    case UP -> new double[]{0.5, 0.3125, 0.5,
                            entity.getStackDirection() == Direction.NORTH || entity.getStackDirection() == Direction.SOUTH ? entity.getStackDirection().getOpposite().asRotation() : entity.getStackDirection().asRotation()};
                    case DOWN -> new double[]{0.5, 0.6875, 0.5,
                            entity.getStackDirection() == Direction.NORTH || entity.getStackDirection() == Direction.SOUTH ? entity.getStackDirection().getOpposite().asRotation() : entity.getStackDirection().asRotation()};
                };
                matrices.translate(translation[0], translation[1], translation[2]);
                matrices.scale(0.75f, 0.75f, 0.75f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) translation[3]));
                this.itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
            }
        }

        matrices.pop();
    }
}
