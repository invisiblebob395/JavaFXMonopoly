package Cards;

import Main.GameController;
import Players.Player;
import Util.Globals;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class TakeXMoney extends Card {
    private int amount;
    //Collects $amount from each other player
    @Override
    public void onDraw() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Collect $" + amount + " from each person.", ButtonType.CLOSE);
        alert.showAndWait();
        for (Player player : Globals.playerList) if (player != Globals.currentPlayerRound) player.sendMoney(Globals.currentPlayerRound, amount);
        GameController.controller.log(Globals.currentPlayerRound.getName() + " received $" + amount * (Globals.playerList.size() - 1));
        GameController.controller.updatePlayerStats();
    }
    public TakeXMoney(int amount) {
        this.amount = amount;
    }
}
