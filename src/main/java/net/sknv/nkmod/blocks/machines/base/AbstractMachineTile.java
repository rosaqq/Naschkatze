package net.sknv.nkmod.blocks.machines.base;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Subclass to create a specific TileEntity type for a {@link MachineBlock}, {@code XXTile}.<br>
 * A supplier of this {@code XXTile} must be passed as argument to construct a {@link MachineBlock}.<br>
 * Register with {@code .simpleTileEntity()} on the block registration chain:<br>
 * <pre>
 * MY_MACHINE = REGISTRATE.object("my_machine").block(Material.IRON, p -> new MachineBlock(p, () -> new XXTile(MY_MACHINE.getSibling(ForgeRegistries.TILE_ENTITIES).get())))
 *         .simpleTileEntity(XXTile::new)
 *         .simpleItem().register();
 * </pre>
 * Also, because of how Registrate's registration chains work, {@code XXTile}'s constructor needs to eat a it's own TileEntityType.<br>
 * You can get that with {@code GRINDER.getSibling(ForgeRegistries.TILE_ENTITIES).get()}<br>
 * More details on the constructor javadoc: {@link AbstractMachineTile#AbstractMachineTile}.
 */
public abstract class AbstractMachineTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private final LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private final IMachineContainerFactory containerFactory;

    /**
     * Check class javadoc: {@link AbstractMachineTile}.
     * @param tileEntityTypeIn Subclass constructor param, pass through
     * @param containerFactory A factory of {@code XXTile}'s respective {@code XXContainer} -> {@code XXContainer::new} should suffice.
     */
    public AbstractMachineTile(TileEntityType<?> tileEntityTypeIn, IMachineContainerFactory containerFactory) {
        super(tileEntityTypeIn);
        this.containerFactory = containerFactory;
    }

    /**
     * Implementation example:
     * <pre>{@code return new ItemStackHandler(1) {
     *     @Override
     *     public boolean isItemValid(int slot, ItemStack stack) {
     *         return stack.getItem() == RegistrateHandler.SOME_ITEM.get().asItem();
     *     }
     *     @Override
     *     public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
     *         if (stack.getItem() != RegistrateHandler.SOME_ITEM.get().asItem()) {
     *             return stack;
     *         }
     *         return super.insertItem(slot, stack, simulate);
     *     }
     * };}</pre>
     * @return An ItemHandler with your machine's specific functionality.
     */
    @Nonnull
    protected abstract IItemHandler createHandler();

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(Objects.requireNonNull(getType().getRegistryName()).getPath());
    }

    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int i, PlayerInventory inv, PlayerEntity playerEntity) {
        return containerFactory.create(i, inv, pos);
    }

    @Override
    public void tick() {

    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("unchecked")
    public void read(BlockState state, CompoundNBT tag) {
        CompoundNBT invTag = tag.getCompound("inv");
        handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>)h).deserializeNBT(invTag));
        super.read(state, tag);
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("unchecked")
    public CompoundNBT write(CompoundNBT tag) {
        handler.ifPresent(h -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>)h).serializeNBT();
            tag.put("inv", compound);
        });
        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }
}
