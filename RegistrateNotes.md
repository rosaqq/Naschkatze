# How to migrate from Forge registry to using Registrate

## Init Registrate
```java
REGISTRATE = Registrate.create(MODID).itemGroup(()-> new ItemGroup("Tab Label") {
    @Nonnull
    @Override
    public ItemStack createIcon() {
        return new ItemStack(MY_ITEM.get());
    }
});
```
Notes:
- `Registrate.create()` must be called at main mod class constructor or later. Static init is too early.  
   Good example TBD
  ```java
   Good example placeholder
  ```
- By calling .itemGroup() like this, every Mod Item gets automatically added to the creative tab.
  You could also pass a pre-made itemGroup subclass (and include a localized name): `.itemGroup(MyModItemGroup::new, "Localized Name")`

## Register a normal block
```java
RegistryObject<Block> MY_BLOCK = BLOCKS.register("my_block", MyBlock::new);
```
becomes
```java
RegistryEntry<MyBlock> MY_BLOCK = REGISTRATE.object("my_block").block(MyBlock::new).simpleItem().register();
```
Note the .simpleItem() to add Block Items -> no need to have a separate `MY_BLOCK_ITEM`.  
Get Block Items with `MY_BLOCK.get().asItem()`.  
RegistryEntry is a more flexible IForgeRegistryEntry implementation by Registrate.  
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
We can short-hand registering tile entities and containers like so:
```java
RegistryEntry<MyMachineBlock> MY_MACHINE_BLOCK = REGISTRATE.object("my_machine").block(Material.IRON, MyMachineBlock::new)
        .simpleTileEntity(MyMachineTile::new)
        .simpleItem().register();
RegistryEntry<ContainerType<MyMachineContainer>> MY_MACHINE_CONTAINER = REGISTRATE.container(MyMachineContainer::new, MyMachineScreen::new).register();
```
Get Tile Entities with `MY_MACHINE_BLOCK.getSibling(ForgeRegistries.TILE_ENTITIES).get()`.  

## Register an Item
Extremely simple.
```java
RegistryEntry<Item> MY_ITEM = REGISTRATE.object("my_item").item(Item::new).register();
```