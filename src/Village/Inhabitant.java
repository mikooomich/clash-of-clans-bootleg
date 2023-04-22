package Village;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Abstract class for Structures and Troops classes.
 * Contains basic properties shared by both classes needed for every entity in the game (structures and troops)
 */
public abstract class Inhabitant {

  /**
   * initiate costs
   */
  public Inhabitant() {
    cost.put("Wood", 0);
    cost.put("Iron", 0);
    cost.put("Gold", 0);
  }

  //Troop or Structure current X position
  public double xPos;

  //Troop or Structure current Y position
  public double yPos;

  //Troop or Structure current hitpoints
  public int currentHitpoints;

  //Troop or Structure current max hitpoints (based on level and hitpoints multiplier)
  public int maxHitpoints;

  //Troop or structure hitpoints multiplier
  public float hpMultiplier;

  //Troop or Structure current level
  public int currentLevel;

  //Troop or Structure max level
  public int maxLevel;

  //Troop or Structure id
  public int id;

  //Hashmap of the Troop or Structure cost, contains three keys Wood, Iron, Gold and the amount (int) corresponding to each
  public HashMap<String, Integer> cost = new HashMap<>();


  //Troop or Structure population weight (how much population they take up)
  public int populationWeight;

  //Troop or Structure upgrade time length
  public double upgradeTime;
  public double remainingUpgradeTime;

  public String symbol; // what will display on map as

  public String name; //Entity name (eg. Archer, Cannon, VillageHall)







  /**
   * Upgrade method to upgrade Troop or Structure by changing needed values
   */
  public abstract void upgrade();

  public abstract void finishUpgrade();



public boolean isUpgrading() {
  if (remainingUpgradeTime > 0) {
    return true;
  }
  return false;

}


  /**
   * Getter method for ID
   *
   * @return id
   */
  public int getID() {return id;}

  /**
   * Getter method for X position
   *
   * @return xPos
   */
  public double getXPos() {return xPos;}

  /**
   * Getter method for Y position
   *
   * @return yPos
   */
  public double getYPos() {return yPos;}

  /**
   * Getter method for current hitpoints
   * @return currentHitpoints
   */
  public int getCurrentHitpoints(){return currentHitpoints;}

  /**
   * Getter method for current level
   * @return currentLevel
   */
  public int getCurrentLevel(){return currentLevel;}


  /**
   * Getter method for max level
   * @return maxLevel
   */
  public int getMaxLevel(){ return maxLevel;}

  /**
   * Getter method for cost
   * @return cost
   */
  public HashMap getCost(){return cost;};

  /**
   * Getter method for population weight
   * @return populationWeight
   */
  public int getPopulationWeight(){return populationWeight;};

  /**
   * Getter method for upgrade time
   * @return upgradeTime
   */
  public double getUpgradeTime(){return upgradeTime;};

  /**
   * Getter method to get entity name
   * @return name
   */
  public String getName(){return name;}

  /**
   * Updates the Troop or Structure hitpoints when taken damage
   */
  public void takeDamage(int damage) {
    currentHitpoints -= damage;
  }

  public boolean isDestroyed() {
    if (currentHitpoints > 0) {return false;}
    return true;
  }


  /** for map display
   *
   * @return
   */
  public String getSymbol() {
    return this.symbol;
  }


}