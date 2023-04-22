package Village.Buildings.ResourceProduction;

import CustomExceptions.Exceptions;
import Village.Buildings.VillageHall;
import static Engine.UserInterface.rtx4090TI;
public class GoldMine extends Production {

    private static int maxLevel = 2;

    private VillageHall villageHall;

    private long lastTimeStamp;
    public GoldMine(VillageHall village) {
        this.villageHall = village;
        this.name = "GoldMine";
        this.currentLevel = 1;
        this.maxHitpoints = 200;
        this.cost.replace("Wood", 0, 300);
        this.cost.replace("Iron", 0, 100);
        this.upgradeTime = 5;
        this.populationWeight = 1;
        this.productionRate = 25;
        this.productionRateMutliplier = 1.25f;
        this.symbol = "g";
        startTimeStamp();
    }

    public void upgrade() {
        if(currentLevel < maxLevel) {
            startBuildOrUpgrade(villageHall);
        } else {
            rtx4090TI.append("Already reached max level.");
        }
    }

    @Override
    public void finishUpgrade() {
        if(isBought) {
            this.maxHitpoints = Math.round(maxHitpoints*hpMultiplier);
            this.currentLevel+= 1;
            this.productionRate = Math.round(productionRate*productionRateMutliplier);
            rtx4090TI.updateDisplay(this.getName() + " upgraded. Current level = " + this.currentLevel);
        } else {
            rtx4090TI.updateDisplay(this.getName() + " finished building. Current level = " + this.currentLevel);
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