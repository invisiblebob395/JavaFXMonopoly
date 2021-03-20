package Cards;

import Main.GameController;
import Players.Player;
import Util.Globals;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class PayMoneyToEveryone extends Card {
    private int amount;
    //Forces player to pay $amount to each other player
    @Override
    public void onDraw() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pay $" + amount + " to each player.", ButtonType.CLOSE);
        alert.showAndWait();
        for (Player player : Globals.playerList) if (player != Globals.currentPlayerRound) Globals.currentPlayerRound.sendMoney(player, amount);
        GameController.controller.log(Globals.currentPlayerRound.getName() + " lost $" + amount * (Globals.playerList.size() - 1));
        GameController.controller.updatePlayerStats();
    }

    public PayMoneyToEveryone(int amount) {
        this.amount = amount;
    }
}
