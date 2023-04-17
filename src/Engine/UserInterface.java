package Engine;

import Village.*;
import Village.Army.*;

// for system.in reader
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Objects;


/**
 * Main user interface class, which handles all user interfaces and manages them.
 */
public class UserInterface {

  public interface Generator {
    /**
     * Generate a new, random village, adhering to limits of the village
     * @return
     */
    public MainVillage GenerateVillage(int targetVillageHallLvl);
  }




  //Unique player ID, for multiplayer
  private Integer UID;

  //Village array containing all villages available for multiplayer
  private MainVillage allVillages[];

  //Player village
  protected MainVillage myVillage;
  protected MainVillage opponent;


  /**
   * modules
   */

  //User interface editor
  private Editor editor;
  //Battle simulator variable
  private VeryAccurateBattleSimulator battleSim;
  //Village simulator variable
  private VillageSimulator villageSim;
  private Shop shop;



//  //Time variable for current time
//  public Float time;


  public static void main(String args[]) throws Exception {
    System.out.println("Hewwo world");
    UserInterface hewwo = new UserInterface();
    System.out.println("Gwoodbye world");
  }





  public UserInterface() throws Exception {
    myVillage = new MainVillage();
    System.out.println("Hewwo! Type a command or or type 'man' for the manual");


    BufferedReader command = new BufferedReader(new InputStreamReader(System.in));

    String cmd[]; // command seperated into args
    String whatToDo = "";

    outer:
    while (true) {
//      System.out.println("received :::: " + thing);
      cmd = command.readLine().split(" ");
      whatToDo = cmd[0];
      String operation; // for secondary command ex. the display part of "editor display"

      try {
      switch (whatToDo) {

        case "exit":

          break outer;


        /**
         * Enter editor. Please refer to manual for specifics
         */
        case "editor":
          operation = cmd[1];
          editVillage(myVillage);

          if (operation.equals("place")) {
            System.out.println(editor.place(Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]), Integer.parseInt(cmd[4])));
          } else if (operation.equals("remove")) {
            System.out.println(editor.remove(Integer.parseInt(cmd[2])));
          } else if (operation.equals("display")) { // displays map
            System.out.println(drawMap(myVillage.getMap()));
          } else if (operation.equals("avail")) {
            System.out.println(editor.getAvail());
          } else if (operation.equals("rng")) { // generate random layout
            editor.generateRandomLayout();
            drawMap(myVillage.getMap());
          } else if (operation.equals("reset")) { // un-place all structures
            editor.reset();
          }

          editor = null; // kill session
          break;


        /**
         * Enter Shop. Please refer to manual for specifics
         */
        case "shop":

          shop = new Shop(myVillage);
          operation = cmd[1];
          
            enterShop(myVillage);

            if (operation.equals("buy")) {
              try {
              shop.buy(Integer.parseInt(cmd[2])); // ERRORS IF CHARACTER TYPED
              } catch (NumberFormatException e) {
                System.out.println("Invalid id specified, run catalog command to see available");
                shop = null; // make sure close shop
              }
            } else if (operation.equals("catalog") || operation.equals("c")) {
              shop.showCatalog();
            } else if (operation.equals("train")) {
              try {
                shop.train(Shop.Who.valueOf(cmd[2])); // throw enum error possibly
              } catch (IllegalArgumentException e) {
                System.out.println("Invalid id specified, run catalog command to see available");
                shop = null; // make sure close shop
              }
            } else if (operation.equals("upgradeT")) {
              try {
                shop.upgradeTroop(Shop.Who.valueOf(cmd[2]));
              } catch (EnumConstantNotPresentException e) {
                System.out.println("Invalid troop specified, run catalog command to see available");
              }
            } else if (operation.equals("upgradeS")) {
              try {
                shop.upgradeStructure(Integer.parseInt(cmd[2]));
              } catch (NoSuchElementException e) {
                System.out.println("Invalid ID or structure not placed, run catalog command to see available");
              }
            } else if (operation.equals("collect")) {

              myVillage.villageHall.collectAll();
            }
          if (operation.equals("buyall")) {
              shop.buyAll();
            }


          shop = null;
          break;


        /**
         * ATTACK AND DEFENCE COMMANDS
         */
        case "simdefence":
          // it is possible to start a defence when you or opponent have no placedbuildings.
          opponent = getNextVillage(myVillage.villageHall.currentLevel);
          initiateSimulator(opponent, myVillage, true);
          battleSim.faceRoll();
          battleSim.startSim();
          System.out.println("Simulation done");
          battleSim = null;

          // start guard after being attacked
          villageSim = new VillageSimulator(myVillage);
          villageSim.startGuard();
          break;

        /**
         * Generate new village to attack
         */
        case "next":
          opponent = getNextVillage(myVillage.villageHall.currentLevel);
          System.out.println(opponent.getDetails());
          System.out.println(drawMap(opponent.getMap()));
          System.out.println("Generation done, run attack command to start");
          break;

        /**
         * attack village generated
         */
        case "attack":
  //          operation = cmd[1];
            battleSim = new VeryAccurateBattleSimulator(myVillage, opponent, true);
  //          if (operation == "faceroll") {
            battleSim.faceRoll();
  //          }
            battleSim.startSim();


            // start guard after being attacked
            new VillageSimulator(opponent).startGuard();



          break;


        /**
         * displays your village layout and details
         */
        case "map":
          System.out.println(myVillage.getDetails());
          System.out.println(drawMap(myVillage.getMap()));
          break;


        /**
         * print manual from manual file
         */
        case "man":
          String text = "";
          BufferedReader stockpile = new BufferedReader(new FileReader("./src/MANUAL"));
          String data = stockpile.readLine();
          while (data != null) {
            text += data + "\n";
            data = stockpile.readLine();
          }
          System.out.println(text);
          break;

        case "cheat":
          myVillage.villageHall.hax();
          break;

        case "delete":
          deleteVillage();
          break;


        default:
          System.out.println("Invalid command. Try again or type 'man' for the manual");

      } // end switch
        System.out.println("------------------------\n\nEnter a command: ");
      } // end try
      catch (ArrayIndexOutOfBoundsException error) {
        System.out.println("Please use correct syntax");
      }

      command = new BufferedReader(new InputStreamReader(System.in));

    }
  }











//  /**
//   *Method for a user to log on and access their village
//   * For multiplayer
//   * @param user
//   * @param password
//   * @return MainVillage (their village)
//   */
//  private MainVillage logon(String user, String password) {
//  return null;
//  }

  /**
   * Method to enter village editor
   * @param myVillage
   */
  private void editVillage(MainVillage myVillage) {
    editor = new Editor(myVillage);
  }

  /**
   * Method to enter village editor
   * @param myVillage
   */
  private void enterShop(MainVillage myVillage) {
    shop = new Shop(myVillage);
  }

  /**
   * Method to get the next village (randomly), used in battle sim
   * @return MainVillage (random village)
   */
  private MainVillage getNextVillage(int targetVillageHallLvl) {

    Generator yes = new Generator() {

//      @Override
      public MainVillage GenerateVillage(int targetVillageHallLvl) {
        MainVillage hi = new MainVillage();

        // set level for max allowed stuff
//        int villageHallLvl = rng(1, VillageHall.maxLevel);
        int villageHallLvl = targetVillageHallLvl;
        System.out.println(hi.villageHall.getCurrentLevel() +"<-- current   target -->" + villageHallLvl);

        // upgrade village hall until level
        while (hi.villageHall.getCurrentLevel() < villageHallLvl) {
//          System.out.println("---------------------------------------------");
          hi.villageHall.hax();
          hi.villageHall.upgrade();
        }

        // create army, structures
        new Shop(hi).buyAll();
        new Editor(hi).generateRandomLayout();


        while (hi.getTotalPopulation() < hi.populationLimit) {
          hi.addToVillage(new Archer(myVillage));
          hi.addToVillage(new Soldier(myVillage));
          hi.addToVillage(new Catapult(myVillage));
          hi.addToVillage(new Knight(myVillage));
        }
        return hi;
      }
    };


    return yes.GenerateVillage(targetVillageHallLvl);
  }


  /**
   * Method for deleting a village
   */
  private void deleteVillage() {
    myVillage = new MainVillage();
  }

  /**
   * Method to start the battle simulation
   *
   * @param attacker attacker's village
   * @param defender defender's village
   * @param realtime realtime = true will simulate in realtime
   */
  private void initiateSimulator(MainVillage attacker, MainVillage defender, boolean realtime) {
    battleSim = new VeryAccurateBattleSimulator(attacker, defender, realtime);

  }




/**
 * -------------------------------------------------
 * Static method land
 * -------------------------------------------------
 */


  /**
   * Random number generator, inclusive
   * @param min
   * @param max
   * @return
   */
  public static int rng(int min, int max) {
    return (int) (Math.random() * (max - min + 1) + min);
  }

  /**
   * Return string form of string array
   * @return
   */
  public static String drawMap(String[][] psudoMap) {
    String map = "";

    // translate array into string
    for (int i = 0; i < psudoMap.length; i ++) { // y
      for (int j = 0; j < psudoMap[i].length; j++) { //x
        if (psudoMap[i][j] != null) {
          map+= psudoMap[i][j];
        } else {
          map+=" ";
        }
      }

      map+="\n";
    }

    return map;
  }

}