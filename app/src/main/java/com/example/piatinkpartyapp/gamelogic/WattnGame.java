package com.example.piatinkpartyapp.gamelogic;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.cards.WattnDeck;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.Responses;

import java.util.ArrayList;
import java.util.logging.Logger;

public class WattnGame extends Game {

    public WattnDeck deck;
    //logging for testing purposes
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());

    public WattnGame(Lobby l){
        lobby = l;
      //  resetWattnDeck(l.getPlayers().size());

    }
    public void resetWattnDeck(Integer numberOfPlayers){
        deck = new WattnDeck(GameName.Wattn,numberOfPlayers);
    }
    //starting the game
    @Override
    public void startGameWattn(){
        new Thread(()->{
            resetWattnDeck(lobby.getPlayers().size());
            sendGameStartedMessageToClients();
            resetRoundFinished();
            sendHandCards();
            setRoundStartPlayer(lobby.getPlayers().get(0));
            notifyPlayerYourTurn(lobby.getPlayers().get(0));
        }).start();
    }
    //sending handcards to players
    public void sendHandCards() {
        for (Player player: lobby.getPlayers()) {
            ArrayList<Card> handCards = deck.getHandCards();
            player.setHandcards(handCards);

            // send message to client with handcards
            sendHandCardsToPlayer(handCards, player);
            //messages for player 1 to set schlag & player 2 to set trump
            if(player.getId() == 1){
                player.getClientConnection().sendTCP(new Responses.NotifyPlayerToSetSchlag());
            }else if(player.getId() == 2){
                player.getClientConnection().sendTCP(new Responses.NotifyPlayerToSetTrump());
            }
        }
    }
    public void sendHandCardsToPlayer(ArrayList<Card> handCards, Player player){
        Responses.SendHandCards request = new Responses.SendHandCards();
        request.cards = handCards;
        request.playerID = player.getClientConnection().getID();
        player.getClientConnection().sendTCP(request);
    }
    //cheating function to give calling player the best /right card
    @Override
    public void givePlayerBestCard(int playerId) {
      sendPlayerBestCard(playerId, new Card(this.deck.getTrump(),this.deck.getHit()));

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

        player.setCheaten(true);
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
            if(winningPlayer.getCardPlayed().getCardValue() == this.deck.getRightCard().getCardValue() && winningPlayer.getCardPlayed().getSymbol() == deck.getRightCard().getSymbol()){
                winningPlayer = winningPlayer;
            }
            //second played card is right card
            else if(currentPlayer.getCardPlayed().getSymbol() == this.deck.getRightCard().getSymbol() && currentPlayer.getCardPlayed().getCardValue() == deck.getRightCard().getCardValue()){
                winningPlayer = currentPlayer;

            } //hit case - first played hit wins
            else if(winningPlayer.getCardPlayed().getCardValue() == this.deck.getRightCard().getCardValue() ){
                winningPlayer = winningPlayer;
            }//hit case - hit wins
            else if(currentPlayer.getCardPlayed().getCardValue() ==this.deck.getHit() && winningPlayer.getCardPlayed() != this.deck.getRightCard()){

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
            /*Integer p = playerPoints.get(winningPlayer.getId()-1);
            p++;
            playerPoints.set(winningPlayer.getId()-1,p);*/
            if(winningPlayer.getPoints() == 3){
                LOG.info(winningPlayer + " won this game!");

                sendEndRoundMessageToPlayers(roundStartPlayer);
                return winningPlayer;
            }
            currentPlayer = getNextPlayer(currentPlayer);

        }

        return winningPlayer;
    }


    public void addPointsToWinnerPlayer(Player winnerPlayer) {
         if(lobby.getPlayers().size()==3 && (winnerPlayer.getId() == 2 || winnerPlayer.getId() == 3)){
            lobby.getPlayerByID(2).addPoints(1);
            sendPointsToWinnerPlayer(lobby.getPlayerByID(2));
            lobby.getPlayerByID(3).addPoints(1);
            sendPointsToWinnerPlayer(lobby.getPlayerByID(3));
        }else if(lobby.getPlayers().size()== 4){
             if(winnerPlayer.getId() == 1 || winnerPlayer.getId() == 3){
                 lobby.getPlayerByID(1).addPoints(1);
                 sendPointsToWinnerPlayer(lobby.getPlayerByID(1));
                 lobby.getPlayerByID(3).addPoints(1);
                 sendPointsToWinnerPlayer(lobby.getPlayerByID(3));
             }else if(winnerPlayer.getId() == 2 || winnerPlayer.getId() == 4){
                 lobby.getPlayerByID(2).addPoints(1);
                 sendPointsToWinnerPlayer(lobby.getPlayerByID(2));
                 lobby.getPlayerByID(4).addPoints(1);
                 sendPointsToWinnerPlayer(lobby.getPlayerByID(4));
             }
         }else {
             winnerPlayer.addPoints(1);
             sendPointsToWinnerPlayer(winnerPlayer);
         }


    }

    public void startNewRoundWattn(Player startPlayer) {
        new Thread(()->{
            if (startPlayer.getPoints() >= 3 ) {
                // if the player gets at least 66 points then the player wins
                // TODO: test if player can win on other places? wenn 20 oder 40 angesagt wird?
                sendEndRoundMessageToPlayers(startPlayer);

            }else if(startPlayer.getHandcards().isEmpty()){
                sendEndRoundMessageToPlayers(startPlayer);
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
            Responses.WattnStartedClientMessage request = new Responses.WattnStartedClientMessage();
            player.getClientConnection().sendTCP(request);
        }
    }
   /* public void resetVotingFinished() {
        for (Player player : players) {
            player.setVotingFinished(false);
        }
    }*/
/*@Override
    public Player addPlayer(Connection connection, String playerName) {
        Player player = new Player(connection, playerName);
        players.add(player);
        return player;
    }




    @Override
    public void resetSchnopsnDeck(){}
    //2 players for beginning


    @Override
    public Player getRoundWinnerPlayerSchnopsn(){return null;}








@Override
    public Player getNextPlayer(Player player) {
        int currentIndex = players.indexOf(player);
        if (currentIndex == players.size()-1) {
            currentIndex = 0;
            //LOG.info("No next player! return first player");
        } else {
            currentIndex = currentIndex + 1;
            //LOG.info("Index of next player: " + currentIndex);
        }
        return players.get(currentIndex);
    }
    @Override
    public boolean checkIfAllPlayersFinishedRound() {
        for (Player player: players) {
            if (!player.isRoundFinished()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void resetRoundFinished() {
        for (Player player: players) {
            player.setRoundFinished(false);
        }
    }
    @Override
    public void sendEndRoundMessageToPlayers(Player roundWinner){
        for (Player player: players) {
            Responses.EndOfRound response = new Responses.EndOfRound();
            response.playerID = roundWinner.getId();
            player.getClientConnection().sendTCP(response);
        }
    }
    @Override
    public void sendTrumpToAllPlayers(Symbol symbol) {
        for (Player player: players) {
            Responses.SendTrumpToAllPlayers response = new Responses.SendTrumpToAllPlayers();
            response.trump = symbol;
            player.getClientConnection().sendTCP(response);
        }
    }
    public void sendSchlagToAllPlayers(CardValue schlag){
    for(Player player: players){
        Responses.SendSchlagToAllPlayers response = new Responses.SendSchlagToAllPlayers();
        response.schlag = schlag;
        player.getClientConnection().sendTCP(response);
    }
    }

  */
}
