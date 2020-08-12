package com.cxa.cardUI;

/**
 * Created by dancrisan on 1/15/17.
 */

public class Card {
    private String line1;
    private String line2;
    private float line3;
    private String line4;
    private String line5;
    private int line6;
    private it.gmariotti.cardslib.library.internal.Card.OnCardClickListener cardListener;

    public Card(String line1, String line2, float line3, String line4, String line5, int line6, it.gmariotti.cardslib.library.internal.Card.OnCardClickListener cardListener) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.line4 = line4;
        this.line5 = line5;
        this.line6 = line6;
        this.cardListener = cardListener;
    }
    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public float getLine3() {
        return line3;
    }

    public String getLine4() { return line4; }

    public String getLine5() { return line5; }

    public int getLine6() { return line6; }

    public it.gmariotti.cardslib.library.internal.Card.OnCardClickListener getCickListener() { return cardListener; }

}
