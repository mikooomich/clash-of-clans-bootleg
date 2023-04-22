package Village.Buildings.Defences;

import Village.Buildings.VillageHall;
import static Engine.UserInterface.rtx4090TI;
public class Cannon extends DefenceStructures {

    public static int maxLevel = 5;

    public Cannon(VillageHall village) {
        this();
        this.setVillageHall(village);
    }

    public Cannon() {
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
    }

    public void upgrade() {
        if (currentLevel < maxLevel) {
            startBuildOrUpgrade(getVillageHall());
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