package com.example.piatinkpartyapp.gamelogic;

public class WattnGame extends Game {
/*    private int mainPlayerId;
    public Player roundStartPlayer;
    // public ArrayList<Player> players = new ArrayList<>();
    public ArrayList<Integer> playerPoints = new ArrayList<>();
    public ArrayList<Player> players = new ArrayList<>();
    public WattnDeck deck;



    //logging for testing purposes
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());
@Override
    public Player addPlayer(Connection connection, String playerName) {
        Player player = new Player(connection, playerName);
        players.add(player);
        return player;
    }
    public WattnGame(){resetWattnDeck();}
    public void resetVotingFinished() {
        for (Player player : players) {
            player.setVotingFinished(false);
        }
    }

    public void startGameWattn(){
    LOG.info("here");
        new Thread(()->{
            sendGameStartedMessageToClients();
            resetRoundFinished();
            sendHandCards();
            setRoundStartPlayer(players.get(0));
            notifyPlayerYourTurn(players.get(0));
        }).start();
    }

    @Override
    public void resetSchnopsnDeck(){}
    //2 players for beginning
    public void resetWattnDeck(){
        deck = new WattnDeck(GameName.Wattn,2);
        //setting hit & trump randomly 4 testing because UI isn't connected to game logic via live data yet
     //   deck.setHit(CardValue.randomValue());
       // deck.setTrump(Symbol.randomSymbol());
        // deck.setHit(CardValue.ZEHN);
        //deck.setTrump(Symbol.HERZ);

        //deck.rightCard = deck.getRightCard();
        //  deck.setTrump(Symbol.randomSymbol());
       // LOG.info("hit is "+ deck.getHit());
        //LOG.info("trump is "+deck.getTrump());
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

     //   Player winningPlayer = getRoundStartPlayer();
        Player winningPlayer = players.get(0);
        LOG.info(winningPlayer.toString());
        LOG.info(players.get(0).toString());
        Player currentPlayer = getNextPlayer(winningPlayer);

        while (currentPlayer != winningPlayer) {
            //the player that plays the right card always wins the subround
            // first played card is right card
            LOG.info(winningPlayer.getCardPlayed().getCardValue().toString());
            LOG.info(currentPlayer.getCardPlayed().getCardValue().toString());
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
            if(winningPlayer.getPoints() == 3){
                LOG.info(winningPlayer + " won this game!");

                sendEndRoundMessageToPlayers(players.get(0));
                return winningPlayer;
            }
            currentPlayer = getNextPlayer(currentPlayer);

        }

//       Log.e("22", winningPlayer + " won this round");
        return winningPlayer;
    }
    @Override
    public void sendHandCards() {
        for (Player player: players) {
            ArrayList<Card> handCards = deck.getHandCards();
            player.setHandcards(handCards);

            // send message to client with handcards
            Responses.SendHandCards request = new Responses.SendHandCards();
            request.cards = handCards;
            request.playerID = player.getClientConnection().getID();
            player.getClientConnection().sendTCP(request);
            if(player.getId() == 1){
                player.getClientConnection().sendTCP(new Responses.NotifyPlayerToSetSchlag());
            }else if(player.getId() == 2){
                player.getClientConnection().sendTCP(new Responses.NotifyPlayerToSetTrump());
            }
        }
    }
    @Override
    public void setCard(int playerID, Card card) {
       // Player player = getPlayerByID(playerID);
        Player player = players.get(playerID-1);

        //only for testing
        //Card card2 = player.getHandcards().get(0);

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

                //TODO: check if one player have enough points
                startNewRoundWattn(roundWonPlayer);
            } else {
                // Nächsten Spieler benachrichtigen dass er dran ist
                LOG.info("notify next player: " + getNextPlayer(player).getId());
                notifyPlayerYourTurn(getNextPlayer(player));
            }
        }).start();
    }

@Override
    public void addPointsToWinnerPlayer(Player winnerPlayer) {

            winnerPlayer.addPoints(1);
         //   LOG.info("Points added to player: " + winnerPlayer.getId() + ". Points: " + 1);

        sendPointsToWinnerPlayer(winnerPlayer);
    }
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
    public void startNewRoundWattn(Player startPlayer) {
        new Thread(()->{
            if (startPlayer.getPoints() >= 3 ) {
                // if the player gets at least 66 points then the player wins
                // TODO: test if player can win on other places? wenn 20 oder 40 angesagt wird?
                sendEndRoundMessageToPlayers(startPlayer);

            } else {
                resetRoundFinished();
                resetPlayedCard();

                setRoundStartPlayer(startPlayer);
                notifyPlayerYourTurn(startPlayer);
            }
        }).start();
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
