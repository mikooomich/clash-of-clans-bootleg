package Village.Buildings.Defences;
import Village.AttackOrDefence;
import Village.Inhabitant;

/**
 * Super class for all defence structures. Both defences share all the same properties but just have different values.
 * This class contains properties that the defences uniquely share such as range and damage.
 */
public abstract class DefenceStructures extends Village.Buildings.Structures implements AttackOrDefence {

  //Defence damage
  public int damage;

  //Defence damage multiplier, used in the upgrade method to calculate new damage after upgrade
  public float dmgMultiplier;

  //Current locked on target
  public Inhabitant targetLock;

  //Defence attack rate
  public int attackRate;

  //Defence max range
  public int maxRange;





  public abstract void upgrade();

  public int getDamage() {
    return damage;
  }

  public int getRange() {

    return maxRange;
  }

  @Override
  public boolean isAlive() {
    return this.isDestroyed();
  }

  /**
   * Get targetLock
   * @return
   */
  public Inhabitant getTargetLock() {
    return targetLock;
  }

  /**
   * Set targetLock
   * @param targetLock
   */
  public void setTargetLock(Inhabitant targetLock) {
    this.targetLock = targetLock;
  }

  public int getAttackRate(){return attackRate;}
}