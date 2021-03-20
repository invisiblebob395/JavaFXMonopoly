package Squares;

import Main.GameController;
import Players.Player;
import Util.Globals;
import javafx.scene.image.Image;

public class Go extends Property {
    @Override
    public void buildHouse() {

    }

    @Override
    public int getType() {
        return 5;
    }

    @Override
    public boolean isBuyable() {
        return false;
    }

    @Override
    public boolean canBuildHouse(Player player) {
        return false;
    }

    @Override
    public void setOwner(Player player) {

    }

    @Override
    public int getRent() {
        return 0;
    }
    public Go() {
        this.name = "Go";
    }
}
