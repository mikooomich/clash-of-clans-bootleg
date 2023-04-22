package Engine;

import ChallengeDecision.ChallengeAttack;
import ChallengeDecision.ChallengeDefense;
import Village.AttackOrDefence;

public class AttackOrDefenceAdapter {

    ChallengeAttack attacker;
    ChallengeDefense defence;

    /**
     * This will convert to attack AND defence typed object.
     * @param entity In our case a troop or structure
     */
    public AttackOrDefenceAdapter(AttackOrDefence entity) {
        attacker = new ChallengeAttack<>(entity.getDamage() + 0.0, entity.getHp()+ 0.0);
        defence = new ChallengeDefense(entity.getDamage()+ 0.0, entity.getHp()+ 0.0);
    }

    /**
     * Get the attack typed version
     * @return
     */
    public ChallengeAttack getAttacker() {
        return attacker;
    }

    /**
     * Get the defence typed version
     * @return
     */
    public ChallengeAttack getDefense() {
        return attacker;
    }
}
