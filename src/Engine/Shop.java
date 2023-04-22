package Engine;

import CustomExceptions.Exceptions;
import Village.Army.*;
import Village.Buildings.*;
import Village.Inhabitant;
import Village.MainVillage;

import java.util.ArrayList;
import java.util.List;






/**
 * Shop UI class for displaying the structures that the user can buy
 */
public class Shop {

  public enum Who {
    A, C, K, S
  }
  MainVillage mainVillage;
  //Available list of structures to buy. This is effectively entries in available Structures that bought == false
  public List<Structures> catalog;

  private NvidiaRTX4090TI rtx4090TI;





  public Shop(MainVillage mv, NvidiaRTX4090TI rtx4090TI) {
    this.rtx4090TI = rtx4090TI;
    catalog = new ArrayList<>();
    mainVillage = mv;
    // filter to only unbought
    for (Structures yes: mainVillage.getAvailableStructures()) {
      if (!yes.isBought()) {
        catalog.add(yes);
      }

    }
  }


  /**
   * Train a troop.
   * Create troop, subtract village moneys, add to village
   * @param t
   */
  public void train(Troop t) {

    try {
          isAffordable(t);
            mainVillage.villageHall.updateGoldInStorage(-(Integer) t.getCost().get("Gold"));
            mainVillage.villageHall.updateIronInStorage(-(Integer) t.getCost().get("Iron"));
            mainVillage.villageHall.updateWoodInStorage(-(Integer) t.getCost().get("Wood"));
            mainVillage.addToVillage(t);
            rtx4090TI.append("Trained " + t.getName());
        }
          catch (Exceptions.NotEnoughResourcesException e) {
          rtx4090TI.appendDebug(e.getMessage());
        } catch (Exceptions.PopulationCapReachedException e) {
          rtx4090TI.appendDebug(e.getMessage());
        } catch (Exceptions.FullStorageException e) {
          rtx4090TI.appendDebug(e.getMessage());
    }

  }



  /**
   * BUY EVERYTHING (STRUCTURES)
   * DO NOT IMPLEMENT INTO A COMMAND
   * THIS USES THE HAX INFINITE MONEY THING
   */
  public void buyAll() {
    rtx4090TI.append("Attempting to buy everything possible to buy");

    try {
      while (!catalog.isEmpty()) {
        mainVillage.villageHall.hax();
        buy(0, true, true);
      }

    } catch (Exceptions.NotEnoughResourcesException e) {
      rtx4090TI.appendDebug(e.getMessage());
    } catch (Exceptions.FullStorageException e) {
      rtx4090TI.appendDebug(e.getMessage());
    } catch (Exceptions.PopulationCapReachedException e) {
      rtx4090TI.appendDebug(e.getMessage());
    }

  }





  /**
   * Method for purchasing the structure
   */
  public void buy(int index, boolean unconditional, boolean scilent) throws Exceptions.NotEnoughResourcesException, Exceptions.FullStorageException, Exceptions.PopulationCapReachedException {
    try {
      mainVillage.updateMaxPopulation(); // force update
      Structures selection = catalog.get(index);
      if (isAffordable(selection)) {
        selection.isBought = true;
//      mainVillage. change moneys
        mainVillage.villageHall.updateGoldInStorage(-(Integer) selection.getCost().get("Gold"));
        mainVillage.villageHall.updateIronInStorage(-(Integer) selection.getCost().get("Iron"));
        mainVillage.villageHall.updateWoodInStorage(-(Integer) selection.getCost().get("Wood"));

        if (scilent) { // don't spam main window
          rtx4090TI.appendDebug("Bought: " + catalog.get(index).getName() + " ID: " + catalog.get(index).getID());
        }
        else {
          rtx4090TI.append("Bought: " + catalog.get(index).getName() + " ID: " + catalog.get(index).getID());
        }


        if (!unconditional) { // skip builder limits when in cheating mode
          rtx4090TI.updateDisplay("Starting build or upgrade for " + selection.getName() + "id=" + selection.getID() + " time in seconds: " + selection.getUpgradeTime());
          selection.startBuildOrUpgrade(mainVillage.villageHall); // start build time when bought
        }
        catalog.remove(index);
      }
    } catch (Exceptions.PopulationCapReachedException e) {
      rtx4090TI.appendDebug(e.getMessage());
    } catch (Exceptions.NotEnoughResourcesException e) {
      rtx4090TI.appendDebug(e.getMessage());
      rtx4090TI.append(e.getMessage()); // also let user see
    } catch (ArrayIndexOutOfBoundsException e) {
      rtx4090TI.appendDebug("Please provide a valid index");
    } catch (IndexOutOfBoundsException e) {
      rtx4090TI.appendDebug("Please provide a valid index");
    }

  }

  /**
   * Method to check if the user can afford the structure or not
   * @return isAffordable
   */
  public boolean isAffordable(Inhabitant str) throws Exceptions.NotEnoughResourcesException, Exceptions.PopulationCapReachedException {
    if ( mainVillage.populationLimit < (mainVillage.getTotalPopulation() + str.getPopulationWeight())) {
      throw new Exceptions.PopulationCapReachedException("Cannot afford because population limit will be violated");
    }
    if (mainVillage.villageHall.getCurrentIronInStorage() >= (int) str.getCost().get("Iron")
            && mainVillage.villageHall.getCurrentWoodInStorage() >= (int) str.getCost().get("Wood")
            && mainVillage.villageHall.getCurrentGoldInStorage() >= (int) str.getCost().get("Gold")) {
      return true;
    }
    throw new Exceptions.NotEnoughResourcesException("Cannot afford structure with id: " + str.id);
  }

  
  /**
   * Displays all the available structures to be placed in the console
   */
  public void showCatalog() throws InterruptedException {
    String outputmsg = "";
//      rtx4090TI.append(mainVillage.getDetails());
    outputmsg += mainVillage.getDetails() + "\n";
//    rtx4090TI.append("Available Structures:");
    outputmsg += "Available Structures:" + "\n";
    for (Structures struc : catalog) {
//        rtx4090TI.append("ID="+struc.getID() + " -- " + struc.getName() + " ("+struc.getSymbol()+ ")" + " -- "
//        + "Wood: " + struc.getCost().get("Wood") + " Iron: " + struc.getCost().get("Iron") + " Gold: " + struc.getCost().get("Gold"));

        outputmsg += "ID="+struc.getID() + " -- " + struc.getName() + " ("+struc.getSymbol()+ ")" + " -- "
        + "Wood: " + struc.getCost().get("Wood") + " Iron: " + struc.getCost().get("Iron") + " Gold: " + struc.getCost().get("Gold") + "\n";
    }
    // give time to read
//    rtx4090TI.updateDisplay();
//    Thread.sleep(1000);

    // temporary, for display purposes
    Archer tempA = new Archer(mainVillage, rtx4090TI);
    Catapult tempC = new Catapult(mainVillage, rtx4090TI);
    Knight tempK = new Knight(mainVillage, rtx4090TI);
    Soldier tempS = new Soldier(mainVillage, rtx4090TI);

//    rtx4090TI.append("Available Troops:");
//    rtx4090TI.append("'A' --> Archer Troop -- Wood: " + tempA.getCost().get("Wood")
//    + " Iron: " + tempA.getCost().get("Iron") + " Gold: " + tempA.getCost().get("Gold"));
//
//    rtx4090TI.append("'C' --> Catapult Troop -- Wood: " + tempC.getCost().get("Wood")
//            + " Iron: " + tempC.getCost().get("Iron") + " Gold: " + tempC.getCost().get("Gold"));
//
//    rtx4090TI.append("'K' --> Knight Troop -- Wood: " + tempK.getCost().get("Wood")
//            + " Iron: " + tempK.getCost().get("Iron") + " Gold: " + tempK.getCost().get("Gold"));
//
//    rtx4090TI.append("'S' --> Soldier Troop -- Wood: " + tempS.getCost().get("Wood")
//            + " Iron: " + tempS.getCost().get("Iron") + " Gold: " + tempS.getCost().get("Gold"));

    outputmsg += "Available Troops:" + "\n";

    outputmsg += "'A' --> Archer Troop -- Wood: " + tempA.getCost().get("Wood")
    + " Iron: " + tempA.getCost().get("Iron") + " Gold: " + tempA.getCost().get("Gold") + "\n";

    outputmsg += "'C' --> Catapult Troop -- Wood: " + tempC.getCost().get("Wood")
    + " Iron: " + tempC.getCost().get("Iron") + " Gold: " + tempC.getCost().get("Gold") + "\n";

    outputmsg += "'K' --> Knight Troop -- Wood: " + tempK.getCost().get("Wood")
    + " Iron: " + tempK.getCost().get("Iron") + " Gold: " + tempK.getCost().get("Gold") + "\n";

    outputmsg += "'S' --> Soldier Troop -- Wood: " + tempS.getCost().get("Wood")
    + " Iron: " + tempS.getCost().get("Iron") + " Gold: " + tempS.getCost().get("Gold") + "\n";

    tempA = null;
    tempC = null;
    tempK = null;
    tempS = null;


    // give time to read
//    rtx4090TI.updateDisplay();
//    Thread.sleep(2000);



    //  prints out what you can upgrade (stuff needs to be placed in order to be upgraded)
//  rtx4090TI.append("\n\n\nThe following is placed structures you can upgrade if they are not max level.  IMPORTANT: (stuff needs to be BOUGHT in order to be upgraded)");
    outputmsg += "\n\n\nThe following is placed structures you can upgrade if they are not max level.  IMPORTANT: (stuff needs to be BOUGHT in order to be upgraded)" + "\n";
    for (Structures struc : mainVillage.getPlacedStructures()) {
//      rtx4090TI.append("ID="+struc.getID() + " -- " + struc.getName() + " ("+struc.getSymbol()+ ")" + " -- "
//              + "Wood: " + Structures.UPGRADE_COST + " Iron: " + Structures.UPGRADE_COST + " Gold: " + Structures.UPGRADE_COST);

      outputmsg += "ID="+struc.getID() + " -- " + struc.getName() + " ("+struc.getSymbol()+ ")" + " -- "
      + "Wood: " + Structures.UPGRADE_COST + " Iron: " + Structures.UPGRADE_COST + " Gold: " + Structures.UPGRADE_COST + "\n";
    }


//    rtx4090TI.append("\n\nTROOP UPGRADES ARE FREE");
//    rtx4090TI.append("Upgrade troops with command for example---->> ex 'shop upgradeT A' will upgrade an archer");
//    rtx4090TI.append("'A' --> Archer Troop -- Wood: " +Troop.UPGRADE_COST
//            + " Iron: " + Troop.UPGRADE_COST + " Gold: " + Troop.UPGRADE_COST);
//
//    rtx4090TI.append("'C' --> Catapult Troop -- Wood: " + Troop.UPGRADE_COST
//            + " Iron: " + Troop.UPGRADE_COST+ " Gold: " + Troop.UPGRADE_COST);
//
//    rtx4090TI.append("'K' --> Knight Troop -- Wood: " + Troop.UPGRADE_COST
//            + " Iron: " + Troop.UPGRADE_COST + " Gold: " + Troop.UPGRADE_COST);
//
//    rtx4090TI.append("'S' --> Soldier Troop -- Wood: " + Troop.UPGRADE_COST
//            + " Iron: " + Troop.UPGRADE_COST + " Gold: " + Troop.UPGRADE_COST);

      outputmsg += "\n\nTROOP UPGRADES ARE FREE" + "\n";
      outputmsg += "Upgrade troops with command for example---->> ex 'shop upgradeT A' will upgrade an archer" + "\n";
      outputmsg += "'A' --> Archer Troop -- Wood: " +Troop.UPGRADE_COST
      + " Iron: " + Troop.UPGRADE_COST + " Gold: " + Troop.UPGRADE_COST + "\n";

      outputmsg += "'C' --> Catapult Troop -- Wood: " + Troop.UPGRADE_COST
      + " Iron: " + Troop.UPGRADE_COST+ " Gold: " + Troop.UPGRADE_COST + "\n";

      outputmsg += "'K' --> Knight Troop -- Wood: " + Troop.UPGRADE_COST
      + " Iron: " + Troop.UPGRADE_COST + " Gold: " + Troop.UPGRADE_COST + "\n";

      outputmsg += "'S' --> Soldier Troop -- Wood: " + Troop.UPGRADE_COST
      + " Iron: " + Troop.UPGRADE_COST + " Gold: " + Troop.UPGRADE_COST + "\n";
      rtx4090TI.updateDisplay(outputmsg);
  }

  /**
   * Method to upgrade troop depending on what the user enters in (in console)
   * @param troop
   */
  public void upgradeTroop(Who troop) throws EnumConstantNotPresentException{

    //Depending on the troop, it will loop through all currently trained troops and upgrade all the troops who are of same type (type of troop to be upgraded)
    switch (troop) {
      case A -> {
        if(mainVillage.archerLvl < Archer.maxLevel) {
          for (Troop t : mainVillage.getTrainedArmy()) {
            if (t instanceof Archer) {
              t.upgrade();
            }
          }
          mainVillage.archerLvl += 1;
          rtx4090TI.append("Upgraded " + troop.name() + mainVillage.archerLvl);
        }
      }
      case C -> {
        if(mainVillage.catapultLvl < Catapult.maxLevel) {
          for (Troop t : mainVillage.getTrainedArmy()) {
            if (t instanceof Catapult) {
              t.upgrade();
            }
          }
          mainVillage.catapultLvl += 1;
          rtx4090TI.append("Upgraded " + troop.name() + mainVillage.catapultLvl);
        }
      }
      case K -> {
        if(mainVillage.knightLvl < Knight.maxLevel) {
          for (Troop t : mainVillage.getTrainedArmy()) {
            if (t instanceof Knight) {
              t.upgrade();
            }
          }
          mainVillage.knightLvl += 1;
          rtx4090TI.append("Upgraded " + troop.name() + mainVillage.knightLvl);
        }
      }
      case S -> {
        if(mainVillage.soldierLvl < Soldier.maxLevel) {
          for (Troop t : mainVillage.getTrainedArmy()) {
            if (t instanceof Soldier) {
              t.upgrade();
            }
          }
          mainVillage.soldierLvl += 1;
          rtx4090TI.append("Upgraded " + troop.name() + mainVillage.soldierLvl);
        }
      }
    }
  }

  /**
   * Upgrades the players building
   * @param id
   */
  public void upgradeStructure(int id) throws Exceptions.NotEnoughResourcesException, Exceptions.FullStorageException {
    // hi. apologies for the ugliness of the code. Please refer to some other more elegant uses of exceptions... and programming in general
    if ((mainVillage.villageHall.getCurrentWoodInStorage() - Structures.UPGRADE_COST) > 0) {
      if ((mainVillage.villageHall.getCurrentIronInStorage() - Structures.UPGRADE_COST) > 0) {
        if ((mainVillage.villageHall.getCurrentGoldInStorage() - Structures.UPGRADE_COST) > 0) {
          mainVillage.villageHall.updateGoldInStorage(-Structures.UPGRADE_COST);
          mainVillage.villageHall.updateIronInStorage(-Structures.UPGRADE_COST);
          mainVillage.villageHall.updateWoodInStorage(-Structures.UPGRADE_COST);
          try { // look for structure in both arrays
            mainVillage.getPlacedStructureByID(id).upgrade();
          } catch (Exception e) {}

          try {
            mainVillage.getAvailStructureByID(id).upgrade();
          } catch (Exception e) {}


          rtx4090TI.updateDisplay("Upgraded structure with id: " + id);
          return;
        }
      }
    }

    rtx4090TI.append("You cannot afford this.");

  }

}