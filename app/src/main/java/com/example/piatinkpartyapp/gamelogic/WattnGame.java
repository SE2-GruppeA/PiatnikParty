package com.example.piatinkpartyapp.gamelogic;
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
    private Player roundStartPlayer;
    private ArrayList<Player> players = new ArrayList<>();
    private WattnDeck deck;



    //logging for testing purposes
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());

    public WattnGame(){}

    @Override
    public void resetSchnopsnDeck(){}
    //2 players for beginning
    public void resetWattnDeck(){
        deck = new WattnDeck(GameName.Wattn,2);
        //setting hit & trump randomly 4 testing because UI isn't connected to game logic via live data yet
        deck.setHit(CardValue.randomValue());
        deck.setTrump(Symbol.randomSymbol());
        LOG.info("hit is "+ deck.getHit());
        LOG.info("trump is "+deck.getTrump());
    }

    @Override
    public Player getRoundWinnerPlayerSchnopsn(){return null;}

    public Player getRoundWinnerWattn(){
        Player winningPlayer = this.roundStartPlayer;
        Player currentPlayer = getNextPlayer(this.roundStartPlayer);

        while (currentPlayer != this.roundStartPlayer) {
            //the player that plays the right card always wins the subround
            if(winningPlayer.getCardPlayed() == deck.getRightCard()){
                winningPlayer = winningPlayer;
            }else if(currentPlayer.getCardPlayed() == deck.getRightCard()){
                winningPlayer = currentPlayer;
                //hit case - first played hit wins
            }else if(currentPlayer.getCardPlayed().getCardValue() ==deck.getHit() && winningPlayer.getCardPlayed() != deck.getRightCard()){
                winningPlayer = currentPlayer;
            }
            currentPlayer = getNextPlayer(currentPlayer);
        }
        return winningPlayer;
    }

}
