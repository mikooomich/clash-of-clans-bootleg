package Village.Buildings.ResourceProduction;


/**
 * This is the super class for all production structures. Each production structure all share these properties.
 * This contains properties such as production rate, multiplier, isProducing, ect.
 */
public abstract class Production extends Village.Buildings.Structures {

  //Structure production rate (amount of food, wood, iron, gold)
  public int productionRate;

  //Boolean for if the structure is current producing or not
  private Boolean isProducing;

  //Production rate multiplier for when the structure is upgraded (boosts production rate)
  public Float productionRateMutliplier;

  public abstract void upgrade();


}