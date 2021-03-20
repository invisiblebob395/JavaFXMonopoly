package Cards;

import Main.GameController;
import Util.Globals;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class BankPaysX extends Card {
    private int amount;
    //Free X amount of money
    @Override
    public void onDraw() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Bank pays you " + amount, ButtonType.CLOSE);
        alert.showAndWait();
        Globals.currentPlayerRound.addMoney(amount);
        GameController.controller.log(Globals.currentPlayerRound.getName() + " received $" + amount + ".");
        GameController.controller.updatePlayerStats();
    }
    public BankPaysX(int amount) {
        this.amount = amount;
    }
}
