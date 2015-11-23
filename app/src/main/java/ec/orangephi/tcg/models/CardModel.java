package ec.orangephi.tcg.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by gesuwall on 11/22/15.
 */
public class CardModel extends RealmObject{
    @PrimaryKey
    private String code;
    private int index;
    private int quantity;
    private String text;

    public CardModel(String code, String text, int quantity, int index) {
        this.code = code;
        this.text = text;
        this.quantity = quantity;
        this.index = index;
    }

    public CardModel(){

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
