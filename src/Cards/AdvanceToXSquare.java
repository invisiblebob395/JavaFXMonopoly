package Cards;

import Main.GameController;
import Util.Globals;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AdvanceToXSquare extends Card {
    private int squareToAdvance;
    //Advances to the square with an index of X
    @Override
    public void onDraw() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Advance to " + Globals.propertyList.get(squareToAdvance).getName() + ".\nCollect $" + Globals.goMoney + " if Go is passed.", ButtonType.CLOSE);
        alert.showAndWait();
        if (Globals.currentPlayerRound.getPosition() > squareToAdvance) {
            Globals.currentPlayerRound.addMoney(Globals.goMoney);
            GameController.controller.log(Globals.currentPlayerRound.getName() + " received $" + Globals.goMoney + " from passing Go");
        }
        Globals.currentPlayerRound.move(squareToAdvance - Globals.currentPlayerRound.getPosition());
    }
    public AdvanceToXSquare(int squareToAdvance) {this.squareToAdvance = squareToAdvance;}
}
