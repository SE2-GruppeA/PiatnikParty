package com.example.piatinkpartyapp.networking;


/*
NOTES :
    If you create a constructor, which also sets the variables,
    then we also need an  empty constructor, else Kryonet throws an Exception !!!
 */
class Packets {

    public static class Requests{

    }

    public static class Response {
        public static class ConnectedSuccessfully {
            int playerID;
            boolean isConnected;

            public ConnectedSuccessfully() { }

            public ConnectedSuccessfully(int playerID, boolean isConnected) {
                this.playerID = playerID;
                this.isConnected = isConnected;
            }
        }
    }

}