package Village.Army;

import Engine.NvidiaRTX4090TI;
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

    private NvidiaRTX4090TI rtx4090TI;

    public Knight(MainVillage village, NvidiaRTX4090TI rtx4090TI) {
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
        this.rtx4090TI = rtx4090TI;
    }


    /**
     * The Knight specific upgrade logic
     */
    public void upgrade() {
        if (currentLevel < maxLevel) {
            try {
                startUpgradeTime(this);
            } catch (InterruptedException e) {
                rtx4090TI.append(e.getMessage());
            }

        } else {
            rtx4090TI.append("Already reached max level.");
        }

    }

    @Override
    public void finishUpgrade() {
        maxHP = Math.round(maxHP*hpMultiplier);
        knightDamage = Math.round(knightDamage*dmgMultiplier);
        rtx4090TI.updateDisplay("Knight upgraded. Current level = " + myVillage.knightLvl);
    }

}