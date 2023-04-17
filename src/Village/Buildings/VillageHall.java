package Village.Buildings;

import CustomExceptions.Exceptions;
import Village.Buildings.Defences.*;
import Village.Buildings.ResourceProduction.*;
import Village.MainVillage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * This class is for the village hall. It contains properties relating to the village resources and builders.
 * Upgrading the village hall will automatically enable the player to buy the new structures for the village hall level
 */
public class VillageHall extends Village.Buildings.Structures {

  public static final int maxLevel = 3;

  /**
   * Creates a new village hall at level 1 with all it's things at level 1
   * @param mv
   */
  public VillageHall(MainVillage mv) {
    this.name = "VillageHall";
    this.currentLevel = 1;
    this.maxHitpoints = 1000;
    this.hpMultiplier = 1.5f;
    this.cost.replace("Wood", 0, 1000);
    this.upgradeTime = 10;
    this.populationWeight = 6;
    this.baseMaxWoodStorage = 10000;
    this.baseMaxIronStorage = 750;
    this.baseMaxGoldStorage = 500;
    this.currentWoodInStorage = 1550;
    this.currentIronInStorage = 50;
    this.mainVillage = mv;
    this.symbol = "v";
    this.availableBuilders = 2;
    addStructuresToAvailable(MainVillage.LEVEL_1_AVAILABLE_STRUCTURES.keySet(), MainVillage.LEVEL_1_AVAILABLE_STRUCTURES);
    //add storage multiplier / update upgrade method
  }


  //Current wood in storage for the village
  private int currentWoodInStorage;

  //Current iron in storage for the village
  private int currentIronInStorage;

  //Current gold in storage for the village
  private int currentGoldInStorage;

  //Base max wood capacity for storage
  private int baseMaxWoodStorage;

  //Base max capacity for storage
  private int baseMaxIronStorage;

  //Base max capacity for storage
  private int baseMaxGoldStorage;

  //Multiplier for wood storage to upgrade capacity (used in upgrade method)
  private double woodStorageMultiplier;

  //Multiplier for iron storage to upgrade capacity (used in upgrade method)
  private double ironStorageMultiplier;

  //Multiplier for gold storage to upgrade capacity (used in upgrade method)
  private double goldStorageMultiplier;

  //Hashmap containing the available loot for the village (when attacked) same format as cost
  public HashMap<String, Integer> availableLoot;

  //Counter for available builders in idle
  public int availableBuilders;

  private int currentBuildingsBuilt;

  private MainVillage mainVillage;

  /**
   * upgrade village hall, adds next level's buildings to avail structures list
   */
  public void upgrade() {
    if(currentLevel < maxLevel) {
      try {
        startBuildOrUpgrade(this);
      } catch (InterruptedException e) {
        System.out.println(e.getMessage());
      }
      this.currentLevel+= 1;
      this.maxHitpoints = Math.round(maxHitpoints*hpMultiplier);
      System.out.println(this.getName() + " upgraded. Current level = " + this.currentLevel);
      if(this.currentLevel == 2) {
        addStructuresToAvailable(MainVillage.LEVEL_2_AVAILABLE_STRUCTURES.keySet(), MainVillage.LEVEL_2_AVAILABLE_STRUCTURES);
      } else if (this.currentLevel == 3) {
        addStructuresToAvailable(MainVillage.LEVEL_3_AVAILABLE_STRUCTURES.keySet(), MainVillage.LEVEL_3_AVAILABLE_STRUCTURES);
      }
    } else {
      System.out.println("Already reached max level.");
    }

  }

  /**
   * Getter method for the current wood in storage
   * @return currentWoodInStorage
   */
  public int getCurrentWoodInStorage() {
  return currentWoodInStorage;
  }

  /**
   * Getter method for the current iron in storage
   * @return currentIronInStorage
   */
  public int getCurrentIronInStorage() {
  return currentIronInStorage;
  }

  /**
   * Getter method for the current gold in storage
   * @return currentGoldInStorage
   */
  public int getCurrentGoldInStorage() {
  return currentGoldInStorage;
  }


  /**
   * Method to add or remove wood from storage
   * @param wood
   * @throws Exceptions.FullStorageException
   * @throws Exceptions.NotEnoughResourcesException
   */
  public void updateWoodInStorage(int wood) throws Exceptions.FullStorageException, Exceptions.NotEnoughResourcesException {
    if ((this.getCurrentWoodInStorage() + wood) > this.baseMaxWoodStorage) {
      this.currentWoodInStorage = this.baseMaxWoodStorage;
      throw new Exceptions.FullStorageException("Wood storage is full");
    } else if ((this.getCurrentWoodInStorage() + wood) < 0) {
      throw new Exceptions.NotEnoughResourcesException("Not enough wood in storage.");
    } else {
      this.currentWoodInStorage += wood;
      System.out.println("Added/removed " + wood + " wood. New wood storage: " + this.currentWoodInStorage + ".");
    }
  }

  /**
   * Method to add or remove iron from storage
   * @param iron
   * @throws Exceptions.FullStorageException
   * @throws Exceptions.NotEnoughResourcesException
   */
  public void updateIronInStorage(int iron) throws Exceptions.FullStorageException, Exceptions.NotEnoughResourcesException {
    if ((this.getCurrentIronInStorage() + iron) > this.baseMaxIronStorage) {
      this.currentIronInStorage = this.baseMaxIronStorage;
      throw new Exceptions.FullStorageException("Iron storage is full");
    } else if ((this.getCurrentIronInStorage() + iron) < 0) {
      throw new Exceptions.NotEnoughResourcesException("Not enough iron in storage.");
    } else {
      this.currentIronInStorage += iron;
      System.out.println("Added/removed " + iron + " iron. New iron storage: " + this.currentIronInStorage + ".");
    }
  }

  /**
   * Method to add or remove gold from storage
   * @param gold
   * @throws Exceptions.FullStorageException
   * @throws Exceptions.NotEnoughResourcesException
   */
  public void updateGoldInStorage(int gold) throws Exceptions.FullStorageException, Exceptions.NotEnoughResourcesException {
    if ((this.getCurrentGoldInStorage() + gold) > this.baseMaxGoldStorage) {
      this.currentGoldInStorage = this.baseMaxGoldStorage;
      throw new Exceptions.FullStorageException("Gold storage is full");
    } else if ((this.getCurrentGoldInStorage() + gold) < 0) {
      throw new Exceptions.NotEnoughResourcesException("Not enough gold in storage.");
    } else {
      this.currentGoldInStorage += gold;
      System.out.println("Added/removed " + gold + " gold. New gold storage: " + this.currentGoldInStorage + ".");
    }
  }


  /**
   * Getter method for the current buildings built counter
   * @return currentBuildingsBuilt
   */
  public int getCurrentBuildingsBuilt() {
  return currentBuildingsBuilt;
  }

  /**
   * Getter method for available loot
   * @return availableLoot
   */
  public HashMap getAvailableLoot() {return availableLoot;}

  /**
   * Method used to get the integer values of the resources gained and add to storage
   * @param loot
   */
  public void addRaidVictoryResources(HashMap<String, Integer> loot) {
    try {
      int woodGain = loot.get("Wood");
      int ironGain = loot.get("Iron");
      int goldGain = loot.get("Gold");
      try {
        addResourcesToStorage(woodGain, ironGain, goldGain);
      } catch (Exceptions.FullStorageException e) {
        System.out.println(e.getMessage());
      }
    } catch (NullPointerException e) {
      System.out.println(e.getMessage());
    }



  }

  /**
   * Method to add resources to the players storage
   * @param wood
   * @param iron
   * @param gold
   * @throws Exceptions.FullStorageException
   */
  public void addResourcesToStorage(int wood, int iron, int gold) throws Exceptions.FullStorageException {
    if ((this.getCurrentWoodInStorage() + wood) > this.baseMaxWoodStorage) {
      throw new Exceptions.FullStorageException("Wood storage is full.");
    } else {
      this.currentWoodInStorage += wood;
    }
    if ((this.getCurrentIronInStorage() + iron) > this.baseMaxIronStorage) {
      throw new Exceptions.FullStorageException("Iron storage is full.");
    } else {
      this.currentIronInStorage += iron;
    }
    if ((this.getCurrentGoldInStorage() + gold) > this.baseMaxGoldStorage) {
      throw new Exceptions.FullStorageException("Gold storage is full.");
    } else {
      this.currentGoldInStorage += gold;
    }
  }

  /**
   * Hmmm I wonder what this thing does...
   * Maxes villages storages with resources
   */
  public void hax() {
    currentGoldInStorage = baseMaxGoldStorage;
    currentWoodInStorage = baseMaxWoodStorage;
    currentIronInStorage = baseMaxIronStorage;
  }

  /**
   * Method to add the available structures per village hall level to the villages availableStructure list
   * @param keySet
   * @param structures
   */
  public void addStructuresToAvailable(Set<String> keySet, HashMap<String, Integer> structures) {
    // add everything in the keyset to the available structures list
    for (String key : keySet) {
      switch (key) {
        case "LumberMill":
          for(int i = 0; i < structures.get(key); i++) {
            mainVillage.addToVillage(new LumberMill(this));
          }
          break;
        case "IronMine":
          for(int i = 0; i < structures.get(key); i++) {
            mainVillage.addToVillage(new IronMine(this));
          }
          break;
        case "GoldMine":
          for(int i = 0; i < structures.get(key); i++) {
            mainVillage.addToVillage(new GoldMine(this));
          }
          break;
        case "Farm":
          for(int i = 0; i < structures.get(key); i++) {
            mainVillage.addToVillage(new Farm(this));
          }
          break;
        case "Cannon":
          for(int i = 0; i < structures.get(key); i++) {
            mainVillage.addToVillage(new Cannon(this));
          }
          break;
        case "ArcherTower":
          for(int i = 0; i < structures.get(key); i++) {
            mainVillage.addToVillage(new ArcherTower(this));
          }
          break;
      }
    }
  }

  /**
   * Simple method to check if there are any available builders
   * @return isAvailableBuilders
   */
  public boolean isAvailableBuilders() {
      if(availableBuilders != 0) {
        return true;
      } else {
        return false;
      }
  }

  /**
   * Used for when starting a build or upgrade, uses a builder
   */
  public void useBuilder() {
    availableBuilders -= 1;
    System.out.println("Builder starting job. Available builders: " + availableBuilders);
  }

  /**
   * Used for when finishing a build or upgrade, returns a builder
   */
  public void buildDone() {
    availableBuilders += 1;
    System.out.println("Builder finished job. Available builders: " + availableBuilders);
  }

  public void collectAll() {
    Arrays.stream(mainVillage.getPlacedStructures()).filter(LumberMill.class::isInstance).forEach(struc -> {
      try {
        ((LumberMill) struc).collect();
      } catch (Exceptions.NotEnoughResourcesException e) {
        System.out.println(e.getMessage());
      } catch (Exceptions.FullStorageException e) {
        System.out.println("Full Storage");
      }
    });
    Arrays.stream(mainVillage.getPlacedStructures()).filter(IronMine.class::isInstance).forEach(struc -> {
      try {
        ((IronMine) struc).collect();
      } catch (Exceptions.NotEnoughResourcesException e) {
        System.out.println(e.getMessage());
      } catch (Exceptions.FullStorageException e) {
        System.out.println("Full Storage");
      }
    });
    Arrays.stream(mainVillage.getPlacedStructures()).filter(GoldMine.class::isInstance).forEach(struc -> {
      try {
        ((GoldMine) struc).collect();
      } catch (Exceptions.NotEnoughResourcesException e) {
        System.out.println(e.getMessage());
      } catch (Exceptions.FullStorageException e) {
        System.out.println("Full Storage");
      }
    });
  }

}