package Village;

/**
 * This interface is to be implemented by both the Troop class and the DefenceStructures class as they both share the same methods
 */
public interface AttackOrDefence {

  /**
   * Getter method for the unit's damage
   */
  public int getDamage();

  /**
   * Method to get the range of the unit
   */
  public int getRange();

  /**
   * Getter method to get the attack or defence attack rate
   * THIS WILL NOT BE IMPLEMENTED
   * @return attackRate
   */
  public int getAttackRate();

  /**
   * Check is unit is alive
   * @return
   */
  public boolean isAlive();

  /**
   * Sets the troop/device the current unit is locked onto
   * @param targetLock
   */
  public void setTargetLock(Inhabitant targetLock);

  /**
   * Return the troop/device the current unit is locked onto
   */
  public Inhabitant getTargetLock();
}