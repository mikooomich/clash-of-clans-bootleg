package Village.Buildings.Defences;

import Engine.NvidiaRTX4090TI;
import Village.Buildings.VillageHall;
public class ArcherTower extends DefenceStructures {

   private static int maxLevel = 5;

    private VillageHall villageHall;

    private NvidiaRTX4090TI rtx4090TI;
    public ArcherTower(VillageHall village, NvidiaRTX4090TI rtx4090TI) {
        this.villageHall = village;
        this.name = "Archer_Tower";
        this.currentLevel = 1;
        this.maxHitpoints = 400;
        this.hpMultiplier = 1.25f;
        this.cost.replace("Wood", 0, 350);
        this.cost.replace("Iron", 0, 150);
        this.upgradeTime= 9;
        this.populationWeight = 2;
        this.damage = 10;
        this.maxRange = 10;
        this.symbol = "a";
        this.dmgMultiplier = 1.25f;
        this.maxLevel = 5;
        this.attackRate = 3;
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