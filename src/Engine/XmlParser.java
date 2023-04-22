package Engine;

import Village.Army.*;

import Village.Buildings.Structures;
import Village.Buildings.VillageHall;
import Village.MainVillage;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class XmlParser {


    /**
     * THIS MAIN METHOD IS ONLY USED FOR TESTING, DO NOT RUN THIS
     * @param args
     */
    public static void main(String args[])  {

       System.out.println("Hewwooooooooooooo world \nTHIS MAIN METHOD IS ONLY USED FOR TESTING, DO NOT RUN THIS");

       // making a sample village to test writing
       MainVillage testVillage = new MainVillage();
//       new Shop(testVillage).buyAll();
//       new Editor(testVillage).generateRandomLayout();
        testVillage.addToVillage(new Archer());
        testVillage.addToVillage(new Knight());
        testVillage.addToVillage(new Catapult());
        testVillage.addToVillage(new Soldier());
        testVillage.addToVillage(new Archer());
        testVillage.addToVillage(new Knight());
        testVillage.addToVillage(new Catapult());

        testVillage.villageHall.hax();
        new Editor(testVillage).place(4,4, 7, true);


       writeVillage(testVillage);
        System.out.println("Gwoodbye world");

    }

    /**
     * Write a village to xml
     * @param wonderfulVillage
     */
    public static void writeVillage(MainVillage wonderfulVillage) {
        try {
            FileOutputStream fo = new FileOutputStream("outtttt.xml");
            XMLEncoder encoder = new XMLEncoder(fo);


            // write parts individually
            encoder.writeObject(wonderfulVillage); // village object
            // lists fo structures and troops
            encoder.writeObject(wonderfulVillage.getlISTOFAvailableStructures());
            encoder.writeObject(wonderfulVillage.getlISTOFPlacedStructures());
            encoder.writeObject(wonderfulVillage.getlISTOFTrainedArmy());


            encoder.close();
            fo.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    /**
     * Read and return village from xml
     * @return
     */
    public static MainVillage readFile() {
        try {
            FileInputStream fi = new FileInputStream("outtttt.xml");
            XMLDecoder decoder = new XMLDecoder(fi);

            //MainVillage
            MainVillage mv = (MainVillage) decoder.readObject();
            //Available Structure List
            List<Structures> availStruc = (List<Structures>) decoder.readObject();
            List<Structures> tempAvail = new ArrayList<>();
            for (Structures s : availStruc) {
                if(s instanceof VillageHall) {
                    VillageHall vh = (VillageHall) s;
                    mv.setVillageHall(vh);
                }
                tempAvail.add(s);
            }
            mv.setListOfAvailableStructures(tempAvail);
            //Placed Structure List
            List<Structures> placedStruc = (List<Structures>) decoder.readObject();
            List<Structures> tempPlaced = new ArrayList<>();
            for (Structures s : placedStruc) {
                if(s instanceof VillageHall) {
                    VillageHall vh = (VillageHall) s;
                    mv.setVillageHall(vh);
                }
                tempPlaced.add(s);
            }
            mv.setListOfPlacedStructures(tempPlaced);
            //Trained Troop list
            List<Troop> trainedTroop = (List<Troop>) decoder.readObject();
            List<Troop> tempTroop = new ArrayList<>();
            for (Troop t : trainedTroop) {
                tempTroop.add(t);
            }
            mv.setListOfTroops(tempTroop);

            return mv;


        } catch (IOException e) {
            System.out.println(e);
            return null;
        }

    }


}
