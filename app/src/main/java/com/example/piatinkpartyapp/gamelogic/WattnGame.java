package com.example.piatinkpartyapp.gamelogic;

import android.util.Log;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.cards.WattnDeck;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.responses.responseNotifyPlayerToSetSchlag;
import com.example.piatinkpartyapp.networking.responses.responseNotifyPlayerToSetTrump;
import com.example.piatinkpartyapp.networking.responses.responseSendHandCards;
import com.example.piatinkpartyapp.networking.responses.responseWattnStartedClientMessage;

import java.util.ArrayList;
import java.util.logging.Logger;

public class WattnGame extends Game {

    public WattnDeck deck;
    //logging for testing purposes
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());

    public WattnGame(Lobby l){
        lobby = l;


    }
    public void resetWattnDeck(Integer numberOfPlayers){
        deck = new WattnDeck(GameName.Wattn,numberOfPlayers);
    }
    //starting the game
    @Override
    public void startGame(){
        new Thread(()->{
            resetWattnDeck(lobby.getPlayers().size());
            resetRoundFinished();
            sendMessageUpdateScoreboard();
            resetPlayerPoints();
            resetCheating();
            sendGameStartedMessageToClients();


            Player roundStartPlayer = getRandomPlayer();
            setRoundStartPlayer(roundStartPlayer);
            sendHandCards();
            notifyPlayerYourTurn(roundStartPlayer);
            //reset player points
            for(Player p: lobby.getPlayers()){
                p.addPoints(-1*p.getPoints());
                sendPointsToWinnerPlayer(p);
            }
        }).start();
    }
    //sending handcards to players
    public void sendHandCards() {
        roundStartPlayer = this.getRoundStartPlayer();
        LOG.info(roundStartPlayer.toString());
        for (Player player: lobby.getPlayers()) {
            ArrayList<Card> handCards = deck.getHandCards();
            player.setHandcards(handCards);

            // send message to client with handcards
            sendHandCardsToPlayer(handCards, player);
            //messages for player 1 to set schlag & player 2 to set trump
            if(player.getId().equals(roundStartPlayer.getId())){
                player.getClientConnection().sendTCP(new responseNotifyPlayerToSetSchlag());
            }else if(player.getId().equals(getNextPlayer(roundStartPlayer).getId())){
                player.getClientConnection().sendTCP(new responseNotifyPlayerToSetTrump());
            }
        }
    }
    public void sendHandCardsToPlayer(ArrayList<Card> handCards, Player player){
        responseSendHandCards request = new responseSendHandCards();
        request.cards = handCards;
        request.playerID = player.getClientConnection().getID();
        player.getClientConnection().sendTCP(request);
    }
    //cheating function to give calling player the best /right card
    @Override
    public void givePlayerBestCard(int playerId) {
      sendPlayerBestCard(playerId, new Card(this.deck.getTrump(),this.deck.getHit()));
      lobby.getPlayerByID(playerId).setCheaten(true);

    }
    //replaces the first hand card with the best card in the game
    //adopted from schopsnGame since it has the same functionality for wattn
    public void sendPlayerBestCard(int playerId, Card card){
        Player player = lobby.getPlayerByID(playerId);

        ArrayList<Card> currentHandCards = player.getHandcards();

        //replaces first card with the best card

        currentHandCards.set(0, card);
        player.setHandcards(currentHandCards);

        //sends new handcards to the player
        sendHandCardsToPlayer(currentHandCards, player);

    }

    @Override
    public void setCard(int playerID, Card card) {

        Player player = lobby.getPlayerByID(playerID);

        new Thread(()->{

            player.setRoundFinished(true);
            LOG.info("setRoundFinished = true");

            player.setCardPlayed(card);

            LOG.info("set Playercard of player: " + player.getId() + " card: " +  card.getSymbol().toString() + card.getCardValue().toString());

            sendPlayedCardToAllPlayers(playerID, card);

            player.removeHandcard(card);
            LOG.info("card removed from handcards");

            if (checkIfAllPlayersFinishedRound()) {
                // gelegte Karten vergleichen und Stich zu cardsWon + Punkte dazurechnen
                LOG.info("RoundFinished. Trump is: " + deck.getTrump().toString());

                Player roundWonPlayer = getRoundWinnerWattn();
                LOG.info("Round won by Player: " + roundWonPlayer.getId());

                addPointsToWinnerPlayer(roundWonPlayer);
                LOG.info("Points added to winner player: " + roundWonPlayer.getId() + ". Points: " + roundWonPlayer.getPoints());
                sendRoundWinnerPlayerToAllPlayers(roundWonPlayer);

                startNewRoundWattn(roundWonPlayer);
            } else {
                // Nächsten Spieler benachrichtigen dass er dran ist
                LOG.info("notify next player: " + getNextPlayer(player).getId());
                notifyPlayerYourTurn(getNextPlayer(player));
            }
        }).start();
    }
    public Player getRoundWinnerWattn(){

        ArrayList<Player> winners = new ArrayList<>();
        Player winningPlayer = this.roundStartPlayer;
        LOG.info(winningPlayer.toString());
        Player currentPlayer = getNextPlayer(this.roundStartPlayer);

        while (currentPlayer != this.roundStartPlayer) {
            //the player that plays the right card always wins the subround
            // first played card is right card
            LOG.info(winningPlayer.getCardPlayed().getCardValue().toString());
            LOG.info(currentPlayer.getCardPlayed().getCardValue().toString());
            LOG.info(deck.toString());

            LOG.info(this.deck.getTrump().toString());
            LOG.info(this.deck.getHit().toString());

            if (winningPlayer.getCardPlayed().getCardValue()
                    == this.deck.getHit()
                    && winningPlayer.getCardPlayed().getSymbol()
                    == deck.getTrump()){
                return winningPlayer;
            }

            //second played card is right card
            else if (currentPlayer.getCardPlayed().getSymbol() == this.deck.getTrump() && currentPlayer.getCardPlayed().getCardValue() == deck.getHit()){
                winningPlayer = currentPlayer;

            } //hit case - first played hit wins
            else if(winningPlayer.getCardPlayed().getCardValue() == this.deck.getHit() ){
                return winningPlayer;
            }//hit case - hit wins
            else if(currentPlayer.getCardPlayed().getCardValue() ==this.deck.getHit() && (winningPlayer.getCardPlayed().cardValue != this.deck.getHit())){

                winningPlayer = currentPlayer;
            } // trump case - higher trump wins
            else if(winningPlayer.getCardPlayed().getSymbol() == this.deck.getTrump()){
                if(currentPlayer.getCardPlayed().getSymbol() == this.deck.getTrump() && this.deck.cardPoints(currentPlayer.getCardPlayed().cardValue) > this.deck.cardPoints(winningPlayer.getCardPlayed().cardValue)){
                    winningPlayer = currentPlayer;
                }
                //default cases: if same colour higher value wins, if trump & other value trump wins
            }else if(currentPlayer.getCardPlayed().getSymbol() == this.deck.getTrump() || (winningPlayer.getCardPlayed().getSymbol() == currentPlayer.getCardPlayed().getSymbol() && this.deck.cardPoints(currentPlayer.getCardPlayed().cardValue) > this.deck.cardPoints(winningPlayer.getCardPlayed().cardValue))){
                winningPlayer = currentPlayer;
            }


            currentPlayer = getNextPlayer(currentPlayer);

        }

        return winningPlayer;
    }


    public void addPointsToWinnerPlayer(Player winnerPlayer) {
         if(lobby.getPlayers().size()==3) {
             Player nextPlayer = getNextPlayer(winnerPlayer);
             if (!winnerPlayer.getId().equals(roundStartPlayer.getId())) {
                 updatePoints(winnerPlayer);
                 if (!nextPlayer.getId().equals(roundStartPlayer.getId())) {
                     updatePoints(nextPlayer);
                 }else{
                     updatePoints(getNextPlayer(nextPlayer));
                 }
             }else{
                 updatePoints(winnerPlayer);
             }
         }
         else if(lobby.getPlayers().size()== 4){
             Player oppositeRoundStart = getNextPlayer(getNextPlayer(roundStartPlayer));
             if(winnerPlayer.equals(roundStartPlayer) || winnerPlayer.equals(oppositeRoundStart)){
                 LOG.info("winners are: "+winnerPlayer.getId() +" ,"+roundStartPlayer.getId() + " ,"+oppositeRoundStart.getId());
                updatePoints(roundStartPlayer);
                LOG.info(roundStartPlayer.getId().toString());
                updatePoints(oppositeRoundStart);
                LOG.info(oppositeRoundStart.getId().toString());
             }else {
                 LOG.info("winners are: "+winnerPlayer.getId() +" ,"+getNextPlayer(roundStartPlayer).getId() + " ,"+getNextPlayer(oppositeRoundStart).getId());
                 Player second = getNextPlayer(roundStartPlayer);
                 LOG.info(second.getId().toString());
                 updatePoints(second);
                 Player fourth = getNextPlayer(oppositeRoundStart);
                 updatePoints(fourth);
                 LOG.info(fourth.getId().toString());
             }
         }else {
            updatePoints(winnerPlayer);
         }
    }
    private void updatePoints(Player player){
        lobby.getPlayerByID(player.getId()).addPoints(1);
        sendPointsToWinnerPlayer(lobby.getPlayerByID(player.getId()));
    }

    public void startNewRoundWattn(Player startPlayer) {
        new Thread(()->{
            ArrayList<Player> winnerIDs = new ArrayList<>();
            LOG.info(startPlayer.getPlayerName() + " has " +startPlayer.getPoints());
            Player other = getNextPlayer(startPlayer);
            LOG.info(other.getPlayerName() + " has " + other.getPoints());
            if (startPlayer.getPoints() >= 3 ) {
                //sendEndRoundMessageToPlayers(startPlayer);

                if(lobby.getPlayers().size() == 3){
                    if(!startPlayer.equals(roundStartPlayer)){
                        Player next = getNextPlayer(startPlayer);
                        Player nextNext = getNextPlayer(next);
                        winnerIDs.add(next);
                        LOG.info(next.getId().toString());
                        winnerIDs.add(nextNext);
                        LOG.info(nextNext.getId().toString());
                        addPointsAndUpdateScoreboard(next, 1);
                        addPointsAndUpdateScoreboard(nextNext, 1);
                    }else{
                        winnerIDs.add(startPlayer);
                        addPointsAndUpdateScoreboard(startPlayer,1);
                    }
                }else if(lobby.getPlayers().size() == 4){
                    Player oppositeOfStart = getNextPlayer(getNextPlayer(roundStartPlayer));
                    if(startPlayer.equals(roundStartPlayer) || startPlayer.equals(oppositeOfStart)){
                        winnerIDs.add(roundStartPlayer);
                        LOG.info("win "+roundStartPlayer.getId().toString());
                        winnerIDs.add(oppositeOfStart);
                        LOG.info("win "+oppositeOfStart.getId().toString());
                        addPointsAndUpdateScoreboard(roundStartPlayer,1);
                        addPointsAndUpdateScoreboard(oppositeOfStart,1);
                    }else{
                        Player next = getNextPlayer(startPlayer);
                        Player oppositeNext = getNextPlayer(oppositeOfStart);
                        winnerIDs.add(next);
                        LOG.info("win "+next.getId().toString());
                        winnerIDs.add(oppositeNext);
                        LOG.info("win "+oppositeNext.toString());
                        addPointsAndUpdateScoreboard(next,1);
                        addPointsAndUpdateScoreboard(oppositeNext,1);
                    }
                }else{
                    winnerIDs.add(startPlayer);
                    addPointsAndUpdateScoreboard(startPlayer, 1);
                }

                sendEndRoundMessageToPlayers(winnerIDs);
            }else if(startPlayer.getHandcards().isEmpty() && other.getHandcards().isEmpty()){

                if(lobby.getPlayers().size() == 2) {
                    if (startPlayer.getPoints() >= other.getPoints()) {
                        //sendEndRoundMessageToPlayers(startPlayer);
                        winnerIDs.add(startPlayer);
                        addPointsAndUpdateScoreboard(startPlayer, 1);
                    } else {
                        //sendEndRoundMessageToPlayers(other);
                        winnerIDs.add(other);
                        addPointsAndUpdateScoreboard(other, 1);
                    }

                }else if(lobby.getPlayers().size() == 3){
                    Player third = getNextPlayer(other);
                    if (startPlayer.getPoints() < other.getPoints() && startPlayer.getPoints() < third.getPoints() ) {

                        winnerIDs.add(other);
                        winnerIDs.add(third);
                        addPointsAndUpdateScoreboard(other, 1);
                        addPointsAndUpdateScoreboard(third,1);
                    } else {
                        winnerIDs.add(startPlayer);
                        addPointsAndUpdateScoreboard(startPlayer, 1);
                    }

                }else if(lobby.getPlayers().size() == 4){

                    Player third = getNextPlayer(other);
                    Player fourth = getNextPlayer(third);
                    if (startPlayer.getPoints() < other.getPoints() && third.getPoints() < fourth.getPoints() ) {

                        winnerIDs.add(other);
                        winnerIDs.add(fourth);
                        addPointsAndUpdateScoreboard(other, 1);
                        addPointsAndUpdateScoreboard(fourth,1);
                    } else {
                        winnerIDs.add(startPlayer);
                        winnerIDs.add(fourth);
                        addPointsAndUpdateScoreboard(startPlayer, 1);
                        addPointsAndUpdateScoreboard(fourth,1);
                    }
                }
                sendEndRoundMessageToPlayers(winnerIDs);
            }
            else {
                resetRoundFinished();
                resetPlayedCard();

                setRoundStartPlayer(startPlayer);
                notifyPlayerYourTurn(startPlayer);
            }
        }).start();
    }
    @Override
    public void setSchlag(CardValue cv){
        this.deck.setHit(cv);
        sendSchlagToAllPlayers(cv);

    }
    @Override
    public void setTrump(Symbol s){
        this.deck.setTrump(s);
        sendTrumpToAllPlayers(s);

    }
    public Card rightCard(){
        return this.deck.getRightCard();
    }
    @Override
    public Symbol getTrump(){
        return deck.getTrump();
    }
    @Override
    public CardValue getSchlag(){
        return deck.getHit();
    }

    @Override
    public void sendGameStartedMessageToClients() {
        for (Player player : lobby.getPlayers()) {
            // send message to client that game has started
            responseWattnStartedClientMessage request = new responseWattnStartedClientMessage();
            player.getClientConnection().sendTCP(request);
        }
    }
    @Override
    public void punishWrongExposure(Integer exposerId){
        Player player = lobby.getPlayerByID(exposerId);

        if(lobby.getPlayers().size()==2){
            penaltyPoints(player,-1);
        }else if(lobby.getPlayers().size() == 3){
            if(!exposerId.equals(roundStartPlayer.getId())){
                //second player  of round
                penaltyPoints(getNextPlayer(roundStartPlayer),-1);
                //third player of round
                penaltyPoints(getNextPlayer(getNextPlayer(roundStartPlayer)),-1);
            }else{
                penaltyPoints(player,-1);
            }
        }else if(lobby.getPlayers().size() == 4){
            Player opposite = getNextPlayer(getNextPlayer(roundStartPlayer));
            if(exposerId.equals(roundStartPlayer.getId()) || exposerId.equals(opposite.getId())){
                penaltyPoints(roundStartPlayer,-1);
                penaltyPoints(opposite,-1);
            }else{
                penaltyPoints(getNextPlayer(roundStartPlayer),-1);
                penaltyPoints(getNextPlayer(opposite),-1);
            }
        }
    }
    private void penaltyPoints(Player player, Integer points){
        player.addPoints(points);
        sendPointsToWinnerPlayer(player);
    }

    @Override
    public void cheaterPenalty(Integer playerId){
        Player player = lobby.getPlayerByID(playerId);

        if(player.isCheaten()){
            if(lobby.getPlayers().size() == 2){
                penaltyPoints(player,-2);
            }else if(lobby.getPlayers().size() == 3){
                if(!playerId.equals(roundStartPlayer.getId())){
                    penaltyPoints(getNextPlayer(roundStartPlayer),-2);
                    penaltyPoints(getNextPlayer(getNextPlayer(roundStartPlayer)),-2);
                }else{
                    penaltyPoints(player,-2);
                }
            }else if(lobby.getPlayers().size() == 4){
                Player oppositeOfRoundStart = getNextPlayer(getNextPlayer(roundStartPlayer));
                if(playerId.equals(roundStartPlayer.getId()) || playerId.equals(oppositeOfRoundStart.getId())) {
                    penaltyPoints(roundStartPlayer,-2);
                    penaltyPoints(oppositeOfRoundStart,-2);
                }else{
                    penaltyPoints(getNextPlayer(roundStartPlayer),-2);
                    penaltyPoints(getNextPlayer(oppositeOfRoundStart),-2);
                }
            }
        }
    }
}
