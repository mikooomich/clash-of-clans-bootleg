package Village.Army;

import Engine.NvidiaRTX4090TI;
import Village.MainVillage;
public class Soldier extends Troop {

    public static int maxLevel = 5;
    private int maxHP = 50;
    private float hpMultiplier = 1.25f;
    private int currentlvl = 1;
    private int upgradeLength = 2;
    private int soldierDamage = 10;
    private float dmgMultiplier = 1.25f;

    private MainVillage myVillage;

    private NvidiaRTX4090TI rtx4090TI;

    public Soldier(MainVillage village, NvidiaRTX4090TI rtx4090TI) {
        this.myVillage = village;
        this.name = "Soldier";
        this.currentLevel = myVillage.soldierLvl;
        this.maxHitpoints = maxHP;
        this.cost.replace("Wood", 0, 1);
        this.cost.replace("Iron", 0, 1);
        this.upgradeTime = upgradeLength;
        this.populationWeight = 1;
        this.damage = soldierDamage;
        this.maxRange = 1;
        this.movementSpeed = 3;
        this.attackRate = 3;
        this.symbol = "S";
        this.rtx4090TI = rtx4090TI;
    }

    /**
     * The Soldier specific upgrade logic
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
        soldierDamage = Math.round(soldierDamage*dmgMultiplier);
        rtx4090TI.updateDisplay("Soldier upgraded. Current level = " + myVillage.soldierLvl);

    }

}