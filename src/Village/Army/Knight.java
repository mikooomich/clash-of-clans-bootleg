package Village.Army;

import Village.MainVillage;
import static Engine.UserInterface.rtx4090TI;
public class Knight extends Troop {

    public static int maxLevel = 4;
    public int maxHP = 100;
    public float hpMultiplier = 1.5f;
    public int currentlvl = 1;
    public int upgradeLength = 3;
    public int knightDamage = 15;
    public float dmgMultiplier = 1.25f;
    public MainVillage mainVillage;


    public Knight(MainVillage village) {
        this();
        this.mainVillage = village;
        this.currentLevel = mainVillage.knightLvl;
    }
    public Knight() {
        this.name = "Knight";
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
        rtx4090TI.updateDisplay("Knight upgraded. Current level = " + mainVillage.knightLvl);
    }

}