package Village.Buildings.Defences;

import Village.Buildings.VillageHall;

public class Cannon extends DefenceStructures {

    private static int maxLevel = 5;

    private VillageHall villageHall;
    public Cannon(VillageHall village) {
        this.villageHall = village;
        this.name = "Cannon";
        this.currentLevel = 1;
        this.maxHitpoints = 500;
        this.hpMultiplier = 1.25f;
        this.cost.replace("Wood", 0, 200);
        this.cost.replace("Iron", 0, 250);
        this.upgradeTime = 5;
        this.populationWeight = 1;
        this.damage = 35;
        this.maxRange = 5;
        this.symbol = "c";
        this.dmgMultiplier = 1.25f;
        this.attackRate = 2;
    }

    public void upgrade() {
        if (currentLevel < maxLevel) {
            try {
                startBuildOrUpgrade(villageHall);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            this.maxHitpoints = Math.round(maxHitpoints*hpMultiplier);
            this.currentLevel+= 1;
            this.damage = Math.round(damage*dmgMultiplier);
            System.out.println(this.getName() + " upgraded. Current level = " + this.currentLevel);
        } else {
            System.out.println("Already reached max level.");
        }
    }

}