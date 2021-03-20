package Squares;

import Players.Player;
import Util.Globals;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class JustVisiting extends Property {
    @Override
    public void buildHouse() {

    }

    @Override
    public int getType() {
        return 7;
    }

    @Override
    public boolean canBuildHouse(Player player) {
        return false;
    }

    @Override
    public void setOwner(Player player) {

    }

    @Override
    public boolean isBuyable() { return false; }

    @Override
    public int getRent() {
        return 0;
    }
    public JustVisiting() {
        this.name = "Just Visiting";
    }
}
