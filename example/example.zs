//importing and declaring variables.
import mods.draconicalchemy.BlastWave;
var default as BlastWave;
var test as BlastWave;

//Default blast wave is the normal one produced by a reactor detonation
default = BlastWave.getDefaultBlastWave() as BlastWave;

//Colours for the nuclear symbol used in JEI integration
default.setColour1 = 0x000000;
default.setColour2 = 0xffffff;

//transmutation is the blastwave turning one block into another
//transmutation is added with addTransmutation(BlockStateMatcher input, int power, blockstate output)
default.addTransmutation(<blockstate:minecraft:iron_ore> | <blockstate:minecraft:gold_ore>, 10, <blockstate:minecraft:dirt>);

//deposits are left behind when the blast terminates.  defaults to air if empty, add air to make it not guaranteed
//add with addDeposition(blockstate, weight)
default.addDeposition(<blockstate:minecraft:dirt>, 5);
default.addDeposition(<blockstate:minecraft:grass>, 5);

//remove with removeDeposition(blockstate) (I'm not sure why I made this)
default.removeDeposition(<blockstate:minecraft:air>);

//new blastwave types can be declared
//name is the name of the blastwave
//destructive = false prevents the blastwave from destroying blocks
//use BlastWave.newBlastWave(string name, boolean destructive)
test = BlastWave.newBlastWave("charcoal", true);

test.setColour1 = 0x5e5e5f;
test.setColour2 = 0xd0b100;

//Polarisers convert one blast wave type to another
//each block has a weight, with conversion occuring after that blast wave consumes 100 total weight
//create a polariser with (BlastWave)input.addPolariser(BlockstateMatcher input, int power, BlastWave output)
default.addPolariser(<blockstate:minecraft:furnace>.matchBlock(), 100, test);
//Furnaces are an example of where it's not obvious how to handle blockstates for all blocks.
//The different facing directions are different blockstates and so need a matcher

//different blast waves can have different transmutations
test.addTransmutation(<blockstate:minecraft:log>.matchBlock(), 1, <blockstate:minecraft:coal_block>);
//logs are another example of multiple block states you probably want to capture.
//wool colours (or i believe thermal expansion metal blocks) are an example where you may not want to

//Immunities can be added for specific blockstates
test.addImmunity(<blockstate:minecraft:leaves>.matchBlock());