package Village.Buildings.ResourceProduction;

import CustomExceptions.Exceptions;
import Village.Buildings.VillageHall;
import static Engine.UserInterface.rtx4090TI;
public class IronMine extends Production {

    public static int maxLevel = 3;



    public long lastTimeStamp;
    public IronMine(VillageHall village) {
        this();
        this.setVillageHall(village);
    }
    public IronMine() {
        this.name = "IronMine";
        this.currentLevel = 1;
        this.maxHitpoints = 150;
        this.cost.replace("Wood", 0, 400);
        this.upgradeTime= 8;
        this.populationWeight = 1;
        this.productionRate = 50;
        this.productionRateMutliplier = 1.25f;
        this.symbol = "i";
        startTimeStamp();
    }

    public void upgrade() {
        if(currentLevel < maxLevel) {
            startBuildOrUpgrade(getVillageHall());
        } else {
            rtx4090TI.append("Already reached max level.");
        }
    }

    @Override
    public void finishUpgrade() {
        this.maxHitpoints = Math.round(maxHitpoints*hpMultiplier);
        this.currentLevel+= 1;
        this.productionRate = Math.round(productionRate*productionRateMutliplier);
        rtx4090TI.updateDisplay(this.getName() + " upgraded. Current level = " + this.currentLevel);

    }

    public void startTimeStamp() {
        lastTimeStamp = System.currentTimeMillis();
    }

    public void collect() throws Exceptions.NotEnoughResourcesException, Exceptions.FullStorageException {
        long currentTime = System.currentTimeMillis();
        long differenceOfTimeInSec = (currentTime - lastTimeStamp) / 1000;

        getVillageHall().updateWoodInStorage((int)(this.productionRate * differenceOfTimeInSec));
        lastTimeStamp = System.currentTimeMillis();
    }

}