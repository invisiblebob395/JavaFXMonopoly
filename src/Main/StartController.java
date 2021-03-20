package Main;

import Players.Player;
import Util.Globals;
import Util.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.ResourceBundle;

public class StartController implements Initializable {
    private int rowNum = 0;
    private ArrayList<TextField> nameFields = new ArrayList<>();
    private ArrayList<ColorPicker> pickers = new ArrayList<>();
    @FXML private TextField initialMoneyField, goMoneyField, houseRentMultiplierField, diceSidesField, rentMultiplierField, diceAmountField, housePerPlayerField, hotelPerPlayerField;
    @FXML private CheckBox hasAuction, hasTrade, propertyForfeitOnBankrupt, hasAlliances, canBuyMortgage;
    @FXML private GridPane playerGrid;
    @FXML private ComboBox<String> mapChoiceBox;
    public void addNewPlayer() {
        //adds a new player and initializes everything for him/her
        TextField textField = new TextField();
        textField.setPromptText("Player name");
        nameFields.add(textField);
        ColorPicker picker = new ColorPicker();
        Random random = new Random();
        picker.setValue(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        pickers.add(picker);
        playerGrid.add(nameFields.get(nameFields.size() - 1), 0, rowNum);
        playerGrid.add(pickers.get(nameFields.size() - 1), 1, rowNum++);
    }
    public void removePlayer() {
        //removes a player, make sure that amount of players wouldn't drop below 2
        if (rowNum < 3) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You must have at least two players!", ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }
        int rows = rowNum--;
        nameFields.remove(nameFields.size() - 1);
        pickers.remove(pickers.size() - 1);
        playerGrid.getChildren().remove((rows + 1) * 2 - 3, (rows + 1) * 2 - 1);
    }
    public void startGame() throws IOException { //there'll be no IO exception, no need to handle it
        Globals.playerList.clear();
        for (TextField field : nameFields) {
            if (field.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter player names", ButtonType.OK);
                alert.showAndWait();
                return;
            }
        }
        ArrayList<Color> colors = new ArrayList<>();
        //Checks that no colors are the same
        for (ColorPicker picker : pickers) {
            if (colors.indexOf(picker.getValue()) > -1) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please do not choose the same color for different players", ButtonType.OK);
                alert.showAndWait();
                return;
            } else {
                colors.add(picker.getValue());
            }
        }
        //makes sure that entry is of correct type, no NumberFormatException
        if (!(Utils.isInt(initialMoneyField.getText()) && Utils.isInt(goMoneyField.getText()) && Utils.isNumber(houseRentMultiplierField.getText()) && Utils.isInt(diceSidesField.getText()) && Utils.isNumber(rentMultiplierField.getText()) && Utils.isInt(diceAmountField.getText()) && Utils.isInt(housePerPlayerField.getText()) && Utils.isInt(hotelPerPlayerField.getText()))) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please check inputs in settings", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        //assigns everything and makes sure that values make sense, ie. no negative money
        Globals.initialMoney = Math.max(0, Integer.parseInt(initialMoneyField.getText()));
        Globals.goMoney = Math.max(0, Integer.parseInt(goMoneyField.getText()));
        Globals.houseRentMultiplier = Math.max(0, Double.parseDouble(houseRentMultiplierField.getText()));
        Globals.diceSides = Math.max(2, Integer.parseInt(diceSidesField.getText()));
        Globals.rentMultiplier = Math.max(0.01, Double.parseDouble(rentMultiplierField.getText()));
        Globals.diceAmount = Math.max(1, Integer.parseInt(diceAmountField.getText()));
        Globals.hasAlliances = hasAlliances.isSelected();
        Globals.hasAuction = hasAuction.isSelected();
        Globals.hasTrading = hasTrade.isSelected();
        Globals.loserForfeitsProperty = propertyForfeitOnBankrupt.isSelected();
        Globals.canBuyMortgages = canBuyMortgage.isSelected();
        Globals.houseAmount = Math.max(0, Integer.parseInt(housePerPlayerField.getText()));
        Globals.hotelAmount = Math.max(0, Integer.parseInt(hotelPerPlayerField.getText()));
        Globals.mapChoice = mapChoiceBox.getSelectionModel().getSelectedItem();
        for (int i = 0; i < nameFields.size(); i++) {
            Globals.playerList.add(new Player(nameFields.get(i).getText(), Globals.initialMoney, colors.get(i)));
        }
        //shuffles playerlist so that order is shuffled
        Collections.shuffle(Globals.playerList);
        //sets current player to first player
        Globals.currentPlayerRound = Globals.playerList.get(0);
        //shifts screen
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Game.fxml"));
        BorderPane game = fxmlLoader.load();
        Main.primaryStage.setScene(new Scene(game, 1200,800));
    }

    public void showContextMenu(ContextMenuEvent event) {
        //shows a help menu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Help");
        menuItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String helpText = "Initial Money: How much money each player starts with\n" +
                        "GO money: Money each player gets after passing GO\n" +
                        "Rent Multiplier: How much rent is multiplied by\n" +
                        "Auction enabled: If there will be an auction after player refuses to buy\n" +
                        "Trading enabled: Whether trading is allowed\n" +
                        "Dice sides: how many sides per die\n" +
                        "Dice amount: How many die are rolled each turn\n" +
                        "Forfeit Bankruptcy: Whether owner of the property that bankrupted player takes all property of that player\n" +
                        "Alliances: Whether alliances are allowed\n" +
                        "Can buy mortgages: Whether or not mortgages are allowed to be bought\n" +
                        "Houses/hotels per player: How many houses/hotels each player gets to build";
                Alert alert = new Alert(Alert.AlertType.INFORMATION, helpText, ButtonType.OK);
                alert.showAndWait();
            }
        });
        contextMenu.getItems().add(menuItem1);
        contextMenu.setX(event.getScreenX());
        contextMenu.setY(event.getScreenY());
        contextMenu.show(Main.primaryStage);
    }
    public void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to quit?", ButtonType.NO, ButtonType.YES);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) System.exit(0);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < 2; i++) addNewPlayer();
        mapChoiceBox.getItems().addAll("Classic");
        mapChoiceBox.getSelectionModel().select("Classic");
    }
}
