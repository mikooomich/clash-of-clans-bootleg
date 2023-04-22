package Engine;

import Village.Inhabitant;
import Village.MainVillage;

import java.util.LinkedList;
import java.util.List;

/**
 * Controls the village guard and upgrades / builds
 */
public class VillageSimulator {
  private MainVillage mainVillage;

  public static final int TICK_SPEED = 1;
  public static final int MAX_GUARD_TIME_SECONDS = 20;
  public static final int MAX_ALLOWED_TICKS = TICK_SPEED * MAX_GUARD_TIME_SECONDS; // for guard time
  public static final double PAUSE_TIME = Math.floor((Double.valueOf(1)/ TICK_SPEED) * 1000); // pause time between ticks in ms

  final boolean realtime = true;

  protected static List<Inhabitant> needUpgradOrBuild;

  protected static List<Inhabitant> needGuard;

  private NvidiaRTX4090TI rtx4090TI;

  public VillageSimulator(MainVillage mv, NvidiaRTX4090TI rtx4090TI) {
    this.mainVillage = mv;
    this.rtx4090TI = rtx4090TI;
    needGuard = new LinkedList<>();
    needUpgradOrBuild = new LinkedList<>();

  }
  /**
   * Starts the village guard time
   */
  public void startGuard() throws InterruptedException {
    rtx4090TI.updateDisplay("Starting Guard");
    mainVillage.isOnGuard = true;
    int currentTickCount = 0;
    while (currentTickCount < MAX_ALLOWED_TICKS) {
      currentTickCount++;
      if (realtime && currentTickCount % 5 == 0) {
        rtx4090TI.updateDisplay(currentTickCount + "/" + MAX_ALLOWED_TICKS + " Guard time");
      }
      if (realtime) {
        Thread.sleep((long) PAUSE_TIME); // realtime.
      }
    }
    rtx4090TI.updateDisplay("Guard End");
  }

  /**
   * Ends the village guard time
   */
  public void endGuard() {
  }



  /**
   * Starts a entity build
   * This method adds the entity to the tick array, the async clock is a singleton that will tick the structures.
   * @param entity
   */
  public static String doBuildOrUpgrade(Inhabitant entity) {
    entity.remainingUpgradeTime = entity.upgradeTime;
    if (needUpgradOrBuild.size() <= 0) { // you get some scary error with immutable error without this idk
      needUpgradOrBuild = new LinkedList<>();
    }
    needUpgradOrBuild.add(entity);

    // start ticking
    AsyncClock.idk();
   return "Starting build or upgrade for " + entity.getName() + "id=" + entity.getID();





  }


}