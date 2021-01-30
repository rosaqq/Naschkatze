package net.sknv.nkmod.blocks.machines.base;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

/**
 * Generic class to create all Machines.
 * <h3>Instructions:</h3>
 * <ol>
 *     <li>
 *         Create XXContainer extending {@link AbstractMachineContainer}, with a
 *         {@code XXContainer(int windowId, PlayerInventory inv, BlockPos pos)} constructor.
 *         More details on what you need to pass to {@code super()} in {@link AbstractMachineContainer#AbstractMachineContainer}.
 *     </li>
 *     <li>
 *         Create XXTile extending {@link AbstractMachineTile}, with a {@code XXTile(TileEntityType<?> type)} constructor.
 *         Pass {@code type} and {@code XXContainer::new} in {@code super()}.
 *     </li>
 *     <li>
 *         Register the block in {@link net.sknv.nkmod.RegistrateHandler}, just like the examples in the class' javadoc.
 *     </li>
 *     <li>
 *         Datagen is in force, you should add a gui texture to {@code textures/gui}
 *         (and reference it during container registration as an argument to {@link MachineScreenFactory}).<br>
 *         Also, add the following textures to {@code textures/block}:
 *         <ul>
 *             <li>x.png</li>
 *             <li>x_back.png</li>
 *             <li>x_bot.png</li>
 *             <li>x_sides.png</li>
 *             <li>x_top.png</li>
 *         </ul>
 *         Where "x" is the MachineBlock's registry name.
 *     </li>
 *     <li>
 *         Lang (en_us.json) is currently manually generated, you might want to add an entry on that too.
 *     </li>
 * </ol>
 *
 */
public class MachineBlock extends ContainerBlock {

    private final Supplier<TileEntity> tileEntitySupplier;
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty WORKING = BooleanProperty.create("machine_working");

    /**
     * Check class javadoc for more info -> {@link MachineBlock}.
     * @param p Properties pass through, required by Registrate's chain mechanics
     * @param tileEntitySupplier A supplier for the TileEntity that will be associated with this Block
     */
    public MachineBlock(Properties p, Supplier<TileEntity> tileEntitySupplier) {
        super(p.sound(SoundType.METAL).hardnessAndResistance(2f).setLightLevel(MachineBlock::getLightValue));
        this.tileEntitySupplier = tileEntitySupplier;

        // Set default state
        BlockState defaultState = this.getStateContainer().getBaseState().with(WORKING, false);
        this.setDefaultState(defaultState);
    }

    /**
     * Returns a different light level based on the BlockState (working or not)
     * @param state BlockState in
     * @return Light level out
     */
    public static int getLightValue(BlockState state) {
        boolean working = state.get(WORKING);
        return working ? 10 : 0;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, WORKING);
    }

    // Kinda useless since this block extends ContainerBlock
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return tileEntitySupplier.get();
    }


    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        // game logic should only be ran on the logical server
        // so if this client, we don nothing
        if (worldIn.isRemote) return ActionResultType.SUCCESS;
        // if this is the server...
        INamedContainerProvider container = this.getContainer(state, worldIn, pos);
        if (container != null) {
            NetworkHooks.openGui((ServerPlayerEntity) player, container, pos);
            // is this the pos that goes in RegistrateHandler?
        }
        // diff is arm swing?
        return ActionResultType.CONSUME;
        // return ActionResultType.SUCCESS;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof AbstractMachineTile) {
                ((AbstractMachineTile)tileEntity).dropAllContents(worldIn, pos);
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    // required because the default (super method) is INVISIBLE for BlockContainers.
    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public BlockRenderType getRenderType(BlockState iBlockState) {
        return BlockRenderType.MODEL;
    }
}
