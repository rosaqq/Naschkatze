# How to migrate from Forge registry to using Registrate

## Stuff Registrate does for you
- Linking Screens to Containers   
  `REGISTRATE.container(MyMachineContainer::new, MyMachineScreen::new)`
  instead of having something like:
  ```java
  @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
  public class ClientSetup {
  
    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(RegistryHandler.MY_MACHINE_CONTAINER.get(), MyMachineScreen::new);
        ScreenManager.registerFactory(RegistryHandler.MY_OTHER_MACHINE_CONTAINER.get(), MyOtherMachineScreen::new);
    }
  }
  ```
- Automatically puts all your stuff into the creative tab you specify during `Registrate.create()`
- Simple BlockItem generation with `.block().simpleItem()`
- Simple TE registration with `.block().simpleTileEntity()`
- Datagen (TBD)
- Lang (TBD)

## Init Registrate
- `Registrate.create()` must be called at main mod class constructor or later. Static init is too early.  
   Here is an example where we create a helper class, declare static fields, and fill them up later (`RegistrateHandler.init()` gets called on our @Mod class' constructor)  
  ```java
  public class RegistrateHandler {
  
    public static Registrate REGISTRATE;
  
    // blocks
    public static RegistryEntry<MyBlock> MY_BLOCK;
    public static RegistryEntry<MyMachineBlock> MY_MACHINE_BLOCK;
    public static RegistryEntry<ContainerType<MyMachineContainer>> MY_MACHINE_CONTAINER;
  
    // items
    public static RegistryEntry<Item> A_ITEM;
    public static RegistryEntry<Item> B_ITEM;
  
    public static void init() {
  
      REGISTRATE = Registrate.create(Naschkatze.MODID).itemGroup(MyCreativeTab::new);
  
      // blocks
      MY_BLOCK = REGISTRATE.object("my_block").block(MyBlock::new)
              .properties(p -> p.harvestLevel(2))
              .simpleItem().register();
      MY_MACHINE_BLOCK = REGISTRATE.object("my_machine").block(Material.IRON, MyMachineBlock::new)
              .simpleTileEntity(MyMachineTile::new)
              .simpleItem().register();
      MY_MACHINE_CONTAINER = REGISTRATE.container(MyMachineContainer::new, MyMachineScreen::new).register();
  
      // items
      A_ITEM = REGISTRATE.object("a_item").item(Item::new).register();
      B_ITEM = REGISTRATE.object("b_item").item(Item::new).register();
    }
  }
  ```
- By calling `.itemGroup()` like this, every Mod Item gets automatically added to the creative tab.
  You could also pass a localized name: `.itemGroup(MyModItemGroup::new, "Localized Name")`

## Register a normal block
```java
RegistryObject<Block> MY_BLOCK = BLOCKS.register("my_block", MyBlock::new);
```
becomes
```java
RegistryEntry<MyBlock> MY_BLOCK = REGISTRATE.object("my_block").block(MyBlock::new).simpleItem().register();
```
Note the `.simpleItem()` to add Block Items -> no need to have a separate `MY_BLOCK_ITEM`.  
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