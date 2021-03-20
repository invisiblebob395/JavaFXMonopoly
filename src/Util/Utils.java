package Util;

import Main.GameController;
import Squares.Property;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Utils {
    //Stackoverflow: check if a number is .. a number
    public static boolean isNumber(String string) {
        try {
            double test = Double.parseDouble(string);
            return true;
        }catch (NumberFormatException | NullPointerException e) { return false; }
    }
    //Inspired from isNumber, check if a number is int
    public static boolean isInt(String string) {
        try {
            int test = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException | NullPointerException e) { return false; }
    }
    public static Rectangle newRect(double width, double height, Color fill, double x, double y) {
        Rectangle rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setFill(fill);
        rect.setTranslateX(x);
        rect.setTranslateY(y);
        return rect;
    }
    //Dice rolling algoirithm
    public static int rollDice() {
        return (new Random()).nextInt(Globals.diceSides) + 1;
    }
    public static int[] rollDies() {return rollDies(Globals.diceAmount);}
    public static int[] rollDies(int dies) {
        int[] diceRolls = new int[dies];
        for (int i = 0; i < diceRolls.length; i++) diceRolls[i] = rollDice();
        String rollString = Arrays.toString(diceRolls);
        return diceRolls;
    }
    //sums an int array
    public static int getSumOfArray(int[] array) {
        int sum = 0;
        for (int number : array) sum += number;
        return sum;
    }
    //checks if all eleemtns in array is equal
    public static boolean allEqual(int[] array) {
        if (array.length < 2) return false;
        int num = array[0];
        for (int i = 1; i < array.length; i++) if (array[i] != num) return false;
        return true;
    }
    //Stackoverflow: gets if there are duplicates in a list
    public static <T> boolean hasDuplicate(Iterable<T> all) {
        Set<T> set = new HashSet<>();
        // Set#add returns false if the set does not change, which
        // indicates that a duplicate element has been added.
        for (T each: all) if (!set.add(each)) return true;
        return false;
    }
    public static Property getPropertyFromCoordinate(double x, double y) {
        if (y >= Globals.boardSize - Globals.normalHeight) { //QUAD 1
            if (x <= Globals.boardSize && x >= Globals.boardSize-Globals.normalHeight) return Globals.propertyList.get(0);
            else if (x < Globals.normalWidth * 9 + Globals.normalHeight && x >= Globals.normalHeight) return Globals.propertyList.get(9 - (int) (Math.round(x - Globals.normalHeight)/(double) Globals.normalWidth));
            else return Globals.propertyList.get(10);
        }else if(x <= Globals.normalHeight) { //QUAD 2
            if (y >= Globals.normalHeight) return Globals.propertyList.get(19 - (int) (Math.round(y - Globals.normalHeight)/(double) Globals.normalWidth));
            else return Globals.propertyList.get(20);
        }else if (y <= Globals.normalHeight) {
            if (x <= Globals.boardSize-Globals.normalHeight) return Globals.propertyList.get(21 + (int) (Math.round(x - Globals.normalHeight)/(double) Globals.normalWidth));
            else return Globals.propertyList.get(30);
        }else if (x >= Globals.boardSize-Globals.normalHeight) {
            return Globals.propertyList.get(31 + (int) (Math.round(y - Globals.normalHeight)/(double) Globals.normalWidth));
        }
        return null;
    }
    //OUTPUT: {int minX, int maxX, int minY, int maxY}
    public static double[] getPropertyCoordinates(Property property) {
        int index = Globals.propertyList.indexOf(property);
        if (index == -1) throw new IllegalArgumentException(property.getName() + " does not exist");
        if (index == 0) return new double[]{Globals.boardSize - Globals.normalHeight, Globals.boardSize, Globals.boardSize - Globals.normalHeight, Globals.boardSize};
        else if (index < 10) return new double[]{Globals.boardSize - Globals.normalHeight - Globals.normalWidth * index, Globals.boardSize - Globals.normalHeight - Globals.normalWidth * (index - 1), Globals.boardSize - Globals.normalHeight, Globals.boardSize};
        else if (index == 10) return new double[]{0, Globals.normalHeight, Globals.boardSize - Globals.normalHeight, Globals.boardSize};
        else if (index < 20) return new double[]{0, Globals.normalHeight, 800 - Globals.normalHeight - Globals.normalWidth * (index - 11), 800 - Globals.normalHeight - Globals.normalWidth * (index - 10)};
        else if (index == 20) return new double[]{0, Globals.normalHeight, 0, Globals.normalHeight};
        else if (index < 30) return new double[]{Globals.normalHeight + Globals.normalWidth * (index - 21), Globals.normalHeight + Globals.normalWidth * (index - 20), 0, Globals.normalHeight};
        else if (index == 30) return new double[]{Globals.boardSize - Globals.normalHeight, Globals.boardSize, 0, Globals.normalHeight};
        else if (index < 40) return new double[]{Globals.boardSize - Globals.normalHeight, Globals.boardSize, Globals.normalHeight + Globals.normalWidth * (index - 30), Globals.normalWidth * (index - 29)};
        return null;
    }
    //send player to jail
    public static void sendCurrentPlayerToJail() {
        Globals.currentPlayerRound.setPosition(10);
        Globals.currentPlayerRound.setInJail(true);
        Globals.currentPlayerRound.setTurnsInJail(0);
        GameController.controller.log(Globals.currentPlayerRound.getName() + " is jailed");
    }
}
