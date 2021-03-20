package Cards;

import Main.GameController;
import Util.Globals;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class LoseXMoney extends Card {
    private int amount;
    //Deducts $amount from current player
    @Override
    public void onDraw() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "You lose $" + amount, ButtonType.CLOSE);
        alert.showAndWait();
        Globals.currentPlayerRound.deductMoney(amount);
        GameController.controller.log(Globals.currentPlayerRound.getName() + " loses $" + amount);
        GameController.controller.updatePlayerStats();
    }
    public LoseXMoney(int amount) {this.amount = amount;}
}
