package Squares;

import Main.GameController;
import Players.Player;
import Util.Globals;
import Util.Utils;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Property {
    protected int[] rents;
    public abstract int getType();
    public int[] getRents() {
        return this.rents;
    }
    final int maxHouses = 5;
    protected int houseCost;
    private Rectangle colorIdentifier;

    public int getHouseCost() {
        return houseCost;
    }

    public boolean isBuyable() { return true; }
    protected boolean mortgaged;

    public boolean isMortgaged() { return mortgaged; }
    //called to mortgage
    public void mortgage() {
       this.owner.addMoney(getBuyCost()/2);
       mortgaged = true;
       GameController.controller.log(this.owner.getName() + " mortgaged " + this.getName() + " for $" + getBuyCost()/2);
    }
    //called to unmortgage
    public void unmortgage() {
        this.owner.deductMoney(getBuyCost()/2 + (int) (0.1 * getBuyCost()));
        mortgaged = false;
        GameController.controller.log(this.owner.getName() + " unmortgaged " + this.getName() + " for $" + (getBuyCost()/2 + (int) (0.1 * getBuyCost())));
    }
    public boolean canSellHouse() {
        return false;
    }
    //sell one house
    public void sellHouse() {
        if (houses > 0) houses--;
        if (houses >= maxHouses - 1) {
            owner.setHotelsLeft(owner.getHotelsLeft() + 1);
            owner.setHousesLeft(owner.getHousesLeft() - 4);
        } else owner.setHousesLeft(owner.getHousesLeft() - 1);
    }

    protected String name;

    public String getName() { return name; }

    protected int buyCost;

    public int getBuyCost() { return buyCost; }

    public int getAmountOfHouses() { return houses; }
    public abstract void buildHouse();
    public abstract boolean canBuildHouse(Player player);
    protected int houses;
    protected Player owner = null;
    //various getters
    public Player getOwner() { return owner; }

    public String getOwnerName() {
        if (owner == null) return "Unowned";
        else return owner.getName();
    }

    //logic to setOwner
    public void setOwner(Player owner) {
        if (this.owner != null) this.owner.getProperties().remove(this);
        this.owner = owner;
        if (owner != null)this.owner.getProperties().add(this);
        else this.mortgaged = false;
        //adds a color rectangle if non exist
        if (colorIdentifier == null) {
            int index = Globals.propertyList.indexOf(this);
            double[] coords = Utils.getPropertyCoordinates(this);
            if (index <= 10) colorIdentifier = Utils.newRect(Globals.normalWidth, 15, Color.WHITE, coords[0], coords[2] - 15);
            else if (index <= 20) colorIdentifier = Utils.newRect(15, Globals.normalWidth, Color.WHITE, coords[1] , coords[3]);
            else if (index <= 30) colorIdentifier = Utils.newRect(Globals.normalWidth, 15, Color.WHITE, coords[0], coords[3]);
            else colorIdentifier = Utils.newRect(15, Globals.normalWidth, Color.WHITE, coords[0] - 15, coords[3] - Globals.normalWidth/2);
            GameController.controller.boardPane.getChildren().add(colorIdentifier);
        }
        //sets the fill, checks for null as well
        colorIdentifier.setFill((owner != null) ? owner.getColor() : Color.WHITE);
    }
    protected Color color;

    public Color getColor() { return color; }
    //called whenever landed
    public void onLand() {
        GameController.controller.log(Globals.currentPlayerRound.getName() + " has landed on " + this.getName());
        //sees if player should pay rent
        if (this.owner != null && this.owner != Globals.currentPlayerRound && !this.isMortgaged()) {
            Globals.currentPlayerRound.sendMoney(this.owner, this.getRent());
            GameController.controller.log(Globals.currentPlayerRound.getName() + " paid a rent of $" + this.getRent() + " to " + this.owner.getName());
            GameController.controller.updatePlayerStats();
        } else if (this.isMortgaged()) GameController.controller.log(this.getName() + " is mortgaged so no rent is paid. " + ((Globals.canBuyMortgages) ? "You can buy this property for the original price." : ""));
    }
    public void onBuy(Player player) {
        //executes statement whenever player buys something, sets owner as well
        this.setOwner(player);
        GameController.controller.log(player.getName() + " bought " + this.getName() + " for $" + this.getBuyCost());
        GameController.controller.updatePlayerStats();
        GameController.controller.updatePropertyManagement();
        GameController.controller.initTrade();
    }

    public abstract int getRent();
}
