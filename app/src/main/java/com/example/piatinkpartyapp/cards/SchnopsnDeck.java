package com.example.piatinkpartyapp.cards;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SchnopsnDeck extends Deck{
    /*specialization of card-deck tailored for schnopsn
    * only 20 cards from less cardvalues
    * selected trump symbol
    * map of points of each cardValue for easier point / win calculation*/
    Symbol trump;
    HashMap<CardValue, Integer> point_map;
    ArrayList<CardValue> schnopsnCardValues;


}

