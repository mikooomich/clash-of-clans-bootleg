package Village.Buildings.ResourceProduction;

import CustomExceptions.Exceptions;
import Village.Buildings.VillageHall;

public class IronMine extends Production {

    private static int maxLevel = 3;

    private VillageHall villageHall;

    private long lastTimeStamp;
    public IronMine(VillageHall village) {
        this.villageHall = village;
        this.name = "IronMine";
        this.currentLevel = 1;
        this.maxHitpoints = 150;
        this.cost.replace("Wood", 0, 400);
        this.upgradeTime= 4;
        this.populationWeight = 1;
        this.productionRate = 50;
        this.productionRateMutliplier = 1.25f;
        this.symbol = "i";
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