package Village.Buildings.ResourceProduction;

import CustomExceptions.Exceptions;
import Village.Buildings.VillageHall;

import java.util.Vector;

public class LumberMill extends Production {

    private static int maxLevel = 4;

    private VillageHall villageHall;

    private long lastTimeStamp;
    public LumberMill(VillageHall village) {
        this.villageHall = village;
        this.name = "LumberMill";
        this.currentLevel = 1;
        this.maxHitpoints = 100;
        this.cost.replace("Wood", 0, 250);
        this.upgradeTime = 3;
        this.populationWeight = 1;
        this.productionRate = 100;
        this.productionRateMutliplier = 1.25f;
        this.symbol = "l";
        startTimeStamp();
    }

    public void upgrade() {
        if(currentLevel < maxLevel) {
            try {
                startBuildOrUpgrade(villageHall);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            this.maxHitpoints = Math.round(maxHitpoints*hpMultiplier);
            this.currentLevel+= 1;
            this.productionRate = Math.round(productionRate*productionRateMutliplier);
            System.out.println(this.getName() + " upgraded. Current level = " + this.currentLevel);
        } else {
            System.out.println("Already reached max level.");
        }
    }

    public void startTimeStamp() {
        lastTimeStamp = System.currentTimeMillis();
    }

    public void collect() throws Exceptions.NotEnoughResourcesException, Exceptions.FullStorageException {
        long currentTime = System.currentTimeMillis();
        long differenceOfTimeInSec = (currentTime - lastTimeStamp) / 1000;

        villageHall.updateWoodInStorage((int)(this.productionRate * differenceOfTimeInSec));
        lastTimeStamp = System.currentTimeMillis();
    }

}