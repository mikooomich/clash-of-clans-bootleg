package Village.Army;

import Village.MainVillage;
import static Engine.UserInterface.rtx4090TI;

import static Engine.UserInterface.rtx4090TI;

public class Catapult extends Troop {

    public static int maxLevel = 4;
    private int maxHP = 400;
    private float hpMultiplier = 1.25f;
    private int currentlvl = 1;
    private int upgradeLength = 2;
    private int catapultDamage = 5;
    private float dmgMultiplier = 1.25f;

    private MainVillage myVillage;
    public Catapult(MainVillage village) {
        this.myVillage = village;
        this.name = "Catapult";
        this.currentLevel = myVillage.catapultLvl;
        this.maxHitpoints = maxHP;
        this.cost.replace("Wood", 0, 16);
        this.cost.replace("Iron", 0, 7);
        this.upgradeTime = upgradeLength;
        this.populationWeight = 1;
        this.symbol = "C";
        this.damage = catapultDamage;
        this.maxRange = 10;
        this.movementSpeed = 1;
        this.attackRate = 1;
    }

    /**
     * The Catapult specific upgrade logic
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
        catapultDamage = Math.round(catapultDamage*dmgMultiplier);
        rtx4090TI.updateDisplay("Catapult upgraded. Current level = " + myVillage.catapultLvl);

    }

}