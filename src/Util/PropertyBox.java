package Util;

import Squares.Property;
import javafx.scene.control.CheckBox;

public class PropertyBox extends CheckBox {
    private Property property; //basically a checkbox that represents a property
    public Property getProperty() {
        return property;
    }
    public PropertyBox(Property property, String text) {
        super(text);
        this.property = property;
    }
}
