package com.example.piatinkpartyapp.gamelogic;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
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
        deck.setHit(CardValue.randomValue());
        deck.setTrump(Symbol.randomSymbol());
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
    @Override
    public void sendHandCards() {
        for (Player player: players) {
            ArrayList<Card> handCards = deck.getHandCards();
            player.setHandcards(handCards);

            // send message to client with handcards
            Packets.Responses.SendHandCards request = new Packets.Responses.SendHandCards();
            request.cards = handCards;
            request.playerID = player.getClientConnection().getID();
            player.getClientConnection().sendTCP(request);
        }
    }
    @Override
    public void setCard(int playerID, Card card) {
        Player player = getPlayerByID(playerID);

        //only for testing
        //Card card2 = player.getHandcards().get(0);

        new Thread(()->{
            player.setRoundFinished(true);
            LOG.info("setRoundFinished = true");

            player.setCardPlayed(card);
            Log.e("##########", card.getCardValue() +" "+ card.getSymbol());
            LOG.info("set Playercard of player: " + player.getId() + " card: " +  card.getSymbol().toString() + card.getCardValue().toString());

            player.removeHandcard(card);
            LOG.info("card removed from handcards");

            if (checkIfAllPlayersFinishedRound()) {
                // gelegte Karten vergleichen und Stich zu cardsWon + Punkte dazurechnen
                LOG.info("RoundFinished. Trump is: " + deck.getTrump().toString());

                Player roundWonPlayer = getRoundWinnerWattn();
                LOG.info("Round won by Player: " + roundWonPlayer.getId());

                addPointsToWinnerPlayer(roundWonPlayer);
                LOG.info("Points added to winner player: " + roundWonPlayer.getId() + ". Points: " + roundWonPlayer.getPoints());

                //TODO: check if one player have enough points
                startNewRoundSchnopsn(roundWonPlayer);
            } else {
                // Nächsten Spieler benachrichtigen dass er dran ist
                LOG.info("notify next player: " + getNextPlayer(player).getId());
                notifyPlayerYourTurn(getNextPlayer(player));
            }
        }).start();
    }

}
