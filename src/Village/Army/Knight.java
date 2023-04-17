package Village.Army;

import Village.MainVillage;

public class Knight extends Troop {

    public static int maxLevel = 4;
    private int maxHP = 100;
    private float hpMultiplier = 1.5f;
    private int currentlvl = 1;
    private int upgradeLength = 3;
    private int knightDamage = 15;
    private float dmgMultiplier = 1.25f;
    
    private MainVillage myVillage;

    public Knight(MainVillage village) {
        this.myVillage = village;
        this.name = "Knight";
        this.currentLevel = myVillage.knightLvl;
        this.maxHitpoints = maxHP;
        this.cost.replace("Wood", 0, 3);
        this.cost.replace("Iron", 0, 2);
        this.upgradeTime = upgradeLength;
        this.populationWeight = 1;
        this.symbol = "K";
        this.damage = knightDamage;
        this.maxRange = 1;
        this.movementSpeed = 2;
        this.attackRate = 2;
    }


    /**
     * The Knight specific upgrade logic
     */
    public void upgrade() {
        if (currentLevel < maxLevel) {
            try {
                startUpgradeTime(this);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            maxHP = Math.round(maxHP*hpMultiplier);
            knightDamage = Math.round(knightDamage*dmgMultiplier);
            System.out.println("Knight upgraded. Current level = " + myVillage.knightLvl);
        } else {
            System.out.println("Already reached max level.");
        }

    }

}