package Squares;

import Main.GameController;
import Players.Player;
import Util.Globals;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class Street extends Property {
    private final int maxHouses = 5;

    @Override
    public boolean canBuildHouse(Player player) {
        boolean equalHouses = true;
        for (Property property : player.getProperties()) {
            if (property.getColor() == this.getColor() && this.getAmountOfHouses() > property.getAmountOfHouses()) {
                equalHouses = false;
                break;
            }
        }
        return (equalHouses && owner == player && houses < maxHouses && player.hasMonopoly(color) && (((houses < maxHouses - 1 && owner.getHousesLeft() > 0) || (houses >= maxHouses - 1 && owner.getHotelsLeft() > 0))));
    }

    //builds a house, takes away houses and hotels of owner
    public void buildHouse() {
        if (houses < maxHouses) {
            houses++;
            if (houses < maxHouses - 1) owner.setHousesLeft(owner.getHousesLeft() - 1);
            else {
                owner.setHotelsLeft(owner.getHotelsLeft() - 1);
                owner.setHousesLeft(owner.getHousesLeft() + maxHouses - 1);
            }
            GameController.controller.log(owner.getName() + " built a house for $"+ this.getHouseCost());
        }
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getRent() { return rents[houses]; }

    @Override
    public boolean canSellHouse() {
        if (this.getAmountOfHouses() < 1) return false;
        for (Property property : this.getOwner().getProperties()) if (property.getColor() == this.getColor() && this.getAmountOfHouses() < property.getAmountOfHouses()) return false;
        System.out.println(true);
        return true;
    }

    private Color color;

    public Color getColor() { return color; }

    public Street(String name, int buyCost, int houseCost, Color color, int rent, int houseRent1, int houseRent2, int houseRent3, int houseRent4, int houseRent5) {
        this.name = name;
        this.buyCost = buyCost;
        this.color = color;
        this.houseCost = houseCost;
        this.rents = new int[]{(int) (rent * Globals.rentMultiplier), houseRent1, houseRent2,  houseRent3, houseRent4, houseRent5};
        for (int i = 1; i < this.rents.length; i++) rents[i] = (int) (rents[i] * Globals.rentMultiplier * Globals.houseRentMultiplier);
    }

}
