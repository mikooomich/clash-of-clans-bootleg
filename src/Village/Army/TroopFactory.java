package Village.Army;

import CustomExceptions.Exceptions;
import Engine.Shop;
import Village.MainVillage;

public class TroopFactory {

    /**
     * Troop factory for creating new instances of troops to be added to an army
     * @param troop
     * @param village
     * @return new Troop (archer, catapult, knight or cannon)
     * @throws Exceptions.InvalidTroopException
     */
    public Troop trainTroop(String troop, MainVillage village) throws Exceptions.InvalidTroopException {
        switch (troop) {
            case "A" -> {
                return new Archer(village);
            }
            case "C" -> {
                return new Catapult(village);
            }
            case "K" -> {
                return new Knight(village);
            }
            case "S" -> {
                return new Soldier(village);
            }
            default -> {
                throw new Exceptions.InvalidTroopException("Invalid Troop -- Check manual for more details.");
            }
        }
    }

}
