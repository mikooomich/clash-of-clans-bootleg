package Village.Buildings;

import Village.Buildings.ResourceProduction.GoldMine;
import Village.Buildings.ResourceProduction.IronMine;
import Village.Buildings.ResourceProduction.LumberMill;
import Village.Inhabitant;
import Village.MainVillage;


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

  public float upgradeTime;
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

  public void startBuildOrUpgrade(VillageHall village) throws InterruptedException {
    if(village.isAvailableBuilders()) {
      village.useBuilder();
      System.out.println(this.getName() + " build/upgrade started.");
      int maxTicks = Math.round(this.upgradeTime * TICK_SPEED);
      int currentTickCount = 0;
      while (currentTickCount < maxTicks) {
        this.isPaused = true;
        currentTickCount++;
        if (realtime && currentTickCount % 30 == 0) {
          System.out.println(currentTickCount + "/" + maxTicks);
//        System.out.println(drawMap());
        }
//      if (realtime) {
//        Thread.sleep((long) PAUSE_TIME); // realtime.
//      }
      }
      this.isPaused = false;
      System.out.println(this.getName() + " build/upgrade completed.");
      village.buildDone();
    } else {
      System.out.println("No builder is available at the moment.");
    }

  }

  public boolean isBought() {
    return isBought;
  }

  public int getDamage() {
    return 0;
  }
}