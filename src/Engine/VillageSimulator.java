package Engine;

import Village.Inhabitant;
import Village.Buildings.Structures;
import Village.MainVillage;

/**
 * Controls the village guard and upgrades / builds
 */
public class VillageSimulator {
  private MainVillage mainVillage;

  public static final int TICK_SPEED = 10;
  public static final int MAX_GUARD_TIME_SECONDS = 20;
  public static final int MAX_ALLOWED_TICKS = TICK_SPEED * MAX_GUARD_TIME_SECONDS; // for guard time
  public static final double PAUSE_TIME = Math.floor((Double.valueOf(1)/ TICK_SPEED) * 1000); // pause time between ticks in ms

  boolean realtime = true;

  public VillageSimulator(MainVillage mv) {
    this.mainVillage = mv;
  }
  /**
   * Starts the village guard time
   */
  public void startGuard() throws InterruptedException {
    mainVillage.isOnGuard = true;
    int currentTickCount = 0;
    while (currentTickCount < MAX_ALLOWED_TICKS) {
      currentTickCount++;
      if (realtime && currentTickCount % 30 == 0) {
        System.out.println(currentTickCount + "/" + MAX_ALLOWED_TICKS + " Guard time");
      }
      if (realtime) {
        Thread.sleep((long) PAUSE_TIME); // realtime.
      }
    }
  }

  /**
   * Ends the village guard time
   */
  public void endGuard() {
  }

  /**
   * Upgrades a unit (either structure or troop)
   * @param entity
   */
  public void doUpgrade(Inhabitant entity) {
    entity.upgrade();
  }

  /**
   * Starts a structure build
   * @param structure
   */
  public void doBuild(Structures structure) throws InterruptedException {
    structure.startBuildOrUpgrade(mainVillage.villageHall);
  }

}