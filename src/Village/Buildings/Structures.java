package Village.Buildings;

import Village.Inhabitant;

import static Engine.UserInterface.rtx4090TI;
import static Engine.VillageSimulator.doBuildOrUpgrade;

/**
 * Super class for all structures as every structure shares these properties. Most are from Inhabitants class as troops also share the same.
 * Unique properties for structures include build time related things.
 */
public abstract class Structures extends Inhabitant {

  public static final int TICK_SPEED = 10; //For upgrade/building

  public static final double PAUSE_TIME = Math.floor((Double.valueOf(1)/ TICK_SPEED) * 1000);

  boolean realtime = true;
  public static final int SIZE = 3; // assume square;

  //Structure build time
  private float buildTime;

  private boolean isPaused;

  //Structure current time until completion
  private float timeUntilCompletion;

  public boolean isBuilt = false;
  public boolean isBought = false;

  public static final int UPGRADE_COST = 100; // it costs this much of every resource to upgrade

  public abstract void upgrade();

  /**
   * Getter method for the build time of the structure
   * @return buildTime
   */
  public float getBuildTime() {
    return buildTime;
  }

  /**
   * Getter method for the current time until completion
   * @return timeUntilCompletion
   */
  public float getTimeUntilCompletion() {
  return timeUntilCompletion;
  }

  public void startBuildOrUpgrade(VillageHall village) {
    if (this.isUpgrading()) {
      rtx4090TI.append("Cannot start an upgrade when an upgrade is ongoing. Remaining seconds: " + this.remainingUpgradeTime);
      return;
    }

    if(village.isAvailableBuilders()) {
      village.useBuilder();
      rtx4090TI.append(this.getName() + " build/upgrade started.");
      this.isPaused = true;
      doBuildOrUpgrade(this);
    } else {
      rtx4090TI.append("No builder is available at the moment.");
    }
  }

  public boolean isBought() {
    return this.isBought;
  }

  public boolean getIsBuilt() {return this.isBuilt;}

  public void finishBuild() {isBuilt = true;}

  public int getDamage() {
    return 0;
  }
  public abstract void finishUpgrade();

}