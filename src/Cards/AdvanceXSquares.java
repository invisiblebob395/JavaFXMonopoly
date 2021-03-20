package Cards;

import Util.Globals;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AdvanceXSquares extends Card {
    private int squares;
    //Advances an X amount of squares
    @Override
    public void onDraw() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Advance " + squares + " squares", ButtonType.CLOSE);
        alert.showAndWait();
        Globals.currentPlayerRound.move(squares);
    }
    public AdvanceXSquares(int squares) {this.squares = squares;}
}
