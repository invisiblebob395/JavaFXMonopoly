package Squares;

import Main.GameController;
import Players.Player;
import Util.Globals;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Tax extends Property {
    private int taxAmount;
    private Pane pane;

    @Override
    public boolean isBuyable() { return false; }

    @Override
    public int getType() {
        return 8;
    }

    @Override
    public void buildHouse() {

    }

    @Override
    public boolean canBuildHouse(Player player) { return false; }

    @Override
    public void setOwner(Player player) {

    }

    //Takes away money based on tax
    @Override
    public void onLand() {
        Globals.currentPlayerRound.deductMoney(taxAmount);
        GameController.controller.log(Globals.currentPlayerRound.getName() + " lost $" + this.taxAmount + " to Tax");
        GameController.controller.updatePlayerStats();
    }

    @Override
    public void onBuy(Player player) {

    }

    @Override
    public int getRent() {
        return 0;
    }
    public Tax(int taxAmount) {
        this.taxAmount = taxAmount;
        this.name = "Tax: " + taxAmount;
    }
}
