package io.mk8bk;

public interface TransactionAuthorisationSystem {
    void registerUser(PointOfSale pos) throws PosAlreadyRegisteredException;
    void connect(PointOfSale pos) throws UnregisteredPosClientException, PosAlreadyConnectedException;
    void disconnect(PointOfSale pos) throws NoSuchPosConnectionException, UnregisteredPosClientException;
    void pay(PointOfSale pos, String cardNumber, String pin, int amountInCentimes) throws NoSuchCreditCardException, InvalidPinException, InsufficientBalanceException, UnregisteredPosClientException, NoSuchPosConnectionException;


    class UnregisteredPosClientException extends Exception {
        public final String posIdentifier;
        public UnregisteredPosClientException(String posIdentifier){
            super("Unregister point of sale `"+posIdentifier+"`.");
            this.posIdentifier = posIdentifier;
        }
    }

    class PosAlreadyRegisteredException extends Exception {
        public final String posIdentifier;
        public PosAlreadyRegisteredException(String posIdentifier){
            super("A point of sale with identifier `"+posIdentifier+"` has already been registered.");
            this.posIdentifier = posIdentifier;
        }
    }

    class PosAlreadyConnectedException extends Exception {
        public final String posIdentifier;
        public PosAlreadyConnectedException(String posIdentifier){
            super("A point of sale with identifier `"+posIdentifier+"` is already connected.");
            this.posIdentifier = posIdentifier;
        }
    }

    class NoSuchPosConnectionException extends Exception {
        public final String posIdentifier;
        public NoSuchPosConnectionException(String posIdentifier){
            super("No connection to `"+posIdentifier+"` was established.");
            this.posIdentifier = posIdentifier;
        }
    }

    class NoSuchCreditCardException extends Exception {
        public final String cardNumber;

        public NoSuchCreditCardException(String cardNumber) {
            super("Unregistered card with identifier `"+cardNumber+"`; aborting.");
            this.cardNumber = cardNumber;
        }
    }

    class InvalidPinException extends Exception {
        public final String cardNumber;
        public InvalidPinException(String cardNumber) {
            super("Invalid pin entered for card with identifier `"+cardNumber+"`; aborting.");
            this.cardNumber = cardNumber;
        }
    }

    class InsufficientBalanceException extends Exception {
        public final String cardNumber;

        public InsufficientBalanceException(String cardNumber) {
            super("Insufficient balance for operation for the card with identifier `"+cardNumber+"`; aborting.");
            this.cardNumber = cardNumber;
        }
    }
}
