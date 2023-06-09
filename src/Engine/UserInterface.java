package Engine;

import CustomExceptions.Exceptions;
import Village.*;
import Village.Army.*;
import Village.Buildings.Structures;
import Village.Buildings.VillageHall;

// for system.in reader
import java.awt.image.AffineTransformOp;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static Village.MainVillage.getNextArmy;
import static Village.MainVillage.getNextVillage;


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


    /**
     * Generate a new, random village, adhering to limits of the village
     * @return
     */

    public void GenerateArmyOnly(MainVillage village);

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

  private TroopFactory troopFactory;

  public final NvidiaRTX4090TI rtx4090TI = new NvidiaRTX4090TI(); // display adapter





//  public static void main(String args[]) throws Exception {
//    System.out.println("Hewwo world");
//    UserInterface hewwo = new UserInterface();
//    System.out.println("Gwoodbye world");
//  }





  public UserInterface(WorkerThread workerThread) {
    myVillage = new MainVillage(rtx4090TI);
    troopFactory = new TroopFactory();
    villageSim = new VillageSimulator(myVillage, rtx4090TI);
    rtx4090TI.setThreadLink(workerThread);
    rtx4090TI.updateDisplay("Hewwo! Type a command or or type 'man' for the manual");
  }

  public void doAction(String command) throws Exception {

//    BufferedReader command = new BufferedReader(new InputStreamReader(System.in));

    String[] cmd; // command separated into args
    String whatToDo = "";

//    outer:
//    while (true) {
//      rtx4090TI.updateDisplay("received :::: " + thing);


    if (command == null) {
      return;
    }
      cmd = command.split(" ");
      whatToDo = cmd[0];
      System.out.println(whatToDo);
      String operation; // for secondary command ex. the display part of "editor display"

      try {
      switch (whatToDo) {



        /**
         * Enter editor. Please refer to manual for specifics
         */
        case "editor":
          operation = cmd[1];
          editVillage(myVillage);

          if (operation.equals("place")) {
            editor.place(Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]), Integer.parseInt(cmd[4]), false);
            rtx4090TI.updateDisplay();
          } else if (operation.equals("remove")) {
            editor.remove(Integer.parseInt(cmd[2]));
            rtx4090TI.updateDisplay();
          } else if (operation.equals("display")) { // displays map
            rtx4090TI.updateDisplay(drawMap(myVillage.getMap()));
          } else if (operation.equals("avail")) {
            rtx4090TI.updateDisplay(editor.getAvail());
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
          rtx4090TI.append("Entered shop");
          shop = new Shop(myVillage, rtx4090TI);
          operation = cmd[1];
          
            enterShop(myVillage);

            if (operation.equals("buy")) {
              try {
              shop.buy(Integer.parseInt(cmd[2]), false, false);
              } catch (NumberFormatException e) {
                rtx4090TI.updateDisplay("Invalid id specified, run catalog command to see available");
                shop = null; // make sure close shop
              }
            } else if (operation.equals("catalog") || operation.equals("c")) {
              shop.showCatalog();
            } else if (operation.equals("train")) {
              try {
                shop.train(troopFactory.trainTroop(cmd[2], myVillage, rtx4090TI));
              //  shop.train(Shop.Who.valueOf(cmd[2])); // throw enum error possibly
              } catch (IllegalArgumentException e) {
                rtx4090TI.updateDisplay("Invalid id specified, run catalog command to see available");
                shop = null; // make sure close shop
              } catch (Exceptions.InvalidTroopException e) {
                rtx4090TI.updateDisplay("Invalid Troop -- Check Manual for more details.");
              }
            } else if (operation.equals("upgradeT")) {
              try {
                shop.upgradeTroop(Shop.Who.valueOf(cmd[2]));
                rtx4090TI.updateDisplay();
              } catch (EnumConstantNotPresentException e) {
                rtx4090TI.updateDisplay("Invalid troop specified, run catalog command to see available");
              }
            } else if (operation.equals("upgradeS")) {
              try {
                shop.upgradeStructure(Integer.parseInt(cmd[2]));
              } catch (NoSuchElementException e) {
                rtx4090TI.updateDisplay("Invalid ID or structure not placed, run catalog command to see available");
              }
            } else if (operation.equals("collect")) {

              myVillage.villageHall.collectAll();
            } else if (operation.equals("buyall")) {
              shop.buyAll();
            } else {
              rtx4090TI.updateDisplay("Invalid shop command -- Check Manual for commands");
            }


          shop = null;
          break;


        /**
         * ATTACK AND DEFENCE COMMANDS
         */
        case "simdefence":
          NvidiaRTX4090TI tempRtx = new NvidiaRTX4090TI();
          MainVillage tempMain = new MainVillage(tempRtx);
          tempMain.setTrainedArmy(MainVillage.getNextArmy(myVillage.villageHall.currentLevel));
          initiateSimulator(tempMain, myVillage, true);
          battleSim.faceRoll();
          battleSim.startSim();
          System.out.println("reached here in simdefence in UI");
          battleSim = null;
          tempRtx = null;
          tempMain = null;
          break;

        case "testvillage":
          MainVillage tempOpponent = getNextVillage(myVillage.villageHall.currentLevel);
          List<Troop> playerCurrentArmy = Arrays.asList(myVillage.getTrainedArmy());
          List<Troop> newArmy = getNextArmy(myVillage.villageHall.currentLevel);
          myVillage.setTrainedArmy(newArmy);
          initiateSimulator(myVillage, tempOpponent, true);
          battleSim = null;
          myVillage.setTrainedArmy(playerCurrentArmy);
          break;


        /**
         * Generate new village to attack
         */
        case "next":
          rtx4090TI.append("The next village is:");
          opponent = getNextVillage(myVillage.villageHall.currentLevel);
          // add all details to buffer then print in one shot
          rtx4090TI.append(opponent.getDetails());
          rtx4090TI.append(drawMap(opponent.getMap()));
          rtx4090TI.append("Generation done, run attack command to start");
          rtx4090TI.updateDisplay();
          break;

        /**
         * attack village generated
         */
        case "attack":
          if (opponent == null) {
            opponent = getNextVillage(myVillage.villageHall.getCurrentLevel());
          }
//          operation = cmd[1];
          initiateSimulator(myVillage, opponent, true);
//          if (operation == "faceroll") {
          battleSim.faceRoll();
//          }
          battleSim.startSim();
          break;


        /**
         * displays your village layout and details
         */
        case "map":
          rtx4090TI.printMap(myVillage);
          break;

//        case "rtx4090":
//          // direct print
//          rtx4090TI.updateDisplay("single line");
//
//          // build a message
//          rtx4090TI.append("line 1");
//          rtx4090TI.append("line 2");
//          rtx4090TI.append("line 3");
//          rtx4090TI.append("Printing all lines now");
//          rtx4090TI.updateDisplay(); // prints and clears the framebuffer
//          break;


        case "multibuildertest":
          enterShop(myVillage);
          myVillage.villageHall.hax();

          rtx4090TI.updateDisplay("Upgrading village hall, id = 7");
          shop.upgradeStructure(7);
          Thread.sleep(1000);
          rtx4090TI.updateDisplay("Upgrading archer tower id = 6");
          shop.upgradeStructure(6);
          Thread.sleep(1000);
          rtx4090TI.updateDisplay("Upgrading farm id = 2 (should fail because no available builders)");
          shop.upgradeStructure(2);

          shop = null;

          break;

        case "testchallenge":
          // create a test village
          rtx4090TI.append("Generating YOUR village to test THE BATTLE SIMULATOR adapter...");

          enterShop(myVillage);
          shop.buyAll();
          editVillage(myVillage);
          editor.generateRandomLayout();

          myVillage.villageHall.hax();

          // train a sample army
          shop.train(troopFactory.trainTroop("A",myVillage, rtx4090TI));
          shop.train(troopFactory.trainTroop("K",myVillage, rtx4090TI));
          shop.train(troopFactory.trainTroop("S",myVillage, rtx4090TI));
          shop.train(troopFactory.trainTroop("C",myVillage, rtx4090TI));

          shop.train(troopFactory.trainTroop("A",myVillage, rtx4090TI));
          shop.train(troopFactory.trainTroop("K",myVillage, rtx4090TI));
          shop.train(troopFactory.trainTroop("S",myVillage, rtx4090TI));
          shop.train(troopFactory.trainTroop("C",myVillage, rtx4090TI));
          rtx4090TI.updateDisplay();
// uncomment this to cause attack win
//          shop.train(troopFactory.trainTroop("A",myVillage));
//          shop.train(troopFactory.trainTroop("K",myVillage));
//          shop.train(troopFactory.trainTroop("S",myVillage));
//          shop.train(troopFactory.trainTroop("C",myVillage));

          shop = null;
          editor = null;


          opponent = getNextVillage(1);
          rtx4090TI.append("Starting simulation...");
          initiateSimulator(myVillage, opponent, true);
          battleSim.faceRoll();
          battleSim.startSim();
          break;


        /**
         * print manual from manual file
         */
        case "man":
          try {
            BufferedReader stockpile = new BufferedReader(new FileReader("./MANUAL"));
            String data = stockpile.readLine();
            while (data != null) {
              rtx4090TI.append(data);
              data = stockpile.readLine();
            }
          }
          catch (FileNotFoundException aaaaaaaaaaaaaaaaaaaaaaaaaaaaa) {
            rtx4090TI.updateDisplay("COULD NOT PRINT MANUAL, FILE NOT FOUNT \n\n" + aaaaaaaaaaaaaaaaaaaaaaaaaaaaa);
          }

          break;

        case "cheat":
          myVillage.villageHall.hax();
          rtx4090TI.append("Hmmm... your storages have been filled to maximum capacity...");
          break;
          
        case "reset":
          myVillage = myVillage.resetVillage();
          rtx4090TI.append("Village Deleted");
          break;



        default:
          rtx4090TI.updateDisplay("Invalid command. Try again or type 'man' for the manual");

      } // end switch
//        rtx4090TI.debugUpdateDisplay();
        rtx4090TI.updateDisplay(); // flush
        rtx4090TI.updateDisplay("------------------------\n\nEnter a command: ");
      } // end try
      catch (ArrayIndexOutOfBoundsException error) {
        rtx4090TI.updateDisplay("Please use correct syntax");
        error.printStackTrace();
      }

//      command = new BufferedReader(new InputStreamReader(System.in));

//    }
    synchronized (this) {
      rtx4090TI.updateDisplay();
    }

  }











  /**
   * Method to enter village editor
   * @param myVillage
   */
  private void editVillage(MainVillage myVillage) {
    editor = new Editor(myVillage, rtx4090TI);
  }

  /**
   * Method to enter village editor
   * @param myVillage
   */
  private void enterShop(MainVillage myVillage) {
    shop = new Shop(myVillage, rtx4090TI);
  }

  /**
   * Method to start the battle simulation
   *
   * @param attacker attacker's village
   * @param defender defender's village
   * @param realtime realtime = true will simulate in realtime
   */
  private void initiateSimulator(MainVillage attacker, MainVillage defender, boolean realtime) {
    battleSim = new VeryAccurateBattleSimulator(attacker, defender, realtime, rtx4090TI);

  }

  public NvidiaRTX4090TI getRtx4090TI() {return this.rtx4090TI;}


/**
 * -------------------------------------------------
 * Static method land
 * -------------------------------------------------
 */

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