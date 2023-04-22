package Village.Buildings.Defences;

import Engine.NvidiaRTX4090TI;
import Village.Buildings.VillageHall;
public class Cannon extends DefenceStructures {

    private static int maxLevel = 5;

    private VillageHall villageHall;

    private NvidiaRTX4090TI rtx4090TI;
    public Cannon(VillageHall village, NvidiaRTX4090TI rtx4090TI) {
        this.villageHall = village;
        this.name = "Cannon";
        this.currentLevel = 1;
        this.maxHitpoints = 500;
        this.hpMultiplier = 1.25f;
        this.cost.replace("Wood", 0, 200);
        this.cost.replace("Iron", 0, 250);
        this.upgradeTime = 7;
        this.populationWeight = 1;
        this.damage = 35;
        this.maxRange = 5;
        this.symbol = "c";
        this.dmgMultiplier = 1.25f;
        this.attackRate = 2;
        this.rtx4090TI = rtx4090TI;
    }

    public void upgrade() {
        if (currentLevel < maxLevel) {
            rtx4090TI.append(startBuildOrUpgrade(villageHall));
        } else {
            rtx4090TI.append("Already reached max level.");
        }
    }

    @Override
    public void finishUpgrade() {
        this.maxHitpoints = Math.round(maxHitpoints*hpMultiplier);
        this.currentLevel+= 1;
        this.damage = Math.round(damage*dmgMultiplier);
        rtx4090TI.updateDisplay(this.getName() + " upgraded. Current level = " + this.currentLevel);

    }

}