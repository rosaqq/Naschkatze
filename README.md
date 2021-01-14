# Naschkatze
A Minecraft Mod  
Jump to [Project Setup Instructions](#project-setup-instructions)

### Mod revamp plan

Currently translating roadmap to issues -> [GitHub Project](https://github.com/rosaqq/Naschkatze/projects/1).  

- [x] Move to 1.16
- [ ] A new direction: Atomkraft
  - [x] Remove odd ends
  - [ ] Uranium ore to fission
    - [ ] Uraninite Ore Processing
      - [x] Uraninite Ore
      - [ ] Grinder
      - [x] Crushed Uraninite
      - [ ] Acid leaching (H2SO4)
        - Sulfuric Acid production:
          S + O2 -> SO2 (sulfur is burned, gas target temperature = 450°C)
          2SO2 + O2 <-> 2SO3 (Vanadium(V) oxide catalyst)
          SO3 + H2O -> H2SO4(g) (highly exothermic)
          Acid must be condensed into liquid.
      - [ ] Force hexavalent oxidation state (NaClO3)
        - [ ] Sodium Chlorate production:
              NaCl + 3H2O -> NaClO3 + 3H2
              High heat (>70°C) and pH must be controlled (avoid increase).
      - [ ] DEHPA solvent extraction
      - [ ] Yellowcake (precipitate by adding NH3(aq), dry into dust)
      - [ ] Uranium Trioxide (ignite/smelt yellowcake 500°C air)
      - [ ] Uranium Dioxide (480°C hydrogen reduction)
    - [ ] Chicago Pile 1
      - [ ] Natural uranium dioxide pellets
      - [ ] Nuclear graphite bricks
        - Produced via the Acheson process.
        - Mixture of 50% quartz dust, 40% coke powder, 7% sawdust and 3% salt.
        - Heat in a graphite electrode furnace (SiC forms at 1600-2500°C (wikipedia) / 2700°C (Materials Handbook by François Cardarelli, p. 627))
        - Keep heating, and the silicon will vaporize leaving high purity graphite behind (US patent 568323)
      - [ ] Control rods (Cadmium based?)
      - [ ] Neutron detector
  - [ ] Advanced Uranium Processing
    - Uranium Tetrafluoride (UO2 + 4HF -> UF4 + H2O)
    - [ ] Natural Uranium Metal
      - [ ] Thermal reduction of halide compounds:
        - UF4 + Ca -> U + 2(CaF2) ... High cost for pure Ca, but simpler process
        - UF + 2Mg -> U + 2(MgF2) ... Low cost for pure Mg, but a more complex process
  - [ ] Natural Uranium Reactors
    - [ ] Heavy water
    - [ ] ...
  - [ ] Age of enrichment
    - [ ] UF6 (Fluorine Oxidation)
    - [ ] Gas Centrifuges
    - [ ] Enriched UO2 (IDR method: 200°C steam + H2 -> UO2 + HF)
    - [ ] HEU
    - [ ] Fuel Pellets
    - [ ] Fuel Rods
  - [ ] Production chain improvements
    - [ ] In-situ leaching
      - [ ] TBD
  


## Project Setup Instructions
This is a MinecraftForge project ([homepage](http://minecraftforge.net/), [github](https://github.com/MinecraftForge/MinecraftForge)).  
See the Forge Documentation for more detailed instructions:  
http://mcforge.readthedocs.io/en/latest/gettingstarted/  

**Step 1:**
- `git clone <this repo>`

**Step 2: IntelliJ Setup**  
1. Open IDEA, select import project.
2. Select the build.gradle file on the import GUI.
3. Run `gradlew genIntellijRuns` in the project folder.
4. Refresh the Gradle Project if required.
5. To get Thermal Expansion & Co. in dev env:
   1. Create folder `dev_mods` in project root.
   2. Add the [CoFH Core](https://www.curseforge.com/minecraft/mc-mods/cofh-core/files), [Thermal Foundation](https://www.curseforge.com/minecraft/mc-mods/thermal-foundation/files), and [Thermal Expansion](https://www.curseforge.com/minecraft/mc-mods/thermal-expansion/files) jars.  
      (Currently using versions 1.16.3-1.1.6)
   3. Gradle should take care of everything now.
  
- Nice to know gradle tasks:
  - `gradlew --refresh-dependencies` to refresh the local cache
  - `gradlew clean` to reset everything (code is untouched)