package Squares;

import Players.Player;
import Util.Globals;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class Utility extends Property {

    @Override
    public boolean canBuildHouse(Player player) {
        return false;
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public void buildHouse() {

    }

    //incremeents utility owned
    @Override
    public void onBuy(Player player) {
        super.onBuy(player);
        player.setUtilityOwned(player.getUtilityOwned() + 1);
    }

    //Rent based on dice

    @Override
    public int getRent() {
        if (owner.getUtilityOwned() < 2) return (int) (4 * Globals.diceRollThisRound * Globals.rentMultiplier);
        return (int) (10 * Globals.diceRollThisRound * Globals.rentMultiplier);
    }

    @Override
    public boolean isBuyable() { return true; }

    public Utility(Image image, String name, int buyCost) {
        this.buyCost = buyCost;
        this.name = name;
    }

}
