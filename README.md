![](src/main/resources/pack.png)
## A content and balancing add-on for Requiem.

Obviously, this mod requires Requiem. It has been tested with version 1.5.1, but may even work on earlier ones.

### Changes:
<details>
  <summary>Possession</summary>

Now, if you touch a mob while possessing, they will be alerted of your unnatural state.

Therefore, hostile and neutral mobs will attack you even while possessing their brethren if you collide with em'.

Additionally, while possessing a mob you have the default harvest level of a wooden pickaxe.
Using blocks that usually require tools to mine, you can adjust your mining speed to that of a stone pickaxe. Albeit, with the harvest level staying at wooden.

Finally, potential hosts will spawn upon death - and over time while incorporeal.
Hosts may spawn in such a fashion as to protect themselves from the sun.
</details>

######
<details>
  <summary>Attrition</summary>

Attrition has been completely reworked.\
Its effect now lasts a default of 20 minutes (configurable through gamerules). \
Additionally, each time Attrition runs out, it goes down a level instead of simply disappearing.\
For example: An Attrition level of IV would take 80 minutes to completely go away. 

While not in human form, Attrition ticks upwards instead of downwards. This can be prevented by gaining the status effect 'Satiation'. This is also configurable through gamerules.\
After reaching the configurable Attrition duration, the Attrition duration resets to 1 and its amplifier increases.\
Reaching level IV from a lower Attrition level will kill the player.

For future reference, the maximum Attrition level is IV.
</details>

######
<details>
  <summary>Mudsoaked</summary>

By using dirt while possessed, you gain the effect Mudsoaked.\
While mudsoaked, mobs that would usually be set on fire in the daylight instead take minor fire damage over time. 

This means player can use dirt as an emergency item to prevent taking substantial fire damage in the absence of more efficient ways to avoid it.

</details>

######
<details>
  <summary>Satiation</summary>

Satiation is a new effect that a player can get while possessing a mob from attacking certain types of mobs.

Generally, the 'humans' of the minecraft world give the most Satiation.
<details>
  <summary>'Humans'</summary>

* Villagers
* Pillagers
* Golems (Either type)
* Witches
* Evokers
* Vindicators
</details>

You can also, however, gain it in smaller quantities (1/4th) from Piglins.
<details>
  <summary>Piglins</summary>

* Piglins
* Piglin Brutes
</details>

To put this into context, 20 damage (A Villager's max health) will equal out to 15 minutes of 'Satiation'.
Therefore, you would need to deal 80 damage against Piglins to reach the same duration.
</details>

######
<details>
  <summary>Alleviation</summary>

Most commonly obtained through Soul Salves. 

While with this effect, your maximum health is no longer decreased by any magnitude of attrition.

Due to one of the requirements to using a Soul Salve forcing you to be human, it is best used to alleviate high levels of attrition that will take a long time to fully dissipate. 

Do keep in mind that Soul Salves DO inflict soul damage. 60, to be exact. So be careful.
</details>

######
<details>
  <summary>Soul damage</summary>

Soul damage can occur from multiple sources.\
When afflicted with soul damage, you do not take damage to your health.\
Instead, you take soul damage as Attrition duration.

Taking 3 soul damage would add 3 seconds to your Attrition.\
Damage going over the configurable Attrition duration would increase the amplifier and have the extra damage be the new duration.\
For example: Taking 7 soul damage when at Attrition I (19:55) would give you Attrition II (00:02).

Taking soul damage that would put you at or in Attrition IV will kill you.

Certain damage sources inflict 3 soul damage by default:
<details>
  <summary>Soul-damaging sources</summary>

* Soul fire
* Wither
* Dragon breath
</details>

Additionally, certain mobs can exert varying levels of soul damage:
<details>
  <summary>Soul-damaging mobs</summary>

* Phantoms: 60
* Vex: 15
* Evoker fangs: 5
</details>

Lastly, the new enchantment, Soul Cleaving, inflicts soul damage 30 x (level).
</details>

######
<details>
  <summary>Phantoms</summary>

I always thought Phantoms were so cool visually and in how their attack patterns work.\
Sadly, they are quite irrelevant at their best, and incredibly annoying at their worst.



So I decided to make them much more interesting.



Phantoms will now spawn every 10 seconds during night in numbers according to your Attrition level.\
A lack of Attrition would mean no Phantoms spawn, IV would mean 4 would spawn.

\
They only spawn if you are not under a block, so it is encouraged to stay inside during night.

They attack the player even if they are possessing a mob. Even possessing a Phantom does not stop these beasts.

With their 60 soul damage, it's easy for them to deck even a netherite-clad player.


Make sure to remember that reaching Attrition IV from soul damage will kill you, so no amount of armor will stop them.
\
So... what WILL stop them?

Light.

If you are in a spot with light, not only will they not spawn, but existing ones will not be able to even touch you.
</details>

######
<details>
  <summary>Items</summary>

<details>
  <summary>Roasted Spider Eye</summary>

Effectively a very cheap way of gaining weakness. \
You can gain this item by cooking a spider eye with a campfire.

This will weaken the poison, allowing consumption with only a *moderate* amount of nerve damage.
</details>
<details>
<summary>Soul Salve</summary>

A very useful item for players who have recently cured. In order to craft it, you must place 4 redstone on each side, 4 quartz in each corner, and a piece of paper in the middle.

In order to use it, it is required to be human and to have the attrition status effect.

When used, it gives 10 minutes of Alleviation. If you already have this effect it increments the duration by 10 minutes.

Additionally, it inflicts 60 soul damage. So be careful.
</details>
<details>
  <summary>Eau De Mort</summary>

Hands down the most interesting item in this mod. 

Although, it doesn't exactly have much competition.

An Eau De Mort allows you to separate from a host, keeping the items intact so that the next host will keep them.

Drinking it while human is NOT ADVISED.

You can gain this item one of two ways:
1. Finding it in loot. (Mostly nether loot)
2. Creating it.

In order to create this, you must first brew a Potion of Withering. \
To get a Potion of Withering you must brew a Wither Rose into an unmodified weakness potion (default state, duration, and amplifier).

Once you have the Potion of Withering, you must obtain a Totem of Undying.

Being saved by a Totem of Undying will convert all unmodified potions of Withering into Eau De Morts.
</details>
</details>

######
<details>
  <summary>Enchantments</summary>

<details>
  <summary>Soul Cleaving</summary>

Soul Cleaving has a maximum level of III.\
It can be gained through an enchanting table or through Nether loot.

Inflicts 30 x (level) soul damage on the victim.
</details>
<details>
  <summary>Reaping</summary>

Reaping has only one level and has two effects:

The primary effect is that it deals 3 x (Victim's Attrition level) magic damage to the victim.

The secondary effect is that it converts mobs that are on normal fire, to being on soul fire.\
It also converts flaming arrows that are shot from it into soul flaming arrows.

If you want a 'soul flaming' sword or bow, then you need to have both Reaping and their respective fire enchantment.
</details>
</details>

######
<details>
  <summary>Soul fire</summary>

This mod implements MoriyaShiine's [On Soul Fire](https://www.curseforge.com/minecraft/mc-mods/on-soul-fire) mod.
</details>

######
<details>
  <summary>Misc</summary>

* (Configurable via gamerule) Enchanted golden apples can be used to cure without weakness applied to the player.


* (Configurable via gamerule) Blocks with a blast resistance of 1200 (obsidian level) or higher cannot be phased through. Happy ghost-trapping :)


* The curing process does not wait any duration to complete.


* You cannot cure Attrition via milk or most other methods.


* You can craft bones via placing 3 bonemeal in a crafting gird.


* Spiders drop spider eyes like normal even if not killed by a player.
</details>

### Gamerules
* ``mementomori:cureAllItems`` -Controls whether or not ``cure_alls`` items can cure a possessed without the weakness debuff. [DEFAULT: true]
* ``mementomori:attritionGrowth`` -Controls whether or not attrition will increase as a mob. [DEFAULT: true]
* ``mementomori:blastResistantUnphasable`` Controls if players are not able to go through blocks with a blast resistance of 1,200 or higher. [DEFAULT: true]
* ``mementomori:attritionTime`` Controls the amount of time, in seconds, that a player has attrition for. [DEFAULT: 1200 (which is 20 minutes))]
* ``mementomori:phantomSpawnCount`` Controls the amount of phantoms that spawn per level of attrition. [DEFAULT: 1]
* ``mementomori:soulDamageDeathLevel`` Controls level of attrition at which soul damage will kill the player indiscriminately. [DEFAULT: 4, put -1 to disable]