package Squares;

import Main.GameController;
import Players.Player;
import Util.Globals;
import javafx.scene.image.Image;

import java.util.Random;

public class Chance extends Property {

    @Override
    public void buildHouse() {

    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public boolean isBuyable() { return false; }

    @Override
    public boolean canBuildHouse(Player player) {
        return false;
    }

    @Override
    public void onLand() {
        super.onLand();
        //removes a chance card and calls its ondraw
        Globals.chanceList.remove((new Random()).nextInt(Globals.chanceList.size())).onDraw();
        if (Globals.chanceList.size() == 0) GameController.controller.restockChanceCards();
    }

    @Override
    public int getRent() {
        return 0;
    }
    public Chance() {
        this.name = "Chance";
    }
}
