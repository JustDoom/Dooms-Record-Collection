package com.imjustdoom.doomsrecordcollection.block;

import com.imjustdoom.doomsrecordcollection.block.entity.RecordDisplayEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RecordDisplay extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public RecordDisplay(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        Vec3 vec = hit.getLocation();
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        double rawInc = (facing == Direction.NORTH || facing == Direction.SOUTH) ? vec.x % 1 : vec.z % 1;
        int slot = getSlot(rawInc >= 0 ? rawInc : rawInc + 1);
        ItemStack heldItem = player.getItemInHand(hand);
        if (slot == -1 || !(level.getBlockEntity(pos) instanceof RecordDisplayEntity recordDisplay)) {
            return InteractionResult.SUCCESS;
        }

        if (heldItem.getItem() instanceof RecordItem && recordDisplay.getItem(slot).isEmpty()) {
            recordDisplay.setItem(slot, heldItem);
            player.getItemInHand(hand).shrink(1);
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.NEUTRAL, 1f, 1f);
        } else if (heldItem.isEmpty() && !recordDisplay.getItem(slot).isEmpty()) {
            player.setItemInHand(hand, recordDisplay.removeItem(slot, 1));
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.NEUTRAL, 1f, 1f);
        }

        return InteractionResult.CONSUME;
    }

    public static int getSlot(double inc) {
        return inc < 1 / 16d || inc > 15 / 16d ? -1 : (int) (8 * inc - 0.5f);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == Direction.Axis.X
                ? Block.box(1, 0, 0.5f, 15, 6, 15.5f)
                : Block.box(0.5f, 0, 1, 15.5f, 6, 15);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RecordDisplayEntity(blockPos, blockState);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.is(newState.getBlock())) {
            return;
        }

        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof RecordDisplayEntity recordDisplay) {
            for (int i = 0; i < RecordDisplayEntity.MAX_SLOTS; i++) {
                ItemStack item = recordDisplay.getItem(i);
                if (item.isEmpty()) {
                    continue;
                }
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), item);
            }
            level.updateNeighbourForOutputSignal(pos, this);
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (level.getBlockEntity(pos) instanceof RecordDisplayEntity recordDisplay) {
            int count = 0;
            for (int i = 0; i < RecordDisplayEntity.MAX_SLOTS; i++) {
                if (recordDisplay.getItem(i) != ItemStack.EMPTY) {
                    count++;
                }
            }
            return count;
        }

        return 0;
    }
}
