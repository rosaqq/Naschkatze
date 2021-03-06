﻿# Naschkatze
A Minecraft Mod  
Jump to [Project Setup Instructions](#project-setup-instructions)

### Mod revamp plan

Currently translating roadmap to issues -> [GitHub Project](https://github.com/rosaqq/Naschkatze/projects/1).  
Information is also being migrated to the wiki -> [Project Wiki](https://github.com/rosaqq/Naschkatze/wiki).

- [x] Move to 1.16
- [ ] A new direction: Atomkraft
  - [x] Remove odd ends
  - [ ] Uranium ore to fission
    - [ ] Uraninite Ore Processing
      - [x] Uraninite Ore
      - [ ] Grinder
      - [x] Crushed Uraninite
      - [ ] Acid leaching (H2SO4)
      - [ ] Force hexavalent oxidation with sodium Chlorate
      - [ ] DEHPA solvent extraction
      - [ ] Ammonia for precipitation
      - [ ] Yellowcake
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
      - [ ] Criticality math (delayed and prompt)
      - [ ] Radiation
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
  - [ ] ...the abyss will also gaze into you
    - [ ] Prompt supercritical runaway
    - [ ] Demon core
    - [ ] For all who take the sword will perish by the sword
  - [ ] Production chain improvements
    - [ ] In-situ leaching
      - [ ] TBD
  - [ ] Modern Reactors
    - [ ] Reactor-grade enriched uranium
    - [ ] Plutonium
    - [ ] MOX

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
