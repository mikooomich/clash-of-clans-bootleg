package Village.Army;

import Village.AttackOrDefence;
import Village.Inhabitant;

import java.util.HashMap;

/**
 * This is the super class for the army troops. All troops share these properties and all troops attributes depend on these properties values
 */
public abstract class Troop extends Village.Inhabitant implements AttackOrDefence {

  //Troop movement speed
  public int movementSpeed;

  //Troop damage
  public int damage;

  //Damage multiplier for troop upgrade
  public float dmgMultiplier;

  //Troop max range
  public int maxRange;


  public float upgradeTime;



  //Holds the defence that is being targeted
  private Inhabitant targetLock;

  //Troop attack rate
  public int attackRate; // currently unimplemented

  private boolean isDeployed;
  public static final int UPGRADE_COST = 0; // it costs this much of every resource to upgrade

  /**
   * Method for upgrading a troop to new stats
   */
  public abstract void upgrade();

  public int getMaxLevel() {
    return maxLevel;
  }


  public HashMap getCost() {return cost;}

  public float getUpgradeTime() {return upgradeTime;}

  public static final int TICK_SPEED = 10; //For upgrade/building

  public static final double PAUSE_TIME = Math.floor((Double.valueOf(1)/ TICK_SPEED) * 1000);

  boolean realtime = true;



  /**
   * Method to move the troop
   * The caller is responsible for ensuring this is a valid position
   */
  public void move(double NewX, double NewY) {
    // idk maybe best to have move in inhabitant then can reuse for battle sim and placing buildings
    xPos = NewX;
    yPos = NewY;
  }


  /**
   * Returns true if the troops is deployed
   * @return
   */
  public boolean isDeployed() {
    return isDeployed;
  }

  /**
   * Set deployed state to true
   */
  public void deploy() {isDeployed = true;}

  /**
   * Set deployed state to true
   */
  public void unDeploy() {isDeployed = false;}

  /**
   * Get troop movement speed
   * @return movementSpeed
   */
  public int getMovementSpeed() {return movementSpeed;}





  /**
   * --------------
   * combat interface implementation methods
   * --------------
   */
  public int getAttackRate() {return attackRate;} // will not implement


  /**
   * Get targetLock
   * @return
   */
  public Inhabitant getTargetLock() {
    return targetLock;
  }

  /**
   * Set targetLock
   * @param targetLock
   */
  public void setTargetLock(Inhabitant targetLock) {
    this.targetLock = targetLock;
  }

  public int getDamage() {return damage;}
  public int getRange() {return maxRange;}
  public boolean isAlive() {return this.isDestroyed();}

  /**
   * Starts the upgrade timer
   * @param troop
   * @throws InterruptedException
   */
  public void startUpgradeTime(Troop troop) throws InterruptedException {
    int maxTicks = Math.round(troop.upgradeTime * TICK_SPEED);
    int currentTickCount = 0;
    while (currentTickCount < maxTicks) {
      currentTickCount++;
      if ( currentTickCount % 30 == 0) {
        System.out.println(currentTickCount + "/" + maxTicks);
      }
      if (realtime) {
        Thread.sleep((long) PAUSE_TIME); // realtime.
      }
    }
  }

}