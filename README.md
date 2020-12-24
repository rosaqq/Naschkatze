# Naschkatze
A Minecraft Mod

### Mod revamp plan
- [x] Move to 1.16
- [ ] A new direction: Atomkraft
  - [x] Remove odd ends
  - [ ] Uranium ore to fission
    - [ ] Create roadmap


## Project Setup Instructions
This is a MinecraftForge project ([homepage](http://minecraftforge.net/), [github](https://github.com/MinecraftForge/MinecraftForge)).  
See the Forge Documentation for more detailed instructions:  
http://mcforge.readthedocs.io/en/latest/gettingstarted/  

**Step 1:**
- `git clone <this repo>`

**Step 2:**
- Eclipse instructions:
  1. Install IntelliJ
  2. Continue below ↓

- IntelliJ instructions:
  1. Open IDEA, select import project.
  2. Select the build.gradle file on the import GUI.
  3. Run `gradlew genIntellijRuns` in the project folder.
  4. Refresh the Gradle Project if required.

- Nice to know gradle tasks:
  - `gradlew --refresh-dependencies` to refresh the local cache
  - `gradlew clean` to reset everything (code is untouched)