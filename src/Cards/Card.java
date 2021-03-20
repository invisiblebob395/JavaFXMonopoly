package Cards;

public abstract class Card {
    protected int type;

    public int getType() {
        return type;
    }

    public abstract void onDraw();
}
