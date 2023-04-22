package Village.Army;

import Village.MainVillage;
import static Engine.UserInterface.rtx4090TI;
public class Soldier extends Troop {

    public static int maxLevel = 5;
    public int maxHP = 50;
    public float hpMultiplier = 1.25f;
    public int currentlvl = 1;
    public int upgradeLength = 2;
    public int soldierDamage = 10;
    public float dmgMultiplier = 1.25f;
    public MainVillage mainVillage;


    public Soldier(MainVillage village) {
        this();
        this.mainVillage = village;
        this.currentLevel = mainVillage.soldierLvl;
    }
    public Soldier() {
        this.name = "Soldier";
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
        rtx4090TI.updateDisplay("Soldier upgraded. Current level = " + mainVillage.soldierLvl);

    }

}