package Squares;

import Players.Player;
import Util.Globals;
import javafx.scene.image.Image;

public class FreeParking extends Property {
    @Override
    public void buildHouse() {

    }

    @Override
    public int getType() {
        return 4;
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
    public FreeParking() {
        this.name = "Free Parking";
    }
}
