package Main;

import Cards.*;
import Players.Player;
import Squares.*;
import Squares.GoToJail;
import Util.Globals;
import Util.PropertyBox;
import Util.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    //@FXML declartions
    @FXML private TextArea logText;
    public static GameController controller;
    @FXML private ComboBox<String> playersBox;
    @FXML private VBox tradeBoxPlayerOne, tradeBoxPlayerTwo;
    @FXML private Label currentPlayerTrade, propertyPriceLabel;
    @FXML private TextField tradePlayerOneMoneyOffered, tradePlayerTwoMoneyOffered;
    @FXML public ImageView gameBoard;
    @FXML private GridPane managementGrid, playerStatsGrid;
    @FXML public Pane boardPane;
    @FXML private Button rollOrEndButton, mortgageButton, buyHouseButton; //mortgage button has three states: mortgage, unmortgage, and sell house //unmortgage: buy house
    private PropertyBox lastSelectedCheckBox;

    private ArrayList<PropertyBox> tradePropertyBoxListPlayerOne = new ArrayList<>();
    private ArrayList<PropertyBox> tradePropertyBoxListPlayerTwo = new ArrayList<>();
    private Player tradingPartner;
    public GameController() {
        controller = this;
    }
    public void clearLog(String message) {
        logText.setText("");
    }
    //logs it into upper right console
    public void log(String message) {
        logText.setText(logText.getText() + "\n" + message);
        logText.selectPositionCaret(logText.getLength());
        logText.deselect();
    }
    //button function, verifies exit
    public void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to exit the game?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) System.exit(0);
    }
    //initializes a new game
    private void initNewGame() {
        Globals.propertyList.clear();
        if (Globals.mapChoice.equals("Classic")) initClassicMap();
        gameBoard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Property property = Utils.getPropertyFromCoordinate(event.getX(), event.getY());
                if (property != null) {
                    String alertText;
                    if (property.getType() == 0) {
                        System.out.println(Arrays.toString(property.getRents()));
                        alertText = "Property name: " + property.getName() + "\n" + "Owner: " + property.getOwnerName() + "\n" + "Price: $" + property.getBuyCost() + " Rent: $" + property.getRent() + "\nBase Rent: $" + (property.getRents())[0] + "\n1 House: $" + (property.getRents())[1] + "\n2 House: $" + (property.getRents())[2] + "\n3 House: $" + (property.getRents())[3]
                                + "\n4 House: $" + (property.getRents())[4] + "\nHotel: $" + (property.getRents())[5];
                    }
                    else if (property.getType() == 1) alertText = "Property name: " + property.getName() + "\nOwner: " + property.getOwnerName() + "\nPrice: $" + property.getBuyCost() + "\nRent: Railroads owned * 4";
                    else if (property.getType() == 2) alertText = "Property name: " + property.getName() + "\nOwner: " + property.getOwnerName() + "\nPrice: $" + property.getBuyCost() + "\nRent: 1 utility owned, 4 * dice roll, 2 utility owned, 10 * dice roll";
                    else return;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, alertText, ButtonType.CLOSE);
                    alert.showAndWait();
                }
            }
        });
        updatePlayerStats();
        initRound();
        initTokens();
    }
    //adds all the tokens in, inits player position
    private void initTokens() {
        for (Player player : Globals.playerList) {
            boardPane.getChildren().add(player.getToken());
            player.setPosition(0);
        }
    }
    //called every time a new round happens, checks for initialize trade + checks whether or not chance and community chest cards need to be restocked
    private void initRound() {
        if (Globals.hasTrading) initTrade();
        restockCommCards();
        restockChanceCards();
    }
    //initializes UI as well as basic tradings tuff
    public void initTrade() {
        tradePlayerOneMoneyOffered.setText("0");
        tradePlayerTwoMoneyOffered.setText("0");
        tradingPartner = null;
        tradeBoxPlayerOne.getChildren().removeAll(tradePropertyBoxListPlayerOne);
        tradePropertyBoxListPlayerOne.clear();
        tradeBoxPlayerTwo.getChildren().removeAll(tradePropertyBoxListPlayerTwo);
        tradePropertyBoxListPlayerTwo.clear();
        playersBox.getItems().clear();
        for (Player player : Globals.playerList) if (player != Globals.currentPlayerRound) playersBox.getItems().add(player.getName());
        currentPlayerTrade.setText(Globals.currentPlayerRound.getName());
        for (int i = 0; i < Globals.currentPlayerRound.getProperties().size(); i++) { //loops through to add all the property
            if (Globals.currentPlayerRound.getProperties().get(i).getAmountOfHouses() < 1) {
                PropertyBox propertyBox = new PropertyBox(Globals.currentPlayerRound.getProperties().get(i), Globals.currentPlayerRound.getProperties().get(i).getName());
                propertyBox.setTextFill((Globals.currentPlayerRound.getProperties().get(i).getColor() != null) ? Globals.currentPlayerRound.getProperties().get(i).getColor() : Color.BLACK);
                tradePropertyBoxListPlayerOne.add(propertyBox);
                tradePropertyBoxListPlayerOne.get(i).setWrapText(true);
                tradePropertyBoxListPlayerOne.get(i).setMaxWidth(135);
                tradeBoxPlayerOne.getChildren().add(tradePropertyBoxListPlayerOne.get(i));
            }
        }
    }
    private void releasePlayerFromJail() {
        Globals.currentPlayerRound.setInJail(false); //... releases player from jail
        Globals.currentPlayerRound.setTurnsInJail(0);
    }
    public void rollOrEnd() throws IOException {
        if (rollOrEndButton.getText().equals("Roll Dice")) {//executes statement for roll dice
            if (Globals.currentPlayerRound.getDoublesRolled() > 0 && Globals.hasAuction && Globals.propertyList.get(Globals.currentPlayerRound.getPosition()).isBuyable() && Globals.propertyList.get(Globals.currentPlayerRound.getPosition()).getOwner() == null && !confirmNotBuyProperty()) return;
            int[] diceRolls = Utils.rollDies();
            int sum = Utils.getSumOfArray(diceRolls);
            Globals.diceRollThisRound = sum;
            String arrString = Arrays.toString(diceRolls);
            GameController.controller.log(Globals.currentPlayerRound.getName() + " rolled " + arrString.substring(1, arrString.length() - 1) + " for a sum of " + sum + ". " + ((Utils.allEqual(diceRolls)) ? "Doubles!":""));
            if (Globals.currentPlayerRound.isInJail()) {
                if (Utils.allEqual(diceRolls)) {
                    GameController.controller.log(Globals.currentPlayerRound.getName() + " rolled doubles! He has been released from jail.");
                    releasePlayerFromJail();
                    Globals.currentPlayerRound.move(sum);
                }
                else {
                    Globals.currentPlayerRound.setTurnsInJail(Globals.currentPlayerRound.getTurnsInJail() + 1);
                    if (Globals.currentPlayerRound.getTurnsInJail() >= 3) {
                        Globals.currentPlayerRound.deductMoney(50);
                        GameController.controller.log(Globals.currentPlayerRound.getName() + " has been forced to pay a fine of $50");
                        releasePlayerFromJail();
                        Globals.currentPlayerRound.move(sum);
                    }
                }
                rollOrEndButton.setText("End Round");
            } else {
                if (Utils.allEqual(diceRolls)) {
                    Globals.currentPlayerRound.setDoublesRolled(Globals.currentPlayerRound.getDoublesRolled() + 1);
                    if (Globals.currentPlayerRound.getDoublesRolled() >= 3) {
                        this.log(Globals.currentPlayerRound.getName() + " is sent to jail for rolling doubles 3 times in a row!");
                        Utils.sendCurrentPlayerToJail();
                        rollOrEndButton.setText("End Round");
                        return;
                    }
                } else rollOrEndButton.setText("End Round");
                Globals.currentPlayerRound.move(sum);
            }
        } else if (rollOrEndButton.getText().equals("End Round")) { //execute statement for end round
            if (Globals.currentPlayerRound.getMoney() < 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to quit? If you quit now, you will lose the game.", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    Player lostTo = Globals.propertyList.get(Globals.currentPlayerRound.getPosition()).getOwner();
                    GameController.controller.log(Globals.currentPlayerRound.getName() + " has resigned.");
                    if (Globals.loserForfeitsProperty && lostTo != null) for (Property property : Globals.currentPlayerRound.getProperties()) property.setOwner(lostTo);
                    else for (Property property : Globals.currentPlayerRound.getProperties()) property.setOwner(null);
                    Globals.playerList.remove(Globals.currentPlayerIndex--);
                    if (Globals.playerList.size() < 2) {
                        this.log("\n\n\n" + Globals.playerList.get(0).getName() + " has won the game.");
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION, Globals.playerList.get(0).getName() + " has won the game.", ButtonType.CLOSE);
                        alert2.showAndWait();
                        BorderPane borderPane = FXMLLoader.load(getClass().getResource("Start.fxml"));
                        Main.primaryStage.setScene(new Scene(borderPane, 600, 400));
                        resetEverything();
                        return;
                    }
                    endPlayerRound();
                }
            } else {
                //Checks if player really don't want toy proeprty by calling confirmNotBuyProperty()
                if (Globals.hasAuction && Globals.propertyList.get(Globals.currentPlayerRound.getPosition()).isBuyable() && Globals.propertyList.get(Globals.currentPlayerRound.getPosition()).getOwner() == null && !confirmNotBuyProperty()) return;
                endPlayerRound();
            }
        }
    }
    private void resetEverything() { //reset everything for end game
        Globals.propertyList.clear();
        Globals.playerList.clear();
        Globals.commList.clear();
        Globals.chanceList.clear();
    }
    public void updatePropertyPriceLabel() { propertyPriceLabel.setText("Property Cost: " + (((Globals.propertyList.get(Globals.currentPlayerRound.getPosition())).isBuyable()) ? "$" + Globals.propertyList.get(Globals.currentPlayerRound.getPosition()).getBuyCost() : "Unbuyable")); } //updates property label to the current property price
    public void buyProperty() { //function called when the "Buy Property" button is pressed
        if (!Globals.propertyList.get(Globals.currentPlayerRound.getPosition()).isBuyable() || (Globals.propertyList.get(Globals.currentPlayerRound.getPosition()).getOwner() != null && !(Globals.propertyList.get(Globals.currentPlayerRound.getPosition()).isMortgaged() && Globals.canBuyMortgages))) return; //Checks for whether the place can be bought
        if (Globals.currentPlayerRound.getMoney() < Globals.propertyList.get(Globals.currentPlayerRound.getPosition()).getBuyCost()) { //Checks for whether the person has enough money
            Alert alert = new Alert(Alert.AlertType.ERROR, "Insufficient funds", ButtonType.YES);
            alert.showAndWait();
        } else {
            Globals.currentPlayerRound.deductMoney(Globals.propertyList.get(Globals.currentPlayerRound.getPosition()).getBuyCost());
            Globals.propertyList.get(Globals.currentPlayerRound.getPosition()).onBuy(Globals.currentPlayerRound);
        }

    }
    private boolean confirmNotBuyProperty() { //Asks player to confirm that they do not wish to buy and init auction
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you don't wish to buy this property, it will go for auction.", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.NO) return false;
        Auction auction = new Auction(Globals.propertyList.get(Globals.currentPlayerRound.getPosition()));
        auction.getAuctionStage().initOwner(Main.primaryStage);
        auction.getAuctionStage().showAndWait();
        return true;
    }
    private void endPlayerRound() {
        //ends player round
        this.log(Globals.currentPlayerRound.getName() + " ended the round\n------------------------------------------");
        Globals.currentPlayerIndex = (Globals.currentPlayerIndex >= Globals.playerList.size() - 1) ? 0 : Globals.currentPlayerIndex + 1; //Goes to next player, checks for index out of bounds
        Globals.currentPlayerRound = Globals.playerList.get(Globals.currentPlayerIndex); //sets currentPlayerRound to this
        updatePropertyManagement();
        this.log(Globals.currentPlayerRound.getName() + " has began the round");
        rollOrEndButton.setText("Roll Dice");
        Globals.currentPlayerRound.setDoublesRolled(0);
        if (Globals.currentPlayerRound.isInJail()) { //execute actions if in jail (alert and choice)
            boolean hasGetOutOfJailCard = false;
            for (Card card : Globals.currentPlayerRound.getCards()) {
                if (card.getType() == 2) {
                    hasGetOutOfJailCard = true;
                    break;
                }
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to bail out of jail for $50? You have to bail yourself out after 3 rounds in jail. " + ((hasGetOutOfJailCard) ? "You can use your get out of jail free card." : ""), ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                if (hasGetOutOfJailCard) {
                    for (Card card : Globals.currentPlayerRound.getCards()) {
                        if (card.getType() == 2) {
                            Globals.currentPlayerRound.getCards().remove(card);
                            break;
                        }
                    }
                }
                else if (Globals.currentPlayerRound.getMoney() >= 50) Globals.currentPlayerRound.deductMoney(50);
                else return;
                releasePlayerFromJail();
            }
        }
        if (Globals.hasTrading) initTrade();
    }
    private void initClassicMap() { //inits map by adding everything to propertyList
        Globals.propertyList.add(new Go());
        Globals.propertyList.add(new Street("Old Kent Road", 60, 50, Color.BROWN, 2, 10, 30, 90, 160, 250));
        Globals.propertyList.add(new CommunityChest());
        Globals.propertyList.add(new Street("Whitechapel Road", 60, 50, Color.BROWN, 4, 20, 60, 180, 320, 450));
        Globals.propertyList.add(new Tax(200));
        Globals.propertyList.add(new Railroad("Kings Cross Station", 200));
        Globals.propertyList.add(new Street("The Angel, Islington", 100, 50, Color.SKYBLUE, 6, 30, 90, 270, 400, 550));
        Globals.propertyList.add(new Chance());
        Globals.propertyList.add(new Street("Euston Road", 100, 50, Color.SKYBLUE, 6, 30, 90, 270, 400, 550));
        Globals.propertyList.add(new Street("Pentonville Road", 100, 50, Color.SKYBLUE, 8, 40, 100, 300, 450, 600));
        Globals.propertyList.add(new JustVisiting());
        Globals.propertyList.add(new Street("Pall Mall", 140, 100, Color.VIOLET, 10, 50, 150, 450, 625, 750));
        Globals.propertyList.add(new Utility(null, "Electric Company", 150));
        Globals.propertyList.add(new Street("Whitehall", 140, 100, Color.VIOLET, 10, 50, 150, 450, 625, 750));
        Globals.propertyList.add(new Street("Northumrl'd Avenue", 160, 100, Color.VIOLET, 12, 60, 180, 500, 700, 900));
        Globals.propertyList.add(new Railroad("Marylebone Station", 200));
        Globals.propertyList.add(new Street("Bow Street", 180, 100, Color.ORANGE, 14, 70, 200, 550, 750, 950));
        Globals.propertyList.add(new CommunityChest());
        Globals.propertyList.add(new Street("Marlborough Street", 180, 100, Color.ORANGE, 14, 70, 200, 550, 750, 950));
        Globals.propertyList.add(new Street("Vine Street", 200, 100, Color.ORANGE, 16, 80, 220, 600, 800, 1000));
        Globals.propertyList.add(new FreeParking());
        Globals.propertyList.add(new Street("Strand", 220, 150, Color.RED, 18, 90, 250, 700, 875, 1050));
        Globals.propertyList.add(new Chance());
        Globals.propertyList.add(new Street("Fleet Street", 220, 150, Color.RED, 18, 90, 250, 700, 875, 1050));
        Globals.propertyList.add(new Street("Trafalgar Square", 240, 150, Color.RED, 20, 100, 300, 750, 925, 1100));
        Globals.propertyList.add(new Railroad("Fenchurch St. Station", 200));
        Globals.propertyList.add(new Street("Leicester Square", 240, 150, Color.YELLOW, 22, 100, 300, 750, 925, 1100));
        Globals.propertyList.add(new Street("Coventry Street", 260, 150, Color.YELLOW, 22, 110, 330, 800, 975, 1150));
        Globals.propertyList.add(new Utility(null, "Water Works", 150));
        Globals.propertyList.add(new Street("Piccadilly", 260, 150, Color.YELLOW, 22, 110, 330, 800, 975, 1150));
        Globals.propertyList.add(new GoToJail());
        Globals.propertyList.add(new Street("Regent Street", 320, 200, Color.GREEN, 26, 130, 390, 900, 1100, 1275));
        Globals.propertyList.add(new Street("Oxford Street", 300, 200, Color.GREEN, 26, 130, 390, 900, 1100, 1275));
        Globals.propertyList.add(new CommunityChest());
        Globals.propertyList.add(new Street("Bond Street", 320, 200, Color.GREEN, 26, 130, 390, 900, 1100, 1275));
        Globals.propertyList.add(new Railroad("Liverpool St. Station", 200));
        Globals.propertyList.add(new Chance());
        Globals.propertyList.add(new Street("Park Lane", 350, 200, Color.BLUE, 35, 175, 500, 1100, 1300, 1500));
        Globals.propertyList.add(new Tax(75));
        Globals.propertyList.add(new Street("Mayfair", 400, 200, Color.BLUE, 50, 200, 600, 1400, 1700, 2000));
    }
    public void restockChanceCards() { //restocks all the chance cards
        Globals.chanceList.add(new AdvanceToXSquare(0));
        Globals.chanceList.add(new AdvanceToXSquare(24));
        Globals.chanceList.add(new AdvanceToXSquare(11));
        Globals.chanceList.add(new AdvanceToType(2, 10));
        Globals.chanceList.add(new AdvanceToType(1, 2));
        Globals.chanceList.add(new BankPaysX(50));
        Globals.chanceList.add(new GetOutOfJail());
        Globals.chanceList.add(new AdvanceXSquares(-3));
        Globals.chanceList.add(new Cards.GoToJail());
        Globals.chanceList.add(new MakeRepairs(25, 100));
        Globals.chanceList.add(new LoseXMoney(15));
        Globals.chanceList.add(new AdvanceToXSquare(25));
        Globals.chanceList.add(new AdvanceToXSquare(39));
        Globals.chanceList.add(new PayMoneyToEveryone(50));
        Globals.chanceList.add(new BankPaysX(150));
        Globals.chanceList.add(new BankPaysX(100));
        Collections.shuffle(Globals.chanceList); //shuffles the cards for random order
    }
    public void restockCommCards() {//restocks all the community chest cards
        Globals.commList.add(new BankPaysX(200));
        Globals.commList.add(new AdvanceToXSquare(0));
        Globals.commList.add(new LoseXMoney(50));
        Globals.commList.add(new GetOutOfJail());
        Globals.commList.add(new Cards.GoToJail());
        Globals.commList.add(new BankPaysX(100));
        Globals.commList.add(new BankPaysX(20));
        Globals.commList.add(new TakeXMoney(10));
        Globals.commList.add(new BankPaysX(100));
        Globals.commList.add(new LoseXMoney(50));
        Globals.commList.add(new LoseXMoney(50));
        Globals.commList.add(new BankPaysX(25));
        Globals.commList.add(new MakeRepairs(40, 115));
        Globals.commList.add(new BankPaysX(10));
        Globals.commList.add(new BankPaysX(100));
        Collections.shuffle(Globals.commList);
    }
    public void tradingPlayerChanged() { //called whenever there is an action to the combo box, inits trade for that player
        String partnerName = playersBox.getSelectionModel().getSelectedItem();
        tradeBoxPlayerTwo.getChildren().removeAll(tradePropertyBoxListPlayerTwo); //removes everything in the vbox first
        tradePropertyBoxListPlayerTwo.clear();
        for (Player player : Globals.playerList) {
            if (player.getName().equals(partnerName)) { //checks to see who is the trading partner, links string to player
                tradingPartner = player;
                break;
            }
        }
        if (tradingPartner == null) return;
        for (int i = 0; i < tradingPartner.getProperties().size(); i++) { //adds all the properties belonging to the partner
            if (Globals.currentPlayerRound.getProperties().get(i).getAmountOfHouses() < 1) {
                PropertyBox propertyBox = new PropertyBox(tradingPartner.getProperties().get(i), tradingPartner.getProperties().get(i).getName());
                propertyBox.setTextFill((tradingPartner.getProperties().get(i).getColor() != null) ? tradingPartner.getProperties().get(i).getColor() : Color.BLACK);
                tradePropertyBoxListPlayerTwo.add(propertyBox);
                tradePropertyBoxListPlayerTwo.get(i).setWrapText(true);
                tradePropertyBoxListPlayerTwo.get(i).setMaxWidth(135);
                tradeBoxPlayerTwo.getChildren().add(tradePropertyBoxListPlayerTwo.get(i));
            }
        }
    }
    public void proposeOffer() {
        if (!Globals.hasTrading) { //checks if trading is allowed
            Alert alert = new Alert(Alert.AlertType.ERROR, "Trading is not enabled", ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }
        if (playersBox.getSelectionModel().getSelectedItem() == null) { //checks if trading partner is selected, prevents null pointer exception
            Alert alert =  new Alert(Alert.AlertType.ERROR, "You need to select your trading partner", ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }
        ArrayList<Property> offerPlayerOne =  new ArrayList<>();
        ArrayList<Property> offerPlayerTwo = new ArrayList<>();
        int moneyOfferedPlayerOne = Math.min(Globals.currentPlayerRound.getMoney(), Integer.parseInt(tradePlayerOneMoneyOffered.getText()));
        int moneyOfferedPlayerTwo = Math.min(tradingPartner.getMoney(), Integer.parseInt(tradePlayerTwoMoneyOffered.getText()));
        for (PropertyBox propertyBox : tradePropertyBoxListPlayerOne) if (propertyBox.isSelected()) offerPlayerOne.add(propertyBox.getProperty());
        for (PropertyBox propertyBox : tradePropertyBoxListPlayerTwo) if (propertyBox.isSelected()) offerPlayerTwo.add(propertyBox.getProperty());
        String tradeString = "Money offered by " + Globals.currentPlayerRound.getName() + ": $" + moneyOfferedPlayerOne + "\nProperties offered:";
        for (int i = 0; i < offerPlayerOne.size(); i++) tradeString += "\n" + (i + 1) + ". " + offerPlayerOne.get(i).getName();
        tradeString += "\n\nMoney offered by " + tradingPartner.getName() + ": $" + moneyOfferedPlayerTwo + "\nProperties offered:";
        for (int i = 0; i < offerPlayerTwo.size(); i++) tradeString += "\n" + (i + 1) + ". " + offerPlayerTwo.get(i).getName();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to propose the following trade?\n" + tradeString, ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) { //logic to confirm trade
            Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION, tradingPartner.getName() + ", do you wish to accept this trade?\n" + tradeString, ButtonType.YES, ButtonType.NO);
            alertConfirm.showAndWait();
            if (alertConfirm.getResult() == ButtonType.YES) {
                Globals.currentPlayerRound.sendMoney(tradingPartner, moneyOfferedPlayerOne);
                tradingPartner.sendMoney(Globals.currentPlayerRound, moneyOfferedPlayerTwo);
                for (Property property : offerPlayerOne) property.setOwner(tradingPartner);
                for (Property property : offerPlayerTwo) property.setOwner(Globals.currentPlayerRound);
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "The trade has been completed", ButtonType.OK);
                alert2.showAndWait();
                initTrade(); //reinitializes trade
                updatePropertyManagement(); //updates property management
                updatePlayerStats(); //updates stats
            }
        }
    }
    public void updatePlayerStats() { //updates stats of players in grid
        playerStatsGrid.getChildren().remove(5, playerStatsGrid.getChildren().size());
        for (int i = 0; i < Globals.playerList.size(); i++) {
            playerStatsGrid.add(new Label(i + 1 + ""), 0, i+1);
            Label nameLabel = new Label(Globals.playerList.get(i).getName());
            nameLabel.setTextFill(Globals.playerList.get(i).getColor());
            playerStatsGrid.add(nameLabel, 1, i + 1);
            playerStatsGrid.add(new Label(Globals.playerList.get(i).getProperties().size() + ""), 2, i + 1);
            playerStatsGrid.add(new Label(Globals.playerList.get(i).getMoney() + ""), 3, i+1);
            playerStatsGrid.add(new Label(Globals.playerList.get(i).getAggregateWealth() + ""), 4, i + 1);
        }
    }
    public void updatePropertyManagement() { //updates property of players in grid
        for (int i = managementGrid.getChildren().size() - 1; i > 3; i--) managementGrid.getChildren().remove(i);
        lastSelectedCheckBox = null;
        for (int i = 0; i < Globals.currentPlayerRound.getProperties().size(); i++) {
            PropertyBox propertyBox = new PropertyBox(Globals.currentPlayerRound.getProperties().get(i), "");
            managementGrid.add(propertyBox, 0, i + 1);
            Label name = new Label(Globals.currentPlayerRound.getProperties().get(i).getName());
            managementGrid.add(name, 1, i + 1);
            name.setTextFill((Globals.currentPlayerRound.getProperties().get(i).getColor() == null) ? Color.BLACK : Globals.currentPlayerRound.getProperties().get(i).getColor());
            managementGrid.add(new Label("" + Globals.currentPlayerRound.getProperties().get(i).getRent()), 2, i + 1);
            if (Globals.currentPlayerRound.getProperties().get(i).getType() == 0) managementGrid.add(new Label("" + Globals.currentPlayerRound.getProperties().get(i).getHouseCost()), 3, i + 1);
            propertyBox.setOnMouseClicked(new EventHandler<MouseEvent>() { //sets logic whenever the checkbox is clicked (propertyBox extends CheckBox btw)
                @Override
                public void handle(MouseEvent event) {
                    if (propertyBox == lastSelectedCheckBox) lastSelectedCheckBox = null;
                    else if (lastSelectedCheckBox == null) lastSelectedCheckBox = propertyBox;
                    else {
                        lastSelectedCheckBox.setSelected(false);
                        lastSelectedCheckBox = propertyBox;
                    }
                    //executes logic to determine text on buttons
                    if (!propertyBox.getProperty().isMortgaged() && propertyBox.getProperty().getOwner().hasMonopoly(propertyBox.getProperty().getColor()) && propertyBox.getProperty().getAmountOfHouses() > 0) {
                        buyHouseButton.setText("Buy house ($" + propertyBox.getProperty().getHouseCost() + ")");
                        mortgageButton.setText("Sell house ($" + propertyBox.getProperty().getHouseCost()/2 + ")");
                    } else if (!propertyBox.getProperty().isMortgaged() && propertyBox.getProperty().getOwner().hasMonopoly(propertyBox.getProperty().getColor()) && propertyBox.getProperty().getAmountOfHouses() < 1) {
                        mortgageButton.setText("Mortgage ($" + propertyBox.getProperty().getBuyCost()/2 + ")");
                        buyHouseButton.setText("Buy house ($" + propertyBox.getProperty().getHouseCost() + ")");
                    } else {
                        mortgageButton.setText("Mortgage ($" + propertyBox.getProperty().getBuyCost()/2 + ")");
                        buyHouseButton.setText("Unmortgage ($" + (propertyBox.getProperty().getBuyCost()/2 + propertyBox.getProperty().getBuyCost() * 0.1) + ")");
                    }
                }
            });
        }
    }
    public void buyHouseButtonPressed() { //executes logic to see which fucntion should be used
        if (buyHouseButton.getText().contains("Buy house") && lastSelectedCheckBox.getProperty().canBuildHouse(Globals.currentPlayerRound) && Globals.currentPlayerRound.getMoney() >= lastSelectedCheckBox.getProperty().getHouseCost()) {
            lastSelectedCheckBox.getProperty().buildHouse();
            Globals.currentPlayerRound.deductMoney(lastSelectedCheckBox.getProperty().getHouseCost());
        } else if (buyHouseButton.getText().contains("Unmortgage") && lastSelectedCheckBox.getProperty().isMortgaged() && Globals.currentPlayerRound.getMoney() >= lastSelectedCheckBox.getProperty().getBuyCost() * 0.6) lastSelectedCheckBox.getProperty().unmortgage();
        updatePropertyManagement(); //updates stats + properties
        updatePlayerStats();
    }
    public void mortgageButtonPressed() { //executes logic to see which function shoudl be used
        if (mortgageButton.getText().contains("Mortgage") && !lastSelectedCheckBox.getProperty().isMortgaged()) lastSelectedCheckBox.getProperty().mortgage();
        else if (mortgageButton.getText().contains("Sell") && lastSelectedCheckBox.getProperty().canSellHouse()) {
            Globals.currentPlayerRound.addMoney(lastSelectedCheckBox.getProperty().getHouseCost()/2);
            lastSelectedCheckBox.getProperty().sellHouse();
        }
        updatePlayerStats();//updates stats + properties
        updatePropertyManagement();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) { //initializes everything
        initNewGame();
        //Stackoverflow: prevents non numerical entry
        tradePlayerOneMoneyOffered.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) tradePlayerOneMoneyOffered.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        //Stackoverflow: prevents non numerical entry
        tradePlayerTwoMoneyOffered.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) tradePlayerTwoMoneyOffered.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
