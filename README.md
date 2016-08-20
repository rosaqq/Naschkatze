# Naschkatze  
A Minecraft Mod  


The mod has a creative tab for all it's stuff.  
The survival additions right now are:

- Copper Ore
- Copper Ingot (smelt ore)
- Copper Block (9 ingots in crafting table)
- Copper Coil (8 ingots in chest-like pattern with wooden plank in the middle)
- When you redstone power a Copper Coil and stand within 3 blocks of it, the compass will point towards the coil  

The way the coil-compass mechanic works is still very inefficient; optimisation planed for next commit.

### Special Thanks
This would not be possible without the patience and constant assitance of many people for the #minecraftforge IRC.  
Here are named some to whoom I owe most of my current understanding of forge, minecraft source code and modding in general  
diesieben07  
gigaherz  
howtonotwin  
williewillus  
Ordinastie_  


####irc best moments
```
<Ordinastie_> because to be honest, you clearly lack programming knowledge to do something better optimized  
  
<diesieben07> do you know what the word "instance" means?  
  
<RANKSHANK> probably the first thing you should learn is how to use your ide  
  
<

diesieben07> jesus man, use your IDE  
  
<ghz|afk> keep in mind scanning for entities is slow  
<ghz|afk> so you may want to throttle it a bit  
<secknv> so what is the best way of knowing when a player is within 3 blocks of my block  
<ghz|afk> williewillus told you  
<ghz|afk> there's methods in world, for scanning  
<secknv> but you said it's slow  
<ghz|afk> yes  
<secknv> slow is not best :\  
<ghz|afk> but you have limited java knowledge  
<secknv> i cry  
```