package Engine;

import Village.Army.Troop;
import Village.Buildings.Defences.DefenceStructures;
import Village.Buildings.ResourceProduction.Farm;
import Village.Buildings.ResourceProduction.Production;
import Village.Buildings.Structures;
import Village.Buildings.VillageHall;
import Village.Inhabitant;
import Village.MainVillage;


import static Engine.UserInterface.drawMap;

public class NvidiaRTX4090TI {
    /**
     * The "view" part of MVC
     *
     * Tl;Dr: prints details of objects
     * instead of having to manually handle prints, do it here
     */

    private String output = "";
    private String debugOut = "";

    WorkerThread communicationThread;


    /**
     * Blank
     */
    public NvidiaRTX4090TI() {
    }

    /**
     * Compile village info
     * @param village
     */
    public void printMap(MainVillage village) {
        output += village.getDetails();
        output += "\n--------PRINTING MAP---------\n";
        output += drawMap(village.getMap());
        output += "\n--------PRINTING END---------\n";
        updateDisplay();
    }

    /**
     * Compile Inhabitant details
     * @param entity
     */
    public void printInhabitant(Inhabitant entity) {
        // all inhabitant things
        output += entity.getName() + "(" + entity.getSymbol()+ ") " + "id="+ entity.getID() +"\n";
        output += "Hp: " + entity.maxHitpoints + " Pop. weight: " + entity.getPopulationWeight() + " Location (x,y): (" + entity.getYPos() + "," + entity.getXPos() + ")\n";
        output += "Level: "+ entity.getCurrentLevel() +" Upgrading: " + entity.isUpgrading() + " Remaining time: " + entity.remainingUpgradeTime + " Upgrade time (next level): " + entity.getUpgradeTime() + " Cost to next Lvl: " + entity.getCost() + "\n";

        // all structures details
        if (!(entity instanceof Troop)) {
            Structures thing = (Structures) entity;
            output += "Bought: " + thing.isBought() + " Placed: " +"not impl" + "\n";
        }


        if (entity instanceof Troop) {
            Troop thing = (Troop) entity;
            output += "Damage: " + thing.getDamage() + " Range: " +thing.getRange() + " Movement speed: " +thing.movementSpeed + "\n";

        } else if (entity instanceof DefenceStructures) {
            DefenceStructures thing = (DefenceStructures) entity;
            output += "Damage: " + thing.getDamage() + " Range: " +thing.getRange() + " Movement speed: " + " Size: 3x3" + "\n";

        } else if (entity instanceof Production) {
            Production thing = (Production) entity;
            output += "Production rate: " + thing.productionRate + " Producing: " + thing.isUpgrading() + " Movement speed: " + " Size: 3x3" + "\n";

            if (entity instanceof Farm yes) {
                output += "Population supported: " + yes.getFoodFromFarm() + "\n";
            }

        } else if (entity instanceof VillageHall) {
            VillageHall thing = (VillageHall) entity;
            output += "Gold: " + thing.getCurrentGoldInStorage() + "/" + thing.baseMaxGoldStorage
                    + " Iron: " + thing.getCurrentIronInStorage() + "/" + thing.baseMaxIronStorage
                    + " Wood: " + thing.getCurrentWoodInStorage() + "/" + thing.baseMaxWoodStorage + "\n";
            output += " Builders: " + thing.availableBuilders + "/" + thing.maxBuilders + "\n";
        } else {
            output += "Invalid type to print specified";
        }


        updateDisplay();
    }


    /**
     * Print and flush the framebuffer.
     *
     * @return
     */
    public void updateDisplay() {
//        System.out.println(output);
        updateDisplay(output);
        output = "";
    }

    /**
     * Print directly. This will send the message immediately to the client.
     *
     * @return
     */
     public void updateDisplay(String line) {
//         System.out.println(line);
         try {
//             System.out.println(line + " in updatedisplay");
             communicationThread.takeInMessage(line); // add to queue of messages to send
         }
         catch (NullPointerException e) {
             System.out.println("No link to where to send the message to: " + e.getMessage());
         }

     //    communicationThread.notify(); // wake up message sender
    }


    /**
     * Add the content in the form of a new line to the framebuffer.
     * @param input
     */
    public void append(String input) {
        output += input + "\n";
    }


    /**
     * Set a link so we can send the content to the thread in charger of communicating with client.
     * @param link
     */
    public void setThreadLink(WorkerThread link) {
        this.communicationThread = link;
    }










//
//    /**
//     * Add the content in the form of a new line to the debug framebuffer.
//     * @param input
//     */
    public void appendDebug(String input) {
        // ignore this
//        debugOut += input + "\n";
    }
//
//    /**
//     * Flush debug
//     * @return
//     */
//    void debugUpdateDisplay() {
//        System.out.println("\n---debug---\n" + debugOut + "\n---debug---\n");
//        debugOut = "";
//    }
//
//    public void debugUpdateDisplay(String line) {
//        System.out.println(line);
//    }

}
