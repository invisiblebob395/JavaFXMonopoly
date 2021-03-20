package Cards;

import Main.GameController;
import Util.Globals;
import Util.Utils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class GoToJail extends Card{
    @Override
    public void onDraw() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Go straight to jail. Do not collect money from Go", ButtonType.CLOSE);
        alert.showAndWait();
        Utils.sendCurrentPlayerToJail();
    }
}
