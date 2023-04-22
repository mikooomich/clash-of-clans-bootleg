package Village.Army;
import Engine.NvidiaRTX4090TI;
import Village.MainVillage;
public class Archer extends Troop {

    public static int maxLevel = 5;
    private int maxHP = 20;
    private float hpMultiplier = 1.5f;
    private int upgradeLength = 2;
    private int archDamage = 5;
    private float dmgMultiplier = 1.5f; //Moved from constructor to here, not sure where it should be best
    private MainVillage myVillage;

    private NvidiaRTX4090TI rtx4090TI;

    public Archer(MainVillage village, NvidiaRTX4090TI rtx4090TI) {
        this.myVillage = village;
        this.name = "Archer";
        this.maxHitpoints = maxHP;
        this.currentLevel = myVillage.archerLvl;
        this.cost.replace("Wood", 0, 1);
        this.cost.replace("Iron", 0, 1);
        this.upgradeTime = upgradeLength;
        this.populationWeight = 1;
        this.damage = archDamage;
        this.maxRange = 5;
        this.movementSpeed = 3;
        this.attackRate = 3;
        this.symbol = "A";
        this.rtx4090TI = rtx4090TI;
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
        rtx4090TI.updateDisplay("Archer upgraded. Current level = " + myVillage.archerLvl);

    }

}