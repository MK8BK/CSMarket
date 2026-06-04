package io.mk8bk;

import java.lang.invoke.LambdaMetafactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class SampleBankTas implements TransactionAuthorisationSystem {
    private final Map<String, PointOfSale> registeredUsers;
    private final Map<String, PointOfSale> connectedUsers;

    // bank card number : balance in centimes
    private final Map<String, Integer> cardBalances;
    // bank card number : pin
    private final Map<String, String> cardPins;

    public SampleBankTas() {
        registeredUsers = new HashMap<>();
        cardPins = new HashMap<>();
        connectedUsers = new HashMap<>();
        cardBalances = new HashMap<>();
    }

    private static final String numbers = "0123456789";
    public static String generateCardNumber(){
        Random r = new Random();
        StringBuilder s = new StringBuilder();
        for(int i=0; i<16; ++i)
            s.append(numbers.charAt(r.nextInt(10)));
        return s.toString();
    }

    public void registerCard(String cardNumber, String pin,
                             int initialBalance) throws CardAlreadyRegisteredException {
        if (cardBalances.containsKey(cardNumber))
            throw new CardAlreadyRegisteredException(cardNumber);
        cardBalances.put(cardNumber, initialBalance);
        cardPins.put(cardNumber, pin);
    }

    @Override
    public void registerUser(PointOfSale pos) throws PosAlreadyRegisteredException {
        if (registeredUsers.containsKey(pos.id))
            throw new PosAlreadyRegisteredException(pos.id);
        registeredUsers.put(pos.id, pos);
    }

    @Override
    public void connect(PointOfSale pos) throws UnregisteredPosClientException,
                                                PosAlreadyConnectedException {
        if (!registeredUsers.containsKey(pos.id))
            throw new UnregisteredPosClientException(pos.id);
        if (connectedUsers.containsKey(pos.id))
            throw new PosAlreadyConnectedException(pos.id);
        connectedUsers.put(pos.id, pos);
    }

    @Override
    public void disconnect(PointOfSale pos) throws NoSuchPosConnectionException,
                                                   UnregisteredPosClientException {
        if (!registeredUsers.containsKey(pos.id))
            throw new UnregisteredPosClientException(pos.id);
        if (!connectedUsers.containsKey(pos.id))
            throw new NoSuchPosConnectionException(pos.id);
        connectedUsers.remove(pos.id);
    }

    @Override
    public void pay(PointOfSale pos, String cardNumber, String pin,
                    int amountInCentimes) throws NoSuchCreditCardException,
                                                 InvalidPinException,
                                                 InsufficientBalanceException
            , UnregisteredPosClientException, NoSuchPosConnectionException {
        if (!registeredUsers.containsKey(pos.id))
            throw new UnregisteredPosClientException(pos.id);
        if (!connectedUsers.containsKey(pos.id))
            throw new NoSuchPosConnectionException(pos.id);
        if (!cardBalances.containsKey(cardNumber))
            throw new NoSuchCreditCardException(cardNumber);
        if (!Objects.equals(pin, cardPins.get(cardNumber)))
            throw new InvalidPinException(cardNumber);
        if (cardBalances.get(cardNumber) < amountInCentimes)
            throw new InsufficientBalanceException(cardNumber);
        cardBalances.put(cardNumber, cardBalances.get(cardNumber)-amountInCentimes);
    }

    public static class CardAlreadyRegisteredException extends Throwable {
        public final String cardNumber;

        public CardAlreadyRegisteredException(String cardNumber) {
            super("A card with the number `" + cardNumber + "` is already " + "registered.");
            this.cardNumber = cardNumber;
        }
    }
}
