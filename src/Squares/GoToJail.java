package Squares;

import Players.Player;
import Util.Globals;
import Util.Utils;
import javafx.scene.image.Image;

public class GoToJail extends Property {
    @Override
    public void buildHouse() {

    }

    @Override
    public int getType() {
        return 6;
    }

    //Sends player to jail
    @Override
    public void onLand() {
        super.onLand();
        Utils.sendCurrentPlayerToJail();
    }

    @Override
    public boolean canBuildHouse(Player player) { return false; }

    @Override
    public void setOwner(Player player) {

    }

    @Override
    public boolean isBuyable() { return false; }

    @Override
    public void onBuy(Player player) {

    }

    @Override
    public int getRent() { return 0; }

    public GoToJail() {
        this.name = "Go to jail";
    }
}
