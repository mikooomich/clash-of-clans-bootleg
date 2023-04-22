How to play the game

You will start with a lvl 1 Village hall and the building, and an amount of resources.
You must buy your buildings first. Use the "shop buy x" command  ("shop buyall" will buy all buildings in catalog, this is a cheat you can conviently abuse)
Your buildings will not be placed automatically, use the editor commands to place stuff.
Shop and editor have commands to get what is available, see manual for full list of commands.

Searching for new villages to attack: use next command until found a desirable village, will generate your equivalent village hall of village.
Initiate the attack with attack command or simulate a defence with the simdefence command.






A sample of commands to play is as follows

// buy all (using cheats), then generate a random layout from the structure you now have. Then see your layout
shop buyall
editor rng
map

// search for a village, attack
next
next
next
next
attack

// search for a village, they attack you
next
simdefence


// upgrade to the next villagehall, then upgrade a few other buildings/troops
// ALL OF THESE NEED (buildings need to be placed to be upgraded)
// If you do not have enough resource type without quotations: "cheat"
shop catalog (view what can be upgraded/bought)
shop upgradeS 7 (villageHall ID)
shop upgradeS 1 (another structure ID)
shop upgradeT A (upgrade all trained Archers)
shop upgradeT K (upgrade all trained Knight)
shop collect (collects all resource buildings)



EXTRAS:
// auto make add an army to your village to test challenge adapters
testchallenge

// automatically starts build/upgrade on 3 structures, 3rd should fail because 2 builder limit.
multibuildertest
