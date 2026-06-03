package io.mk8bk;

import java.util.UUID;

public class PointOfSale {
    public final String id;
    private final TransactionAuthorisationSystem tas;

    public PointOfSale(TransactionAuthorisationSystem tas) {
        id = UUID.randomUUID().toString();
        // setup the TAS
        this.tas = tas;
        try {
            tas.registerUser(this);
        } catch (TransactionAuthorisationSystem.PosAlreadyRegisteredException e) {
            // dead branch (hopefully)
        }
    }
    public void pay(String cardNumber, String pin) throws TransactionAuthorisationSystem.NoSuchCreditCardException, TransactionAuthorisationSystem.InsufficientBalanceException, TransactionAuthorisationSystem.InvalidPinException {
        try {
            tas.connect(this);
            tas.pay(this, cardNumber, pin);
            tas.disconnect(this);
        } catch (TransactionAuthorisationSystem.InsufficientBalanceException | TransactionAuthorisationSystem.InvalidPinException | TransactionAuthorisationSystem.NoSuchCreditCardException e) {
            throw e;
        } catch (TransactionAuthorisationSystem.UnregisteredPosClientException | TransactionAuthorisationSystem.NoSuchPosConnectionException e) {
            // catastrophic error, do not catch and let the system collapse into the void that awaits it
            throw new RuntimeException(e);
        }
    }

}
