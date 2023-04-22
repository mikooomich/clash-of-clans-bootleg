package Engine;

import Village.Buildings.Structures;
import Village.MainVillage;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static Village.MainVillage.MAP_SIZE;


/**
 * This class is for customizing a village. A user can change their layout of their village.
 */
public class Editor implements UiElement {
  private MainVillage myVillage;

  private NvidiaRTX4090TI rtx4090TI;

  public Editor(MainVillage myVillage, NvidiaRTX4090TI rtx4090TI) {
    this.myVillage = myVillage;
    this.rtx4090TI = rtx4090TI;
  }


  public void create() {
    // gui stuff
  }

  public void destroy() {
  // gui stuff
  }

  /**
   * Method used to place a structure at a specific spot (x,y)
   * @param x coordinate
   * @param y coordinate
   */
  public void place(int x, int y, int id, boolean silent)  {
    boolean valid = false;
    try {
      Structures thisStructure = myVillage.getAvailStructureByID(id);
      if (!thisStructure.isBought()) {
        rtx4090TI.append("Unable to place unbought structure");
      }

      if (thisStructure != null && isValidPlacement(x, y)) {
        myVillage.addPlacedStructure(x, y, id);

        if (silent) { // for less spam in main window
          rtx4090TI.appendDebug("Success. Placed id=" + thisStructure.id + "at x/y: " + thisStructure.getXPos() + "/" + thisStructure.getYPos());
        }
        else {
          rtx4090TI.append("Success. Placed id=" + thisStructure.id + "at x/y: " + thisStructure.getXPos() + "/" + thisStructure.getYPos()); // hmmm maybe it would be a good idea to check if the add function fails...
        }

        valid = true;
      }
    }
    catch (NoSuchElementException e) {
      rtx4090TI.appendDebug("Please provide a valid index");
    } catch (IndexOutOfBoundsException e) {
      rtx4090TI.appendDebug("Please provide a valid index");
    }
    if(!valid) {
      rtx4090TI.append("Placement failure, INVALID X/Y CO-ORDINATE PAIR");
    }
  }

  /**
   * Method used to remove a structure from a specific spot (x,y)
   */
  public void remove(int id) {
    try {
      myVillage.removePlacedStructure(id);
      rtx4090TI.append("Success");
    }
    catch (IndexOutOfBoundsException e) {
      rtx4090TI.appendDebug("Please provide a valid index");
    }
  }


  /**
   * Method that returns if a structure can be placed at x,y
   * @param x coordinate
   * @param y coordinate
   */
  public boolean isValidPlacement(int x, int y) {
    // prevent out of bounds of map, the x/y is the center of the 3x3 structure
    if (x > (MAP_SIZE-2) || x < 1 || y > (MAP_SIZE-2) || y < 1) {
      return false;
      // or throw
    }

    boolean violationFound = false;
    String[][] valdationMap = myVillage.getMap();

    // if there is a building overlap within the 3x3 grid, centered on x,y, there is a violation
    for (int i= y-1; i < y+2; i ++) { // y
      for (int j = x-1; j <x+2; j++) { //x
        if (valdationMap[y][x] != null) {
          violationFound = true;
          break;
        }
      }
    }


    return !violationFound;
  }


  /**
   * Generate a random layout by assigning random x/y co-ords to every structure in array
   */
  public void generateRandomLayout() {
    for (Structures buildingSelection: myVillage.getAvailableStructures()) {
      // select random location, place
      if(buildingSelection.isBought()) {
        try {
          place(rng(1, MAP_SIZE-2), rng(1, MAP_SIZE-2), buildingSelection.id, true );
        } catch (Exception e) { //CHANGE TO CUSTOM LATER
          rtx4090TI.appendDebug("Structure x will not be placed down");
        }
      }

    }
  }

  /**
   * Structures available to place
   * Return all bought structures in available structure array
   * @return
   */
  public String getAvail() {
    String stats = "";

    for (Structures thingInList : myVillage.getAvailableStructures()) {
      if (thingInList.isBought()) {
        stats += thingInList.symbol + " id=" + thingInList.id + " level=" + thingInList.getCurrentLevel() + "\n";
      }
    }

    return stats;
  }

  /**
   * Un-place all structures
   */
  public void reset() {
    Arrays.stream(myVillage.getPlacedStructures()).forEach(structure -> myVillage.removePlacedStructure(structure.id));
  }

  /**
   * Random number generator, inclusive
   * @param min
   * @param max
   * @return
   */
  public static int rng(int min, int max) {
    return (int) (Math.random() * (max - min + 1) + min);
  }



}