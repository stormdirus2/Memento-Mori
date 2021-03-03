![](src/main/resources/pack.png)
## Some very opinionated balancing for Requiem.

Obviously, this mod requires Requiem. It has been tested with version 1.5, but may even work on earlier ones.

### Changes
Attrition has been completely reworked. It lasts 20 minutes, and will increase a level every time you die regardless if you are human.

This means stockpiling on resources does not allow you to avoid consequences.

Every time attrition runs out, it decreases a level and resets the timer to 20 minutes yet again. This cannot be cheesed through the usage of milk or other methods.

Your Attrition caps out at level V, and can only reach there from level IV if you die as a player (or spirit via void damage) and not a mob.

Once at level V, you can no longer possess things. Basically, think soft-hardcore.


### Additional Changes
* Zombies can only eat rotten flesh by default
* Skeletons now use bone meal instead, and it only heals 2 health (because the cooldown is lower than rotten flesh).
* Enchanted golden apples can be used to cure without weakness applied to the player. They can now be crafted using the golden apple recipe, but with golden blocks instead of ingots.
* (Latest commit) Blocks with a blast resistance of 1200 (obsidian level) or higher cannot be phased through. Happy ghost-trapping :)

### Gamerules
* ``mementomori:attritionPermaDeath`` -Controls the softlocking of possession when >= Attrition V. [DEFAULT: true]
* ``mementomori:cureAllItems`` -Controls whether or not ``cure_alls`` items can cure a possessed without the weakness debuf. [DEFAULT: true]
* ``mementomori:slowAttritionFade`` -Controls whether or not attrition will decrease a level at a time, instead of completely disapearing. [DEFAULT: true]
* ``mementomori:blastResistantUnphasable`` Controls if players are not able to go through blocks with a blast resistance of 1,200 or higher. [DEFAULT: true]
* ``mementomori:attritionTime`` Controls the amount of time, in seconds, that a player has attrition for. [DEFAULT: 1200 (which is 20 minutes))]
