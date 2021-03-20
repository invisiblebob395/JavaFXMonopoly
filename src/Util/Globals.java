package Util;

import Cards.Card;
import Main.GameController;
import Players.Player;
import Squares.Property;
import Squares.Street;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

public class Globals {
    public static ArrayList<Property> propertyList = new ArrayList<>();
    public static List<Player> playerList = new ArrayList<>();
    public static Player currentPlayerRound;
    public static int currentPlayerIndex;
    public static int diceRollThisRound;
    public static int initialMoney;
    public static int goMoney;
    public static double houseRentMultiplier;
    public static double rentMultiplier;
    public static int diceAmount;
    public static int diceSides;
    public static boolean hasAuction;
    public static boolean hasTrading;
    public static boolean loserForfeitsProperty;
    public static boolean hasAlliances;
    public static boolean canBuyMortgages;
    public static int houseAmount;
    public static int hotelAmount;
    public static String mapChoice;
    public static final double normalWidth = 65.3;
    public static final int normalHeight = 109;
    public static final int boardSize = 800;
    public static ArrayList<Card> chanceList = new ArrayList<>();
    public static ArrayList<Card> commList = new ArrayList<>();
}
