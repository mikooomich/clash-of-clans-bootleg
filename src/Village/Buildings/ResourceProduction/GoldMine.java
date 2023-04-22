package Village.Buildings.ResourceProduction;

import CustomExceptions.Exceptions;
import Engine.NvidiaRTX4090TI;
import Village.Buildings.VillageHall;
public class GoldMine extends Production {

    private static int maxLevel = 2;

    private VillageHall villageHall;

    private long lastTimeStamp;

    private NvidiaRTX4090TI rtx4090TI;
    public GoldMine(VillageHall village, NvidiaRTX4090TI rtx4090TI) {
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
        this.rtx4090TI = rtx4090TI;
        startTimeStamp();
    }

    public void upgrade() {
        if(currentLevel < maxLevel) {
            rtx4090TI.append(startBuildOrUpgrade(villageHall));
        } else {
            rtx4090TI.append("Already reached max level.");
        }
    }

    @Override
    public void finishUpgrade() {
        if (!getIsBuilt()){
            // case for first ime buying
            rtx4090TI.updateDisplay(this.getName() + " bought building. Current level = " + this.currentLevel);
            finishBuild();
            isBuilt = true;
        }

        else if(isBought) {
            this.maxHitpoints = Math.round(maxHitpoints*hpMultiplier);
            this.currentLevel+= 1;
            this.productionRate = Math.round(productionRate*productionRateMutliplier);
            rtx4090TI.updateDisplay(this.getName() + " upgraded. Current level = " + this.currentLevel);
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