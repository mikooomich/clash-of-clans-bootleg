package Village.Buildings.ResourceProduction;

import Village.Buildings.VillageHall;
import static Engine.UserInterface.rtx4090TI;
/**
 * This is the farm class for a farm structure. Holds properties for food that is being produced and other production properties.
 */
public class Farm extends Production {

  private static int maxLevel = 5;

  private VillageHall villageHall;

  public Farm(VillageHall village) {
    this.villageHall = village;
    this.name = "Farm";
    this.currentLevel = 1;
    this.maxHitpoints = 300;
    this.hpMultiplier = 1.5f;
    this.cost.replace("Wood", 0, 300);
    this.cost.replace("Iron", 0, 50);
    this.upgradeTime = 7;
    this.populationWeight = 2;
    this.foodFromFarm = 30;
    // this.productionRateMutliplier = 1.5f;
    this.symbol ="f";
    this.productionRate = -1;
  }

  //Variable to hold the food being obtained from a farm
  public int foodFromFarm; // constant value for now

  public void upgrade() {
    if(currentLevel < maxLevel) {
      startBuildOrUpgrade(villageHall);
    } else {
      rtx4090TI.append("Already reached max level.");
    }
  }

  @Override
  public void finishUpgrade() {
    if(isBought) {
      this.maxHitpoints = Math.round(maxHitpoints*hpMultiplier);
      this.currentLevel+= 1;
      this.foodFromFarm += 5;
      rtx4090TI.updateDisplay(this.getName() + " upgraded. Current level = " + this.currentLevel);
    } else {
      rtx4090TI.updateDisplay(this.getName() + " finished building. Current level = " + this.currentLevel);
    }

  }

  /**
   * Getter method for food from farm
   * This is the population number the farm can sustain
   * @return foodFromFarm
   */
  public int getFoodFromFarm() {
    return foodFromFarm;
  }

}