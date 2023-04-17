package Village;

import CustomExceptions.Exceptions;
import Village.Army.Archer;
import Village.Army.Catapult;
import Village.Buildings.ResourceProduction.*;
import Village.Buildings.Structures;
import Village.Army.Troop;
import Village.Buildings.VillageHall;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main class for a village. Contains properties for a village and for the UI to access. (ex. wins/losses, population, guard time, ect)
 */
public class MainVillage{

  public static final HashMap<String, Integer> STARTING_STRUCTURES = new HashMap<>() {
    {
      put("VillageHall", 1);
      put("LumberMill", 1);
      put("Farm", 1);
    }
  };

  public static final HashMap<String, Integer> LEVEL_1_AVAILABLE_STRUCTURES = new HashMap<>() {
    {
      put("LumberMill", 1);
      put("IronMine", 1);
      put("GoldMine", 1);
      put("Farm", 2);
      put("Cannon", 1);
      put("ArcherTower", 1);
    }
  };

  public static final HashMap<String, Integer> LEVEL_2_AVAILABLE_STRUCTURES = new HashMap<>() {
    {
      put("LumberMill", 1);
      put("IronMine", 1);
      put("GoldMine", 1);
      put("Farm", 1);
      put("Cannon", 2);
      put("ArcherTower", 2);
    }
  };

  public static final HashMap<String, Integer> LEVEL_3_AVAILABLE_STRUCTURES = new HashMap<>() {
    {
      put("LumberMill", 1);
      put("IronMine", 1);
      put("GoldMine", 1);
      put("Cannon", 1);
      put("ArcherTower", 1);
    }
  };


  public static final int MAP_SIZE = 30; // assume square

  public int archerLvl = 1; //Used for new instances of archers
  public int catapultLvl = 1; //Used for new instances of catapults
  public int knightLvl = 1; //Used for new instances of knights
  public int soldierLvl = 1; //Used for new instances of soldiers

  public boolean isOnGuard;


  //Remaining guard time for village
  private float currentGuardTime;

  //Total population the village can currently hold (based on village hall level)
  private int totalPopulation;


  public VillageHall villageHall;

  //Array containing current available structures
  private List<Structures> availableStructures;
  //Array of placed structures in the village
  private List<Structures> placedStructures;
  //Array of the current trained army in the village
  private List<Troop> trainedArmy;
  // if we want to implement defending troops
  private List<Troop> defensiveArmy;

  //Village total wins
  private int totalWins;

  //Village total losses
  private int totalLosses;

  //Village total matches
  private int totalMatches;

  //Max buildings to be placed (for the current village hall level)
  public int maxBuildings;

  //Time stamp for when the guard time is supposed to end
  public double guardEndTime; // this should be time stamp type

  public int populationLimit; // limit for current village population

  public Archer mvArcher = new Archer(this);

  public Catapult mvCatapult = new Catapult(this);

  public  static final int BASE_POP = 30;

  public MainVillage() {
    placedStructures = new ArrayList<>();
    availableStructures = new LinkedList<>();
    trainedArmy = new ArrayList<>();
    defensiveArmy = new ArrayList<>();
    isOnGuard = false;



    populationLimit = BASE_POP; // base pop limit

    villageHall = new VillageHall(this);

    addToVillage(villageHall);
    addToVillage(new LumberMill(this.villageHall));
    addToVillage(new Farm(this.villageHall));

    // dump initial structures in array
  }











  /**
   * Getter method for remaining guard time
   * @return remainingGuardTime
   */
  public double getRemainingGuardTime() {
  return guardEndTime;
  }

  /**
   * Getter method for total population
   * @return totalPopulation
   */
  public int getTotalPopulation() {
    int pop = 0;

    for (Inhabitant thing: availableStructures) {
      pop += thing.getPopulationWeight();
    }
    for (Inhabitant thing: trainedArmy) {
      pop += thing.getPopulationWeight();
    }

    totalPopulation = pop;
    return totalPopulation;
  }

  /**
   * Getter method for placed structures array
   * Hi. It may have been smarter to return list but that would require breaking many things in BattleSim I do not wish to fix
   *
   * @return placedStructures[]
   */
  public Structures[] getPlacedStructures() {return placedStructures.toArray(new Structures[placedStructures.size()]);}

  /**
   * Getter method for trained army array
   * @return trainedArmy[]
   */
  public Troop[] getTrainedArmy() {return trainedArmy.toArray(new Troop[trainedArmy.size()]);}

  /**
   * Getter method for defensive army array
   * @return defensiveArmy[]
   */
  public Troop[] getDefensiveArmy() {return defensiveArmy.toArray(new Troop[defensiveArmy.size()]);}

  /**
   * Getter method for available structures array
   *
   * @return availableStructures[]
   */
  public Structures[] getAvailableStructures(){return availableStructures.toArray(new Structures[availableStructures.size()]);}




  /**
   * Getter method for total wins
   * @return totalWins
   */
  public int getTotalWins() {
  return totalWins;
  }

  /**
   * Getter method for total losses
   * @return totalLosses
   */
  public int getTotalLosses() {
  return totalLosses;
  }

  /**
   * Getter method for total matches
   * @return totalMatches
   */
  public int getTotalMatches() {
  return totalMatches;
  }


  /**
   * Getter method for max buildings counter
   * @return maxBuildings
   */
  public int getMaxBuildings() {
  return maxBuildings;
  }







  /**
   * Get the map in array representation
   * This is essentially the same as the battlesim method but without the troop part
   * @return
   */
  public String[][] getMap () {
    String[][] psudoMap = new String[MAP_SIZE][MAP_SIZE];

    // check if there is an inhabitant on ever integer coordinate pair, if there is add it to psudo map
    for (int i = 0; i < psudoMap.length; i ++) { // y
      for (int j = 0; j < psudoMap[i].length; j++) { //x
        int finalJ = j;
        int finalI = i;

       Arrays.stream(getPlacedStructures()).filter(entity -> Math.round(entity.getXPos()) == finalJ && Math.round(entity.getYPos()) == finalI).forEach(thing -> {
          // structures have a size 3x3. Make a 1 wide perimeter around
          for (int y = finalI-1; y < finalI+2; y ++) { // y
            for (int x = finalJ-1; x <finalJ+2; x++) { //x
              psudoMap[y][x] = thing.getSymbol();
            }
          }
//         psudoMap[finalI][finalJ] = String.valueOf(thing.getID());
        } );

      }
    }

    return psudoMap;
  }

  public String getDetails() {
    String details = "Village hall level: " + villageHall.getCurrentLevel() + " population: " + totalPopulation + "/" + populationLimit +"\n" +
            "Guard status: " + isOnGuard + "\n" +
            "Army size: " + trainedArmy.size() + " Placed structure count: " + placedStructures.size() + " Available structure count: " + availableStructures.size() +"\n" +
            "Resources: Gold: " + villageHall.getCurrentGoldInStorage() + " Iron: " + villageHall.getCurrentIronInStorage() + "Wood: " + villageHall.getCurrentWoodInStorage();
    return details;
  }





  /**
   * -----------------------------------------------------------
   * Village editor use
   * -----------------------------------------------------------
   */



  /**
   * Remove the available structure with the corresponding object, add it to placed structures
   * @param x
   * @param y
   * @param structure object
   */
  public void addPlacedStructure(int x, int y, Structures structure) {
    addPlacedStructure(x, y, structure.getID());
  }

  /**
   * Remove the available structure with the corresponding id, add it to placed structures
   * @param x
   * @param y
   * @param id id of structure
   */
  public void addPlacedStructure(int x, int y, int id) {
    if (availableStructures.size() <= 0) {
      System.out.println("You don't have any structures that can be placed");
      return;
    }

    int index = availableStructures.indexOf(
          // what this disgusting mess does is get the index in the available structures list
            //  of the corresponding structure (id)
            availableStructures.stream().filter(structure -> structure.getID() == id).findFirst().get()
    );

    // assign x,y then move to placed array
    availableStructures.get(index).xPos = x;
    availableStructures.get(index).yPos = y;
    placedStructures.add(availableStructures.remove(index));
  }



  /**
   * Remove the placed structure object, add it back to available structures
   * @param structure
   */
  public void removePlacedStructure(Structures structure) {
    removePlacedStructure(structure.getID());
  }

  /**
   * Remove the placed structure with the corresponding id, add it back to available structures
   * @param id
   */
  public void removePlacedStructure(int id) {
    if (placedStructures.size() <= 0) {
      System.out.println("You don't have any structures that can be removed");
      return;
    }

    int index = placedStructures.indexOf(
            // what this disgusting mess does is get the index in the PLACED structures list
            //  of the corresponding structure (id)
                    placedStructures.stream().filter(structure -> structure.getID() == id).findFirst().get()
            );

    // reset x and y when removing
    placedStructures.get(index).xPos = -1;
    placedStructures.get(index).yPos = -1;
    availableStructures.add(placedStructures.remove(index));
  }






  /**
   * Search the list for placed structure with the corresponding id
   * @param id
   * @return
   */
  public Structures getPlacedStructureByID(int id) {
    // Gets the placed structure object of the corresponding structure (id) and returns it
  return placedStructures.stream().filter(structure -> structure.getID() == id).findFirst().get();
  }

  /**
   * Search the list for Available structures with the corresponding id
   * @param id
   * @return
   */
  public Structures getAvailStructureByID(int id) {
    // Gets the available structure object of the corresponding structure (id)
    return availableStructures.stream().filter(structure -> structure.getID() == id).findFirst().get();
  }


  /**
   * Add the given inhabitant to the corresponding list
   * This is used to add structures to available structures list
   * AND to train add troops to army
   *
   * The following method (in mainvillage class) adds an inhabitant to the corresponding troop/structure list.
   * First by checking the instanceof type of the inhabitant to select a list, determining the id to assign to the inhabitant, then adding to the array.
   * See document for more details
   * @param inhabitant
   * @param <E>
   */
  public <E extends Inhabitant> void addToVillage(E inhabitant) {
      // select corresponding list of type
      List<E> list;
      if (inhabitant instanceof Troop) {
        list = (List<E>) trainedArmy;
      } else {
        list = (List<E>) availableStructures;
      }

      // assign structure an id of next available id starting from 0
      int nextAvailID = 0;
      for (E thingInList : list) {
        if (thingInList.id >= nextAvailID) {
          nextAvailID = thingInList.id+ 1;
        }
      }

      // set id, add
      inhabitant.id = nextAvailID;
      list.add(inhabitant);


  }

//  /**
//   * If the population limit is not reached, this method increases the village population depending on the inhabitant
//   * @throws Exceptions.PopulationCapReachedException
//   */
//  public void addToPopulation(Inhabitant i) throws Exceptions.PopulationCapReachedException {
//    updateMaxPopulation();
//
//
//    if(totalPopulation + i.populationWeight > populationLimit) {
//      throw new Exceptions.PopulationCapReachedException("Cannot add " + i.getName() + ". Population is too high.");
//    } else {
//      this.totalPopulation += i.populationWeight;
//      System.out.println("Added 1 " + i.getName() + ". Level = " + i.getCurrentLevel() + ". Total Pop = " + this.totalPopulation);
//    }
//
//  }

  /**
   * At the end of a battle uses, update army
   * @param attackerArsenal
   */
  public void updateTrainedArmy(List<Inhabitant> attackerArsenal) {
    trainedArmy = new ArrayList<>();
    attackerArsenal.forEach(thing -> trainedArmy.add((Troop) thing));
  }


  public void updateMaxPopulation () {
    AtomicInteger capacity = new AtomicInteger();
    // just take available structures, they dont have to be placed down. however many farms you bought = your limit
    availableStructures.stream().filter(Farm.class::isInstance).forEach(entity -> { // no implementation for defending troops
      // sums up capacity based on number of farms
      capacity.addAndGet(((Farm) entity).getFoodFromFarm());
    });

    populationLimit = BASE_POP + capacity.get(); // base + calculated
  }


}