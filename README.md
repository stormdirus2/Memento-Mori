## A block physics system not unlike Enviromine's or the functionality of scaffolding blocks.

Blocks have a max reach distance based on their blast resistance. They can only reach out so far from a 'support' (a column of block(s) that are connected to a highly-surrounded block or a high blast resistance block) 


Blocks that are not near a support will fall when 'updated'.

#
There is also a reinforcement mechanic, its easier to learn this firsthand than to learn it from ye old bad explainer.

But basically:


As a block is searching along their 8 vectors on the x-z plane, it will upgrade it's reach to a block of higher reach as long as that block is within it's own reach, specifically in that direction.

This makes skylights easier and rewards the player for making shapes found in real architecture, like frames for glass.

#

Some blocks also search for supports upwards rather than downwards. 

The main reasoning for this is to prevent all of the nether crashing down, as well as the end islands.

Blocks with the [sticky_blocks] tag will have this behavior, in this tag there are also fences, walls, chains, iron bars, and GlassSoundGroup blocks.

#
Blocks with the [protected_blocks] tag will have a minimum of 3 block extension capability. This is to prevent trees from getting pruned completely from a single block update.

#

Additionally blocks that have a slipperiness value higher than 0.6, or that are in the [floats] tag will float on liquids (such as lava or water).

#

Confused on when/where the physics system updates the blocks?
It checks the neighbors of a position during these events:
* Player breaks a block.
* Player places a block.
* Fire destroys a block.
* Explosion destroys a block.
