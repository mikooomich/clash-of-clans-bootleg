package Village.Buildings.Defences;

import Village.Buildings.VillageHall;

public class ArcherTower extends DefenceStructures {

   private static int maxLevel = 5;

    private VillageHall villageHall;
    public ArcherTower(VillageHall village) {
        this.villageHall = village;
        this.name = "Archer_Tower";
        this.currentLevel = 1;
        this.maxHitpoints = 400;
        this.hpMultiplier = 1.25f;
        this.cost.replace("Wood", 0, 350);
        this.cost.replace("Iron", 0, 150);
        this.upgradeTime= 5;
        this.populationWeight = 2;
        this.damage = 10;
        this.maxRange = 10;
        this.symbol = "a";
        this.dmgMultiplier = 1.25f;
        this.maxLevel = 5;
        this.attackRate = 3;
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