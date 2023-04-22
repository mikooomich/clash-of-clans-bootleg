package Engine;

import Village.Buildings.Structures;
import Village.Inhabitant;

import java.util.LinkedList;
import java.util.List;

import static Engine.VillageSimulator.TICK_SPEED;
import static Engine.VillageSimulator.needUpgradOrBuild;

public class AsyncClock extends Thread {

    private static AsyncClock instance = null;

    protected AsyncClock() {
        Thread AAAAAAA = new Thread( new Runnable() {
            @Override
            public void run() {
                try {
//                    rtx4090TI.appendDebug("starting clock");
//                    rtx4090TI.debugUpdateDisplay();
                    buildTick();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        AAAAAAA.start();
    }

    public static void idk() {
        if (instance == null || needUpgradOrBuild.size() <= 0) {
            instance = new AsyncClock();
        }
    }

    /**
     * So basically tick until the building reaches zero,
     * then we call the finish upgrade which actually updates the stats
     * @throws InterruptedException
     */
    private void buildTick() throws InterruptedException {
        long iteration = 0;
        while (needUpgradOrBuild.size() > 0) {

            needUpgradOrBuild.forEach(entity -> {
                entity.remainingUpgradeTime -= TICK_SPEED;
//                rtx4090TI.debugUpdateDisplay(entity.remainingUpgradeTime + " id=" + entity.getID());
            });



            // filter out and complete the upgrades
            needUpgradOrBuild.stream().filter(cursor -> cursor.remainingUpgradeTime <= 0).forEach(thing -> {
                if (thing instanceof Structures struct) {
                    if (struct.getIsBuilt()) {  // upgrade if building only if is built already.
                        struct.finishUpgrade();
                    } else {
//                        struct.finishBuild();
                        struct.finishUpgrade();
                    }

                }
                else {
                    thing.finishUpgrade();
                }
            });

            // siiigh, once again avoiding some scary error with immutable error
            List<Inhabitant> temp = needUpgradOrBuild;
           needUpgradOrBuild = new LinkedList<>();
           temp.stream().filter(cursor -> cursor.remainingUpgradeTime > 0).forEach(inh -> needUpgradOrBuild.add(inh));

//           rtx4090TI.debugUpdateDisplay("Finished iteration " + iteration);
           iteration ++;
//            if (realtime) {
                Thread.sleep(1000); // realtime, 1 second
//            }
            System.out.println("size " + needUpgradOrBuild.size());
        }

        instance = null; // kill clock instance
//        rtx4090TI.debugUpdateDisplay("killing clock");
        System.out.println("ending clock");
    }
}