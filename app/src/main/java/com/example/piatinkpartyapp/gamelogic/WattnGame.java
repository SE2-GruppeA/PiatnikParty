package com.example.piatinkpartyapp.gamelogic;
import android.util.Log;
import android.widget.Toast;

import com.esotericsoftware.kryonet.Connection;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.cards.WattnDeck;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.Packets;

import java.util.ArrayList;
import java.util.logging.Logger;
public class WattnGame extends Game {
    private int mainPlayerId;
    public Player roundStartPlayer;
   // public ArrayList<Player> players = new ArrayList<>();
    public ArrayList<Integer> playerPoints = new ArrayList<>();
    public WattnDeck deck;



    //logging for testing purposes
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());

    public WattnGame(){resetWattnDeck();}

    @Override
    public void resetSchnopsnDeck(){}
    //2 players for beginning
    public void resetWattnDeck(){
        deck = new WattnDeck(GameName.Wattn,2);
        //setting hit & trump randomly 4 testing because UI isn't connected to game logic via live data yet
       // deck.setHit(CardValue.randomValue());
       // deck.setHit(CardValue.ZEHN);
        //deck.setTrump(Symbol.HERZ);

        //deck.rightCard = deck.getRightCard();
      //  deck.setTrump(Symbol.randomSymbol());
        LOG.info("hit is "+ deck.getHit());
        LOG.info("trump is "+deck.getTrump());
        playerPoints.add(0);
        playerPoints.add(0);
    }
    public CardValue setHit(CardValue cv){
        this.deck.setHit(cv);
        return cv;
    }
    public Symbol setTrump(Symbol s){
        this.deck.setTrump(s);
        return s;
    }
    public Card rightCard(){
        return this.deck.getRightCard();
    }
    @Override
    public Player getRoundWinnerPlayerSchnopsn(){return null;}

    public Player getRoundWinnerWattn(){
        Player winningPlayer = this.roundStartPlayer;
        Player currentPlayer = getNextPlayer(this.roundStartPlayer);
        while (currentPlayer != this.roundStartPlayer) {
            //the player that plays the right card always wins the subround
            // first played card is right card
           if(winningPlayer.getCardPlayed().getCardValue() == deck.getRightCard().getCardValue() && winningPlayer.getCardPlayed().getSymbol() == deck.getRightCard().getSymbol()){
               winningPlayer = winningPlayer;
           }
           //second played card is right card
           else if(currentPlayer.getCardPlayed().getSymbol() == deck.getRightCard().getSymbol() && currentPlayer.getCardPlayed().getCardValue() == deck.getRightCard().getCardValue()){
                winningPlayer = currentPlayer;

            } //hit case - first played hit wins
           else if(winningPlayer.getCardPlayed().getCardValue() == deck.getRightCard().getCardValue() ){
               winningPlayer = winningPlayer;
           }//hit case - hit wins
            else if(currentPlayer.getCardPlayed().getCardValue() ==deck.getHit() && winningPlayer.getCardPlayed() != deck.getRightCard()){

                winningPlayer = currentPlayer;
            } // trump case - higher trump wins
            else if(winningPlayer.getCardPlayed().getSymbol() == deck.getTrump()){
                if(currentPlayer.getCardPlayed().getSymbol() == deck.getTrump() && deck.cardPoints(currentPlayer.getCardPlayed().cardValue) > deck.cardPoints(winningPlayer.getCardPlayed().cardValue)){
                    winningPlayer = currentPlayer;
                }
                //default cases: if same colour higher value wins, if trump & other value trump wins
            }else if(currentPlayer.getCardPlayed().getSymbol() == deck.getTrump() || (winningPlayer.getCardPlayed().getSymbol() == currentPlayer.getCardPlayed().getSymbol() && deck.cardPoints(currentPlayer.getCardPlayed().cardValue) > deck.cardPoints(winningPlayer.getCardPlayed().cardValue))){
                winningPlayer = currentPlayer;
            }
            Integer p = playerPoints.get(winningPlayer.getId()-1);
            p++;
            playerPoints.set(winningPlayer.getId()-1,p);
            currentPlayer = getNextPlayer(currentPlayer);
        }
        for(Integer p : playerPoints){
            if(p==3){
                LOG.info(winningPlayer + " won this game!");
            }
        }
        return winningPlayer;
    }

}
