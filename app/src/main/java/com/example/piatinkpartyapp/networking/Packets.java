package com.example.piatinkpartyapp.networking;

// will be used as some kind of placeholder for a generic packet send method!
interface IPackets{
}


/*
NOTES :
    If you create a constructor, which also sets the variables,
    then we also need an  empty constructor, else Kryonet throws an Exception !!!!
 */
class Packets {

    public static class Requests{
        public static class SendEndToEndChatMessage implements IPackets {
            String message;
            int from;
            int to;

            public SendEndToEndChatMessage() {
            }

            public SendEndToEndChatMessage(String message, int from, int to) {
                this.message = message;
                this.from = from;
                this.to = to;
            }
        }

        public static class SendToAllChatMessage {
            String message;
            int from;

            public SendToAllChatMessage() {
            }

            public SendToAllChatMessage(String message, int from) {
                this.message = message;
                this.from = from;
            }
        }
    }

    public static class Responses {
        public static class ConnectedSuccessfully {
            int playerID;
            boolean isConnected;

            public ConnectedSuccessfully() { }

            public ConnectedSuccessfully(int playerID, boolean isConnected) {
                this.playerID = playerID;
                this.isConnected = isConnected;
            }
        }

        public static class ReceiveEndToEndChatMessage implements IPackets {
            String message;
            int from;
            int to;

            public ReceiveEndToEndChatMessage() {
            }

            public ReceiveEndToEndChatMessage(String message, int from, int to) {
                this.message = message;
                this.from = from;
                this.to = to;
            }
        }
        public static class ReceiveToAllChatMessage implements IPackets {
            String message;
            int from;

            public ReceiveToAllChatMessage() {
            }

            public ReceiveToAllChatMessage(String message, int from) {
                this.message = message;
                this.from = from;
            }
        }
    }

}