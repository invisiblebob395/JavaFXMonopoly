package Cards;

import Util.Globals;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class GetOutOfJail extends Card {
    @Override
    //Get out of jail free card
    public void onDraw() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Get out of jail free card. May be used.", ButtonType.CLOSE);
        alert.showAndWait();
        Globals.currentPlayerRound.getCards().add(this);
    }
    public GetOutOfJail() {type = 2;}
}
