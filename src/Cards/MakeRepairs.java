package Cards;

import Main.GameController;
import Util.Globals;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class MakeRepairs extends Card {
    private int repairsPerHouse;
    private int repairsPerHotel;
    //Forces players to repair based on their hotel and house amount
    @Override
    public void onDraw() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pay " + repairsPerHouse + " for each house you own and " + repairsPerHotel + " for each hotel you own.", ButtonType.CLOSE);
        alert.showAndWait();
        int amount = repairsPerHotel * (Globals.hotelAmount - Globals.currentPlayerRound.getHotelsLeft()) + repairsPerHouse * (Globals.houseAmount - Globals.currentPlayerRound.getHousesLeft());
        Globals.currentPlayerRound.deductMoney(amount);
        GameController.controller.log(Globals.currentPlayerRound.getName() + " lost $" + amount);
        GameController.controller.updatePlayerStats();
    }
    public MakeRepairs(int repairsPerHouse, int repairsPerHotel) {
        this.repairsPerHotel = (int) (repairsPerHotel * Globals.houseRentMultiplier);
        this.repairsPerHouse = (int) (repairsPerHouse * Globals.houseRentMultiplier);
    }
}
