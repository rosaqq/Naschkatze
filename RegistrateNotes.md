# How to migrate from Forge registry to using Registrate

## Register a normal block
```java
RegistryObject<Block> MY_BLOCK = BLOCKS.register("my_block", MyBlock::new);
```
becomes
```java
RegistryEntry<MyBlock> MY_BLOCK = REGISTRATE.object("my_block").block(MyBlock::new).register();
```
RegistryEntry is a more flexible IForgeRegistryEntry implementation. Also note how the generic `<Block>` changed to `<MyBlock>` in registry entry.  
MyBlock must accept Properties in its constructor. If you want to add more hardcoded Properties, you can do it this way:
```java
public MyBlock(Properties p) {
        super(p.hardnessAndResistance(3).harvestTool(2).etc);
    }
```
Default material is Material.ROCK, but you can specify it using:
```java
.block(Material.IRON, MyMetalBlock::new)
```
or 
```java
.block(MyMetalBlock::new).initialProperties(Material.IRON)
```
## Register a Block with a Tile Entity and a Container
The fact that .object `("my_registry_name")` fixes that state until we call it again means we can short-hand registering tile entities and containers like so:
```java
RegistryEntry<MyMachineBlock> MY_MACHINE_BLOCK = REGISTRATE.object("my_machine").block(Material.IRON, MyMachineBlock::new.register();
RegistryEntry<TileEntityType<MyMachineTile>> MY_MACHINE_TILE = REGISTRATE.tileEntity(MyMachineTile::new).register();
RegistryEntry<ContainerType<MyMachineContainer>> MY_MACHINE_CONTAINER = REGISTRATE.container(MyMachineContainer::new, MyMachineScreen::new).register();
```

## Notes on generic MachineBlock, MachineTile and MachineContainer classes
`.container()` expects a
`ForgeContainerFactory: (ContainerType<?> type, int windowId, PlayerInventory inv, PacketBuffer data)`
ContainerFactory: (ContainerType<?> type, int windowId, PlayerInventory inv)