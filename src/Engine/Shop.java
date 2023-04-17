package Engine;

import CustomExceptions.Exceptions;
import Village.Army.*;
import Village.Buildings.*;
import Village.Buildings.Defences.*;
import Village.Buildings.ResourceProduction.*;
import Village.Inhabitant;
import Village.MainVillage;

import java.util.ArrayList;
import java.util.List;

/**
 * Shop UI class for displaying the structures that the user can buy
 */
public class Shop {
  MainVillage mainVillage;
  //Available list of structures to buy. This is effectively entries in available STructures that bought == false
  public List<Structures> catalog;



  enum Who {
    A, C, K, S
  }



  public Shop(MainVillage mv) {
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
   * @param capitalOfFirstLetterOfUnit
   */
  public void train(Who capitalOfFirstLetterOfUnit) throws Exceptions.FullStorageException {

    switch (capitalOfFirstLetterOfUnit) {

      case A -> { Troop wah = new Archer(mainVillage);
        try {
            isAffordable(wah);
            mainVillage.villageHall.updateGoldInStorage(-(Integer) wah.getCost().get("Gold"));
            mainVillage.villageHall.updateIronInStorage(-(Integer) wah.getCost().get("Iron"));
            mainVillage.villageHall.updateWoodInStorage(-(Integer) wah.getCost().get("Wood"));
            mainVillage.addToVillage(wah);
//            mainVillage.addToPopulation(wah);
        }
       catch (Exceptions.NotEnoughResourcesException e) {
          System.out.println(e.getMessage());
        } catch (Exceptions.PopulationCapReachedException e) {
          System.out.println(e.getMessage());
        }
      }

      case C -> { Troop wah = new Catapult(mainVillage);
        try {
          isAffordable(wah);
            mainVillage.villageHall.updateGoldInStorage(-(Integer) wah.getCost().get("Gold"));
            mainVillage.villageHall.updateIronInStorage(-(Integer) wah.getCost().get("Iron"));
            mainVillage.villageHall.updateWoodInStorage(-(Integer) wah.getCost().get("Wood"));
            mainVillage.addToVillage(wah);
//            mainVillage.addToPopulation(wah);
        }
        catch (Exceptions.NotEnoughResourcesException e) {
          System.out.println(e.getMessage());
        } catch (Exceptions.PopulationCapReachedException e) {
          System.out.println(e.getMessage());
        }
      }

      case K -> { Troop wah = new Knight(mainVillage);
        try {
          isAffordable(wah);
            mainVillage.villageHall.updateGoldInStorage(-(Integer) wah.getCost().get("Gold"));
            mainVillage.villageHall.updateIronInStorage(-(Integer) wah.getCost().get("Iron"));
            mainVillage.villageHall.updateWoodInStorage(-(Integer) wah.getCost().get("Wood"));
            mainVillage.addToVillage(wah);
//            mainVillage.addToPopulation(wah);
        }
        catch (Exceptions.NotEnoughResourcesException e) {
          System.out.println(e.getMessage());
        } catch (Exceptions.PopulationCapReachedException e) {
          System.out.println(e.getMessage());
        }
      }

      case S -> { Troop wah = new Soldier(mainVillage);
        try {
          isAffordable(wah);
            mainVillage.villageHall.updateGoldInStorage(-(Integer) wah.getCost().get("Gold"));
            mainVillage.villageHall.updateIronInStorage(-(Integer) wah.getCost().get("Iron"));
            mainVillage.villageHall.updateWoodInStorage(-(Integer) wah.getCost().get("Wood"));
            mainVillage.addToVillage(wah);
//            mainVillage.addToPopulation(wah);
        }
        catch (Exceptions.NotEnoughResourcesException e) {
          System.out.println(e.getMessage());
        } catch (Exceptions.PopulationCapReachedException e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }




  /**
   * BUY EVERYTHING (STRUCTURES)
   * DO NOT IMPLEMENT INTO A COMMAND
   * THIS USES THE HAX INFINITE MONEY THING
   */
  public void buyAll() {

//    catalog.forEach(structure -> {
    try {
      while (!catalog.isEmpty()) {
        mainVillage.villageHall.hax();
        buy(0);
      }

    } catch (Exceptions.NotEnoughResourcesException e) {
      System.out.println(e.getMessage());
    } catch (Exceptions.FullStorageException e) {
      System.out.println(e.getMessage());
    } catch (Exceptions.PopulationCapReachedException e) {
      System.out.println(e.getMessage());
    }
//    });
  }





  /**
   * Method for purchasing the structure
   */
  public void buy(int index) throws Exceptions.NotEnoughResourcesException, Exceptions.FullStorageException, Exceptions.PopulationCapReachedException {
    try {
      mainVillage.updateMaxPopulation(); // force update
      Structures selection = catalog.get(index);
      if (isAffordable(selection)) {
        selection.isBought = true;

//      mainVillage. change moneys
        mainVillage.villageHall.updateGoldInStorage(-(Integer) selection.getCost().get("Gold"));
        mainVillage.villageHall.updateIronInStorage(-(Integer) selection.getCost().get("Iron"));
        mainVillage.villageHall.updateWoodInStorage(-(Integer) selection.getCost().get("Wood"));

        System.out.println("Bought: " + catalog.get(index).getName() + " ID: " + catalog.get(index).getID());
//        mainVillage.addToPopulation(catalog.get(index));
//        mainVillage.updateMaxPopulation();
        catalog.remove(index);
      }
    } catch (Exceptions.PopulationCapReachedException e) {
      System.out.println(e.getMessage());
    } catch (Exceptions.NotEnoughResourcesException e) {
      System.out.println(e.getMessage());
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Please provide a valid index");
    } catch (IndexOutOfBoundsException e) {
    System.out.println("Please provide a valid index");
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
  public void showCatalog() {
    System.out.println(mainVillage.getDetails());
    System.out.println();
    System.out.println("Available Structures:");
    for (Structures struc : catalog) {
      System.out.println("ID="+struc.getID() + " -- " + struc.getName() + " ("+struc.getSymbol()+ ")" + " -- "
      + "Wood: " + struc.getCost().get("Wood") + " Iron: " + struc.getCost().get("Iron") + " Gold: " + struc.getCost().get("Gold"));
    }
    System.out.println();

    Archer tempA = new Archer(mainVillage);
    Catapult tempC = new Catapult(mainVillage);
    Knight tempK = new Knight(mainVillage);
    Soldier tempS = new Soldier(mainVillage);

    System.out.println("Available Troops:");
    System.out.println("'A' --> Archer Troop -- Wood: " + tempA.getCost().get("Wood")
    + " Iron: " + tempA.getCost().get("Iron") + " Gold: " + tempA.getCost().get("Gold"));

    System.out.println("'C' --> Catapult Troop -- Wood: " + tempC.getCost().get("Wood")
            + " Iron: " + tempC.getCost().get("Iron") + " Gold: " + tempC.getCost().get("Gold"));

    System.out.println("'K' --> Knight Troop -- Wood: " + tempK.getCost().get("Wood")
            + " Iron: " + tempK.getCost().get("Iron") + " Gold: " + tempK.getCost().get("Gold"));

    System.out.println("'S' --> Soldier Troop -- Wood: " + tempS.getCost().get("Wood")
            + " Iron: " + tempS.getCost().get("Iron") + " Gold: " + tempS.getCost().get("Gold"));

    tempA = null;
    tempC = null;
    tempK = null;
    tempS = null;


    //  prints out what you can upgrade (stuff needs to be placed in order to be upgraded)
    System.out.println("\n\n\nThe following is placed structures you can upgrade if they are not max level.  IMPORTANT: (stuff needs to be placed in order to be upgraded)");
    for (Structures struc : mainVillage.getPlacedStructures()) {
      System.out.println("ID="+struc.getID() + " -- " + struc.getName() + " ("+struc.getSymbol()+ ")" + " -- "
              + "Wood: " + Structures.UPGRADE_COST + " Iron: " + Structures.UPGRADE_COST + " Gold: " + Structures.UPGRADE_COST);
    }

    System.out.println("\n\nTROOP UPGRADES ARE FREE");
    System.out.println("Upgrade troops with command for example---->> ex 'shop upgradeT A' will upgrade an archer");
    System.out.println("'A' --> Archer Troop -- Wood: " +Troop.UPGRADE_COST
            + " Iron: " + Troop.UPGRADE_COST + " Gold: " + Troop.UPGRADE_COST);

    System.out.println("'C' --> Catapult Troop -- Wood: " + Troop.UPGRADE_COST
            + " Iron: " + Troop.UPGRADE_COST+ " Gold: " + Troop.UPGRADE_COST);

    System.out.println("'K' --> Knight Troop -- Wood: " + Troop.UPGRADE_COST
            + " Iron: " + Troop.UPGRADE_COST + " Gold: " + Troop.UPGRADE_COST);

    System.out.println("'S' --> Soldier Troop -- Wood: " + Troop.UPGRADE_COST
            + " Iron: " + Troop.UPGRADE_COST + " Gold: " + Troop.UPGRADE_COST);




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
          mainVillage.getPlacedStructureByID(id).upgrade();
          return;
        }
      }
    }

    System.out.println("You cannot afford this");

  }

}