package Cards;

import Main.GameController;
import Util.Globals;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AdvanceToType extends Card {
    private int type;
    private int rentMultiplier;
    //Card advances player to the nearest square of type (type), represented by an int.
    @Override
    public void onDraw() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Advance to the nearest " + ((type == 1) ? "railroad" : "utility") + ". If unowned, you may buy it. If not, pay the owner " + rentMultiplier + " times the rent owed.", ButtonType.CLOSE);
        alert.showAndWait();
        for (int i = Globals.currentPlayerRound.getPosition(); i < Globals.propertyList.size() + Globals.currentPlayerRound.getPosition(); i++) {
            int pos = i;
            if (i >= Globals.propertyList.size()) pos -= Globals.propertyList.size();
            if (Globals.propertyList.get(pos).getType() == type) {
                Globals.currentPlayerRound.move(i - Globals.currentPlayerRound.getPosition());
                if (Globals.propertyList.get(pos).getOwner() != null && Globals.propertyList.get(pos).getOwner() != Globals.currentPlayerRound) {
                    Globals.currentPlayerRound.sendMoney(Globals.propertyList.get(pos).getOwner(), Math.min(0, Globals.propertyList.get(pos).getRent() * (rentMultiplier - 1)));
                    GameController.controller.log(Globals.currentPlayerRound.getName() + " paid an additional $" + Math.min(0, Globals.propertyList.get(pos).getRent() * (rentMultiplier - 1)));
                }
                return;
            }
        }
    }
    public AdvanceToType(int type, int rentMultiplier) {
        this.type = type;
        this.rentMultiplier = rentMultiplier;
    }
}
