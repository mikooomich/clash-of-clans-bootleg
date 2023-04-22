package Village.Army;

import CustomExceptions.Exceptions;
import Engine.NvidiaRTX4090TI;
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
    public Troop trainTroop(String troop, MainVillage village, NvidiaRTX4090TI rtx4090TI) throws Exceptions.InvalidTroopException {
        switch (troop) {
            case "A" -> {
                return new Archer(village, rtx4090TI);
            }
            case "C" -> {
                return new Catapult(village, rtx4090TI);
            }
            case "K" -> {
                return new Knight(village, rtx4090TI);
            }
            case "S" -> {
                return new Soldier(village, rtx4090TI);
            }
            default -> {
                throw new Exceptions.InvalidTroopException("Invalid Troop -- Check manual for more details.");
            }
        }
    }

}
