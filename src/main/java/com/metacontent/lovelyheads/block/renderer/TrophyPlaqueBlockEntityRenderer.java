package com.metacontent.lovelyheads.block.renderer;

import com.metacontent.lovelyheads.block.LovelyBlocks;
import com.metacontent.lovelyheads.block.custom.TrophyPlaqueBlock;
import com.metacontent.lovelyheads.block.entity.TrophyPlaqueBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class TrophyPlaqueBlockEntityRenderer implements BlockEntityRenderer<TrophyPlaqueBlockEntity> {
    private final ItemRenderer itemRenderer;
    private final TextRenderer textRenderer;

    public TrophyPlaqueBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.itemRenderer = context.getItemRenderer();
        this.textRenderer = context.getTextRenderer();
    }

    @Override
    public void render(TrophyPlaqueBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getWorld() != null) {
            ItemStack stack = entity.getItems().get(0);
            BlockState state = entity.getWorld().getBlockState(entity.getPos());
            if (state.getBlock() instanceof TrophyPlaqueBlock) {
                renderStack(stack, state, entity, matrices, light, overlay, vertexConsumers);
                renderText(stack, state, entity, matrices, vertexConsumers, light);
            }
        }
    }

    private void renderStack(ItemStack stack, BlockState state, TrophyPlaqueBlockEntity entity, MatrixStack matrices, int light, int overlay, VertexConsumerProvider vertexConsumers) {
        matrices.push();
        Direction direction = state.get(TrophyPlaqueBlock.FACING);
        double[] translation = switch (direction) {
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
        matrices.pop();
    }

    private void renderText(ItemStack stack, BlockState state, TrophyPlaqueBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        if (stack.hasNbt()) {
            String owner = "";
            NbtCompound nbt = stack.getNbt();
            if (nbt.contains("SkullOwner", 10)) {
                owner = NbtHelper.toGameProfile(nbt.getCompound("SkullOwner")).getName();
            }
            else if (nbt.contains("SkullOwner", 8) && !Util.isBlank(nbt.getString("SkullOwner"))) {
                owner = nbt.getString("SkullOwner");
            }

            Direction direction = state.get(TrophyPlaqueBlock.FACING);
            double[] translation = switch (direction) {
                case NORTH -> new double[]{0.5, 0.25, 0.875, direction.asRotation()};
                case EAST -> new double[]{0.125, 0.25, 0.5, direction.getOpposite().asRotation()};
                case SOUTH -> new double[]{0.5, 0.25, 0.125, direction.asRotation()};
                case WEST -> new double[]{0.875, 0.25, 0.5, direction.getOpposite().asRotation()};
                case UP -> switch (entity.getStackDirection()) {
                    case DOWN, UP -> new double[]{0, 0, 0, 0};
                    case NORTH -> new double[]{0.5, 0.125, 0.25, entity.getStackDirection().asRotation()};
                    case SOUTH -> new double[]{0.5, 0.125, 0.75, entity.getStackDirection().asRotation()};
                    case WEST -> new double[]{0.25, 0.125, 0.5, entity.getStackDirection().getOpposite().asRotation()};
                    case EAST -> new double[]{0.75, 0.125, 0.5, entity.getStackDirection().getOpposite().asRotation()};
                };
                case DOWN -> switch (entity.getStackDirection()) {
                    case DOWN, UP -> new double[]{0, 0, 0, 0};
                    case NORTH -> new double[]{0.5, 0.875, 0.1875, entity.getStackDirection().asRotation()};
                    case SOUTH -> new double[]{0.5, 0.875, 0.8125, entity.getStackDirection().asRotation()};
                    case WEST -> new double[]{0.1875, 0.875, 0.5, entity.getStackDirection().getOpposite().asRotation()};
                    case EAST -> new double[]{0.8125, 0.875, 0.5, entity.getStackDirection().getOpposite().asRotation()};
                };
            };

            matrices.translate(translation[0], translation[1], translation[2]);
            matrices.scale(0.006f, -0.006f, 0.006f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) translation[3]));
            if (direction == Direction.UP) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            }
            if (direction == Direction.DOWN) {
                matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));
            }

            this.textRenderer.draw(owner, -((float) this.textRenderer.getWidth(owner) / 2), 0, 0x1A0E06, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, light);
        }
        matrices.pop();
    }
}
