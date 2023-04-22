package Village.Army;
import Village.MainVillage;
import com.sun.tools.javac.Main;

import static Engine.UserInterface.rtx4090TI;
public class Archer extends Troop {

    public static int maxLevel = 5;
    public int maxHP = 20;
    public  float hpMultiplier = 1.5f;
    public  int upgradeLength = 2;
    public  int archDamage = 5;
    public  float dmgMultiplier = 1.5f; //Moved from constructor to here, not sure where it should be best
    public MainVillage mainVillage;


    public Archer(MainVillage village) {
        this();
        this.mainVillage = village;
        this.currentLevel = mainVillage.archerLvl;
    }
    public Archer() {
        this.name = "Archer";
        this.maxHitpoints = maxHP;
        this.cost.replace("Wood", 0, 1);
        this.cost.replace("Iron", 0, 1);
        this.upgradeTime = upgradeLength;
        this.populationWeight = 1;
        this.damage = archDamage;
        this.maxRange = 5;
        this.movementSpeed = 3;
        this.attackRate = 3;
        this.symbol = "A";
    }

    /**
     * The Archer specific upgrade logic
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
        archDamage = Math.round(archDamage*dmgMultiplier);
        rtx4090TI.updateDisplay("Archer upgraded. Current level = " + mainVillage.archerLvl);

    }

}