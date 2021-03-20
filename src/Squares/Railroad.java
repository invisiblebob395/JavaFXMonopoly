package Squares;

import Players.Player;
import Util.Globals;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Railroad extends Property {

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public void buildHouse() {

    }

    @Override
    public boolean canBuildHouse(Player player) {
        return false;
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public void onBuy(Player player) {
        super.onBuy(player);
        player.setRailroadOwned(player.getRailroadOwned() + 1);
    }

    @Override
    public int getRent() {
        return (int) (25 * Math.pow(2, owner.getRailroadOwned() - 1) * Globals.rentMultiplier);
    }
    public Railroad(String name, int buyCost) {
        this.name = name;
        this.buyCost = buyCost;
    }
}
