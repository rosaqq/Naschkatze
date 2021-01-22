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

public abstract class AbstractMachineTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private final LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private final IMachineContainerFactory containerFactory;

    public AbstractMachineTile(TileEntityType<?> tileEntityTypeIn, IMachineContainerFactory containerFactory) {
        super(tileEntityTypeIn);
        this.containerFactory = containerFactory;
    }

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
