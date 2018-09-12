package Resources.States;

import java.util.Random;

/**
 *
 * @author Rayman Jamal
 */
public enum PUPChoice {
    HEALTH, LASER, SHIELD, NUKE;
    
    public static PUPChoice choose(){
        double random = Math.random()*99 + 1;
        
        /*
            HEALTH = 1/2 chance  (50/100)
            SHIELD = 7/25 chance (28/100)
            LASER = 9/50 chance  (18/100)
            NUKE = 1/25 chance   (04/100)
        */
        if(random < 51)return HEALTH;
        else if (random < 79) return SHIELD;
        else if (random < 97) return LASER;
        else return NUKE;
    }
}
