package Main;

import Players.Player;
import Squares.Property;
import Util.Globals;
import Util.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Collections;

public class Auction {
    private int maxBid;
    private Player bidder = Globals.playerList.get(0);
    private ArrayList<Player> bidderList = new ArrayList<>();
    private Property property;
    private Stage auctionStage;

    public Stage getAuctionStage() {
        return auctionStage;
    }

    private VBox mainBox;
    private Label bidderLabel = new Label("Current bidder: " + Globals.playerList.get(0).getName());
    private Label highestBid = new Label("Highest Bid: $0");
    private TextField newBid = new TextField("0");
    private HBox buttonBox = new HBox();
    private Button bid = new Button("Bid");
    private Button quit = new Button("Exit Auction");
    private void endAuction() {
        auctionStage.close();
    }
    //Goes to next bidder, checks for auction end
    private void nextBidder() {
        bidder = bidderList.get((bidderList.indexOf(bidder) >= bidderList.size() - 1) ? 0 : bidderList.indexOf(bidder) + 1);
        bidderLabel.setText("Current bidder: " + bidder.getName());
        if (bidderList.size() == 1 && maxBid != 0) {
            property.setOwner(bidder);
            bidder.deductMoney(maxBid);
            GameController.controller.log(bidder.getName() + " won the auction for " + property.getName() + " for the price of $" + maxBid);
            endAuction();
        } else if (bidderList.size() == 1 && maxBid == 0) endAuction();
    }
    //Init auction stage, constructor
    public Auction(Property property) {
        auctionStage = new Stage();
        for (Player player : Globals.playerList) bidderList.add(player);
        mainBox = new VBox();
        mainBox.setPrefWidth(300);
        mainBox.setPrefHeight(400);
        this.property = property;
        mainBox.getChildren().add(new Label("Auction for " + property.getName() + "\nBase price: " + property.getBuyCost()));
        mainBox.getChildren().add(bidderLabel);
        mainBox.getChildren().add(highestBid);
        mainBox.getChildren().add(newBid);
        //Stackoverflow: prevents non numerical entry
        newBid.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) newBid.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        mainBox.getChildren().add(new HBox(buttonBox));
        buttonBox.setPrefWidth(300);
        buttonBox.getChildren().add(bid);
        bid.setPrefWidth(150);
        //Checks entry for numerical value as well as whether or not the bid is legal
        bid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int amount;
                if (Utils.isInt(newBid.getText())) amount = Integer.parseInt(newBid.getText());
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter an integer value.", ButtonType.CLOSE);
                    alert.showAndWait();
                    return;
                }
                if (amount > maxBid && bidder.getMoney() >= amount) {
                    maxBid = amount;
                    highestBid.setText("Highest Bid: $" + maxBid);
                    newBid.setText(maxBid + 10 + "");
                    nextBidder();
                } else if (bidder.getMoney() < amount) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "You only have $" + bidder.getMoney(), ButtonType.CLOSE);
                    alert.showAndWait();
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Your bid needs to be higher than $" + maxBid, ButtonType.CLOSE);
                    alert.showAndWait();
                }
            }
        });
        buttonBox.getChildren().add(quit);
        //Confirms exit auction
        quit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"You are about to quit the auction. Are you sure?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    bidderList.remove(bidder);
                    newBid.setText(maxBid + 10 + "");
                    nextBidder();
                }

            }
        });
        quit.setPrefWidth(150);
        auctionStage.setTitle("Auction: " + property.getName());
        auctionStage.setScene(new Scene(mainBox, 300, 300));
        auctionStage.initModality(Modality.APPLICATION_MODAL);
        auctionStage.initStyle(StageStyle.UNDECORATED);
    }
}
