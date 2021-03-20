package Squares;

import Main.GameController;
import Players.Player;
import Util.Globals;

import java.util.Random;

public class CommunityChest extends Property {
    @Override
    public int getType() {
        return 8;
    }

    @Override
    public void buildHouse() {

    }

    @Override
    public boolean canBuildHouse(Player player) {
        return false;
    }

    @Override
    public boolean isBuyable() {
        return false;
    }

    @Override
    public int getRent() {
        return 0;
    }

    @Override
    public void onLand() {
        super.onLand();
        //removes a community chest card and calls its on draw
        Globals.commList.remove((new Random()).nextInt(Globals.commList.size())).onDraw();
        if (Globals.commList.size() == 0) GameController.controller.restockCommCards();
    }
    public CommunityChest() {this.name = "Community Chest";}
}
