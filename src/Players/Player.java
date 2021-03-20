package Players;

import Cards.Card;
import Main.GameController;
import Squares.Property;
import Squares.Street;
import Util.Globals;
import Util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    private String name;
    private int money;

    public int getMoney() {
        return money;
    }

    private int utilityOwned;
    private ArrayList<Player> alliances = new ArrayList<>();
    private ArrayList<Card> cards = new ArrayList<>();

    public ArrayList<Card> getCards() {
        return cards;
    }

    private int housesLeft;
    private int hotelsLeft;
    private int position = 0;
    private int doublesRolled;
    private boolean isInJail;
    //various getters and setters..

    public boolean isInJail() {
        return isInJail;
    }

    public void setInJail(boolean inJail) {
        isInJail = inJail;
    }

    private int turnsInJail;

    public int getTurnsInJail() {
        return turnsInJail;
    }

    public void setTurnsInJail(int turnsInJail) {
        this.turnsInJail = turnsInJail;
    }

    public int getDoublesRolled() { return doublesRolled; }
    public void setDoublesRolled(int doublesRolled) { this.doublesRolled = doublesRolled; }

    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
        double[] propertySpace = Utils.getPropertyCoordinates(Globals.propertyList.get(position));
        System.out.println(Arrays.toString(propertySpace));
        token.setLayoutX(propertySpace[0] + (propertySpace[1]-propertySpace[0])/2);
        token.setLayoutY(propertySpace[2] + (propertySpace[3] - propertySpace[2])/2);
    }

    public int getHousesLeft() {
        return housesLeft;
    }

    public int getHotelsLeft() {
        return hotelsLeft;
    }

    public void setHousesLeft(int housesLeft) {
        this.housesLeft = housesLeft;
    }

    public void setHotelsLeft(int hotelsLeft) {
        this.hotelsLeft = hotelsLeft;
    }

    public ArrayList<Player> getAlliances() {
        return alliances;
    }
    public boolean hasAlliance(Player player) {
        return alliances.indexOf(player) > -1;
    }

    public int getUtilityOwned() {
        return utilityOwned;
    }

    public void setUtilityOwned(int utilityOwned) {
        this.utilityOwned = utilityOwned;
    }

    private int railroadOwned;

    public void setRailroadOwned(int railroadOwned) {
        this.railroadOwned = railroadOwned;
    }

    public int getRailroadOwned() { return railroadOwned; }

    private ArrayList<Property> properties = new ArrayList<>();
    private final int tokenSize = 20;
    private Circle token;
    public Circle getToken() {return token;}

    public ArrayList<Property> getProperties() {
        return properties;
    }

    private Color color;

    private ArrayList<Color> monopolies = new ArrayList<>();
    public ArrayList<Card> cardInventory = new ArrayList<>();

    public ArrayList<Color> getMonopolies() {
        return monopolies;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    //Constructor, inits new player
    public Player(String name, int money, Color color) {
        this.name = name;
        this.money = money;
        this.color = color;
        this.housesLeft = Globals.houseAmount;
        this.hotelsLeft = Globals.hotelAmount;
        token = new Circle(tokenSize);
        token.setFill(color);
    }

    public boolean hasMonopoly(Color color) {
        //sees if player owns color as monopoly
        if (color == null) return false;
        for (Property property : Globals.propertyList) if (property.getColor() == color && property.getOwner() != this) return false;
        return true;
    }
    public void sendMoney(Player player, int amount) {
        //send money to other player
        player.money += amount;
        money -= amount;
    }
    public int getAggregateWealth() {
        //gets total wealth by adding them all up
        int wealth = this.getMoney();
        for (Property property : this.getProperties()) if (!property.isMortgaged())wealth += property.getBuyCost() + property.getHouseCost() * property.getAmountOfHouses();
        return wealth;
    }
    public void addMoney(int amount) { money += amount; }
    public void deductMoney(int amount) { money -= amount; }
    //moves player X squares
    public void move(int squares) {
        if (this.position + squares >= Globals.propertyList.size()) {
            this.setPosition(this.getPosition() + squares - Globals.propertyList.size());
            this.addMoney(Globals.goMoney);
            GameController.controller.log(this.name + " got " + Globals.goMoney + " from passing Go");
        }
        else this.setPosition(this.position + squares);
        GameController.controller.updatePropertyPriceLabel();
        Globals.propertyList.get(this.getPosition()).onLand();
    }
}
