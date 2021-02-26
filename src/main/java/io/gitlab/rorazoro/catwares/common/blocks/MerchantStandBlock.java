package io.gitlab.rorazoro.catwares.common.blocks;

import java.util.UUID;
import java.util.stream.Stream;

import io.gitlab.rorazoro.catwares.common.registries.TileEntityRegistry;
import io.gitlab.rorazoro.catwares.common.tileentities.MerchantStandTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class MerchantStandBlock extends BaseHorizontalBlock {

    private static VoxelShape SHAPE = Stream
            .of(Block.makeCuboidShape(0, 0, 0, 16, 2, 16), Block.makeCuboidShape(0, 14, 0, 16, 16, 16),
                    Block.makeCuboidShape(14, 2, 0, 16, 14, 2), Block.makeCuboidShape(0, 2, 0, 2, 14, 2),
                    Block.makeCuboidShape(0, 2, 2, 16, 14, 16), Block.makeCuboidShape(2, 2, 1, 14, 14, 2))
            .reduce((v1, v2) -> {
                return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
            }).get();

    public MerchantStandBlock(Properties properties) {
        super(properties);
        runCalculation(SHAPE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityRegistry.MERCHANT_STAND_TILE_ENTITY_TYPE.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
            Hand handIn, BlockRayTraceResult result) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof MerchantStandTileEntity && handIn == Hand.MAIN_HAND) {
                if (player.getHeldItemMainhand().isEmpty()) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, (MerchantStandTileEntity) tile, pos);
                } else {
                    ((MerchantStandTileEntity) tile).tryExchangeItems(player);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.FAIL;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (placer instanceof PlayerEntity) {
            UUID id = ((PlayerEntity) placer).getUniqueID();
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof MerchantStandTileEntity) {
                ((MerchantStandTileEntity) tile).setOwnerId(id);
            }
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof MerchantStandTileEntity) {
                InventoryHelper.dropItems(worldIn, pos, ((MerchantStandTileEntity) te).getItems());
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(this).get(state.get(HORIZONTAL_FACING));
    }
}