package Engine;

import Village.*;
import Village.Army.Troop;
import Village.Buildings.Defences.DefenceStructures;

import java.text.DecimalFormat;
import java.util.*;


import static Engine.Editor.rng;
import static Engine.UserInterface.drawMap;
import static Village.MainVillage.MAP_SIZE;

/**
 * Class used for simulating a battle against an attacker and opponent.
 */
public class VeryAccurateBattleSimulator {
  // controls for simulator speed/length limit
  public static final int TICK_SPEED = 10;
  public static final int MAX_TIME_SECONDS = 20;

  // do not touch
  public static final int MAX_ALLOWED_TICKS = TICK_SPEED * MAX_TIME_SECONDS; // for battle time limit
  public static final double PAUSE_TIME = Math.floor((Double.valueOf(1)/ TICK_SPEED) * 1000); // pause time between ticks in ms
  private final NvidiaRTX4090TI rtx4090TI;


  boolean realtime = true; // realtime attack simulation
  private int currentTickCount; // current tick

  enum Who {
    ATTACKER, DEFENDER
  }



  public MainVillage attacker;  //Attacker village
  public MainVillage defender;  //Defender village

  private List<Inhabitant> attackerArsenal;
  private List<Inhabitant> defenderArsenal;



  /**
   * Create a battle simulator object
   *
   * @param attacker  MainVillage of attacker
   * @param defender  MainVillage of defender
   * @param realtime  set true for realtime simulation, else the simulation will run at full speed
   * @param rtx4090TI
   * @throws InterruptedException
   */
  public VeryAccurateBattleSimulator(MainVillage attacker, MainVillage defender, boolean realtime, NvidiaRTX4090TI rtx4090TI) {
    if (defender == null || defender.isOnGuard) {
      rtx4090TI.updateDisplay("ERROR, DEFENDER MUST BE NOT ON GUARD AND MUST EXIST. PLEASE RUN 'next' TO MAKE IT NOT NULL");
    }


    rtx4090TI.append("ONE TICK EVERY THIS AMOUNT OF MILLISECONDS: " + PAUSE_TIME);
    rtx4090TI.append("MAX TIME LIMIT IN SECONDS: " + MAX_TIME_SECONDS);


    this.defender = defender;
    this.attacker = attacker;
    this.realtime = realtime;
    this.rtx4090TI = rtx4090TI;
    currentTickCount = 0;

    attackerArsenal = new ArrayList<>();
    defenderArsenal = new ArrayList<>();

    // add everything to temp arrays
    Collections.addAll(attackerArsenal, attacker.getTrainedArmy());
    Collections.addAll(defenderArsenal, defender.getPlacedStructures());
//    Collections.addAll(defenderArsenal, defender.getDefensiveArmy()); // if want defensive army


    // everything will start at full HP
    for (Inhabitant entity: attackerArsenal) {
      entity.currentHitpoints = entity.maxHitpoints;
      ((Troop) entity).unDeploy();
    }
    for (Inhabitant entity: defenderArsenal) {
      entity.currentHitpoints = entity.maxHitpoints;
    }

    rtx4090TI.append("Initialization finish\n\n");

    rtx4090TI.updateDisplay();
  }










  /**
   * Start the tickloop of the simulation
   * Until one side loses or time is up, whichever comes first
   * @throws InterruptedException
   */
  public void startSim() throws InterruptedException {
    rtx4090TI.append("\n\n\n-------------PRINTING DETAILS BEFORE START SIMULATOR-----------------");
    rtx4090TI.append("\n\nAttacker details");
    rtx4090TI.append(attacker.getDetails());
    rtx4090TI.append("\n\nDefender details");
    rtx4090TI.append(defender.getDetails());
    rtx4090TI.append("-------------PRINTING END-----------------\n\n");
    rtx4090TI.updateDisplay();


    rtx4090TI.append("Attacker army size: \n" + attackerArsenal.size());
    rtx4090TI.append("Defender structure size: " + defenderArsenal.size());
    rtx4090TI.append("STARTING WITH THIS MAP-----------------------");
    rtx4090TI.append(drawMap(getMap())); // draw map before starting (faceroll purposes)
    rtx4090TI.append("MAP PRINT END---------------------------------");
    rtx4090TI.updateDisplay();

    rtx4090TI.updateDisplay("SIMULATING... \n");
    // tick loop, until one side loses or time is up, whichever comes first
    while (defenderArsenal.size() > 0 && attackerArsenal.size() > 0 && currentTickCount < MAX_ALLOWED_TICKS) {
      tick();
      currentTickCount++;

      // progress display
      if (realtime && currentTickCount % 30 == 0) {
        rtx4090TI.updateDisplay(currentTickCount + "/" + MAX_ALLOWED_TICKS);
//        System.out.println(drawMap());
      }
      if (realtime) {
        Thread.sleep((long) PAUSE_TIME); // realtime.
      }
    }

    end();
  }


  /**
   * Ends the battle
   */
  public void end() {
    rtx4090TI.append("\n\n\ntotal ticks:" + currentTickCount);
    rtx4090TI.append("Attacker army size: " + attackerArsenal.size());
    rtx4090TI.append("Defender structure size: " + defenderArsenal.size());
    rtx4090TI.append("FINAL RESULT MAP\n--------------------------------------------------");
    rtx4090TI.append("OVERLAPPING TROOPS WILL SHOW AS ONE UNIT");
    rtx4090TI.append(drawMap(getMap()));
    rtx4090TI.append("END MAP\n--------------------------------------------------");
    rtx4090TI.updateDisplay();
    calculateRewards();

    // modify attacker's troop array here
    attacker.updateTrainedArmy(attackerArsenal);

    // forcibly wipe the object since we are done, regardless of if interfaces is using this still
    attackerArsenal = null;
    defenderArsenal = null;
    defender = null;
    attacker = null;
  }


  /**
   * Method for attacker to place a troop at a specific location
   *
   * @param x
   * @param y
   * @param troop
   */
  public void placeTroop(int x, int y, Troop troop) {
     /*
      troops in attacker arsenal are initialized to deployed = false,
       */

    troop.deploy(); // set deployed state
    troop.xPos = x;
    troop.yPos = y;
  }


  /**
   * Used to simulate an attack on an opponent's village using our state-of-the-art placement algorithm
   */
  public void autoPlace() {faceRoll();}


  /**
   * dump all troops at random co-ordinate
   */
  public void faceRoll() {
    for (Inhabitant troop:attackerArsenal) {
      int x = rng(0, MAP_SIZE);
      int y = rng(0, MAP_SIZE);
      // for now just a dumb "place a random thing in a random place"
      placeTroop(x, y, (Troop) troop);
    }
  }


  /**
   * Method to calculate the loot obtained and win/loss
   */
  public void calculateRewards() {

    DecimalFormat df = new DecimalFormat("#.##"); //Needed to keep double to 2 decimal places

    int defenderBuildings = defender.getPlacedStructures().length;
    int totalBuildingsLeft = defenderArsenal.size();
    //Calculates how many defender structures were destroyed as a percentage
    double winPercentage = Double.parseDouble(df.format((1 - ((double) totalBuildingsLeft / (double) defenderBuildings))));
    double stolenResourcesPercentage;

    //Based on the win percentage, the actual percentage of resources to be taken is found
    if(winPercentage == (double)100) {
      stolenResourcesPercentage = 0.1; //10% of defender loot
    } else if(winPercentage >= 75) {
      stolenResourcesPercentage = 0.08; //8% of defender loot
    } else if(winPercentage >= 50) {
      stolenResourcesPercentage = 0.06; //6% of defender loot
    } else if(winPercentage >= 25) {
      stolenResourcesPercentage = 0.04; //4% of defender loot
    } else {
      stolenResourcesPercentage = 0.02; //2% of defender loot
    }

    //Calculating the amount of wood,iron and gold to be gained based on the defenders resources and win percentage
    int woodGained = (int)(defender.villageHall.getCurrentWoodInStorage() * stolenResourcesPercentage);
    int ironGained = (int)(defender.villageHall.getCurrentIronInStorage() * stolenResourcesPercentage);
    int goldGained = (int)(defender.villageHall.getCurrentGoldInStorage() * stolenResourcesPercentage);

    //Adding the resource values to a hashmap that is passed on to the addRaidVictoryResources function
    HashMap<String, Integer> lootForAttacker = new HashMap<>();
    lootForAttacker.put("Wood", woodGained);
    lootForAttacker.put("Iron", ironGained);
    lootForAttacker.put("Gold", goldGained);
    attacker.villageHall.addRaidVictoryResources(lootForAttacker);
    //Message to console so attacker knows
    rtx4090TI.updateDisplay("Loot gained from attack: Wood: " + woodGained + " Iron: " +ironGained + " Gold: " + goldGained);

  }






  /**
   * Update the situation per tick
   * Assuming 1 attack per tick for now
   *
   *
   * Basic logic rundown:
   * The troop will acquire a target lock to the closest thing if it doesn't already have one.
   * Then if troop is in range, it will inflict damage
   * If not, the troop will move.
   *
   *
   * The structure will acquire a target lock to the closest thing in its range if it doesn't already have one.*
   * It will inflict damage if target lock is not null.
   */
  public void tick() {
    attackerArsenal.stream().filter(Troop.class::isInstance).forEach(entity -> {
        Troop currentTroop = (Troop) entity; // why can you not cast in lambda brooooooo

      // we only bother to tick "troop.isDeployed()" because the dead is auto filtered out at end of tick
      if (currentTroop.isDeployed()) {
      Inhabitant target = currentTroop.getTargetLock();

        // if not already locked, or dead, find new target
        if (target == null || !target.isDestroyed()) {
          target = pathfind(currentTroop, Who.DEFENDER); // get new target
          currentTroop.setTargetLock(target); // save it
        }



        /*
        Take an action
         */


        double distance = Double.POSITIVE_INFINITY; // distance to target

      // if already in range, attack
        if (target != null && (distance = calculateHypotenuse(currentTroop.getXPos(), currentTroop.getYPos(), target.getXPos(), target.getYPos()))  <= currentTroop.getRange()) {
          target.takeDamage(currentTroop.getDamage());
        }
        // not in range, move
        else if (target != null) {
          // it is assumed we already have a target locked to move to
          double movementDistance = ((Troop) entity).getMovementSpeed(); // this is the distance the unit has to travel, initialized to the max distance troop can move in one tick (movement speed)

//           for edge case of moving within attack range. the final move distance is the distance such that just enough to attack the structure.
//           ex range is 7, movement is 9. The distance to get in range is 2. Instead of moving 9, the unit only moves 7
          if (movementDistance > distance && distance - movementDistance < currentTroop.getRange()) {
            movementDistance = Math.ceil(distance- currentTroop.getRange());
          }

          double y = currentTroop.getYPos()- target.getYPos();
          double x = currentTroop.getXPos()- target.getYPos();
          double slope = y/x;
          /**
           Basically this cursed math finds the amount in the X and Y component to travel, while the total distance (along the diagonal) is maximized.
           To make a long explanation short, you find the point of intersection between a circle and a line -- that line being the shortest distance to the target, and the point of intersection is the x/y the troop needs to move to


           Basically because pythagorean X and Y will always be positive
           There will always be 2 cases for each x and y. Use logic to decide which value to use. Ex. if target has a lower y value, take the negative y value
           I hate math
           */
          double yTogo = Math.sqrt(movementDistance * (Math.pow(slope, 2)/ (1 +Math.pow(slope, 2))));
          double xTogo = Math.sqrt(movementDistance - yTogo);


          // There will always be 2 cases for each x and y. Use logic to decide which value to use. Ex. if target has a lower y value, take the negative y value
          if (currentTroop.xPos > target.getXPos()) {xTogo *= -1;}
          if (currentTroop.yPos > target.getYPos()) {yTogo *= -1;}

          // the actual move function
          currentTroop.move(currentTroop.xPos + xTogo, currentTroop.yPos + yTogo);
        }

      }
    });




    // for every thing in placed structures that is defence
    // this is mostly similar to troop but targetlock is dropped on out of range, and structures don't move
    defenderArsenal.stream().filter(DefenceStructures.class::isInstance).forEach(entity -> { // no implementation for defending troops
       DefenceStructures currentBuilding = (DefenceStructures) entity;

        // we don't need check if alive because the dead is filtered out at end of tick
        Inhabitant target = currentBuilding.getTargetLock();

        // if not already locked, or dead, find new target
        if (target == null || !target.isDestroyed()) {
          target = pathfind(currentBuilding, Who.ATTACKER);
          currentBuilding.setTargetLock(target);
        }

        // if target lock acquired, and in range
        double distance = Double.POSITIVE_INFINITY;
        if (target != null && (distance = calculateHypotenuse(currentBuilding.getXPos(), currentBuilding.getYPos(),  target.getXPos(), target.getYPos())) <= currentBuilding.getRange()) {
          // if already in range, attack
          target.takeDamage(currentBuilding.getDamage());
        } else {
          // troop is out of range of structure
          currentBuilding.setTargetLock(null);
        }

    });


    // filter out the dead
    defenderArsenal = defenderArsenal.stream().filter(cursor -> !cursor.isDestroyed()).toList();
    attackerArsenal = attackerArsenal.stream().filter(cursor -> !cursor.isDestroyed()).toList();
  }













  /**
   * ----------------------------------------------------------------------------------------------------------------------------------
   * HELPER METHODS
   * ----------------------------------------------------------------------------------------------------------------------------------
   */





  /**
   * Search for a valid (closest) target
   * @param targeter who is doing the targeting
   * @param targetee who (which team's stuff) are we scanning for
   * @return object of the closest target
   */
  private Inhabitant pathfind(Inhabitant targeter, Who targetee) {
    List<Inhabitant> targetLineup = new ArrayList<>();

    /**
     * set up the list of inhabitants that targeter will try to target.
     */
    if (targetee == Who.DEFENDER) {
//        select all alive structures
      for (Inhabitant thingy: defenderArsenal) {
        if (!thingy.isDestroyed()) {
          targetLineup.add(thingy);
        }
      }

    } else {
      // add all TROOPS that is deployed, and is alive
      for (Inhabitant thingy: attackerArsenal) {
        if (     ((Troop) thingy).isDeployed()     ) {
          targetLineup.add(thingy);
        }
      }

    }

    /**
     * now scan the entire target lineup for closest thing
     */
    Inhabitant centerInhabitant = (Inhabitant) targeter; // casting for use of inhabitant attributes/methods
    Inhabitant target = null; // return value
    int shortestLength = Integer.MAX_VALUE; // current shortest length

    for (int i = 0; i <targetLineup.size(); i++) {
      // check in range
      double lengthBetween = calculateHypotenuse(centerInhabitant.getXPos(), centerInhabitant.getYPos(), targetLineup.get(i).getXPos(), targetLineup.get(i).getYPos());
      if ( lengthBetween < shortestLength) {
        // the closest target is preferred, and will be set to "target"
        target = targetLineup.get(i);
      }
    }

    return target;
  }


  /**
   * Calculate the shortest distance (diagonal line length) from the 2 pairs of co-ordinates
   * @param x1 targeter's x
   * @param y1 targeter's y
   * @param x2 victim's x
   * @param y2 victim's y
   * @return
   */
  private Double calculateHypotenuse(Double x1, Double y1, Double x2, Double y2) {
    // re-zero all the values to the target's co-ordinates
    x2 -= x1;
    y2 -= y1;
    x1 = Double.valueOf(0);
    y1 = Double.valueOf(0);

    return Math.sqrt(Math.pow((x1 + x2), 2)  + Math.pow((y1 + y2),2)); // x1 and y1 is redundant, is always 0
  }




  /**
   * Get the map in array representation
   * @return
   */
  public String[][] getMap () {

    String[][] psudoMap = new String[MAP_SIZE][MAP_SIZE];
    /*
    for each y, iterate all the x
    if troop/structure is in the co-ordinate
    add to map, else add blank char
     */

    // check if there is an inhabitant on every integer coordinate pair, if there is add it to psudo map
    for (int i = 0; i < psudoMap.length; i ++) { // y
      for (int j = 0; j < psudoMap[i].length; j++) { //x
        int finalJ = j; // weird Java stuff that doesn't let you directly use the values
        int finalI = i;

        defenderArsenal.stream()
                .filter(entity -> Math.round(entity.getXPos()) == finalJ && Math.round(entity.getYPos()) == finalI) // filters for anything that has the same x/y coordinate pair
                .forEach(thing -> {
          // structures have a size 3x3. Make a 1 wide perimeter around
          for (int y = finalI-1; y < finalI+2; y ++) { // y
            for (int x = finalJ-1; x <finalJ+2; x++) { //x
              psudoMap[y][x] = thing.getSymbol();
            }
          }
//          psudoMap[finalI][finalJ] = String.valueOf(thing.getID()); // id of structure. this may blow up the map aesthetic above 9
        } );


        // troop appear on top of structures
        attackerArsenal.stream()
                .filter(entity -> Math.round(entity.getXPos()) == finalJ && Math.round(entity.getYPos()) == finalI) // filters for anything that has the same x/y coordinate pair
                .forEach(thing -> psudoMap[finalI][finalJ] = thing.getSymbol());
      }
    }

    return psudoMap;
  }



}