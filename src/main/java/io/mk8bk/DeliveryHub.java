package io.mk8bk;

import java.util.HashMap;
import java.util.Map;

public class DeliveryHub {
    private final Map<String, String> dueDeliveryRequestsCustomerToAddress;
    private final Map<String, String> nextDeliveryRequestsCustomerToAddress;

    public DeliveryHub() {
        dueDeliveryRequestsCustomerToAddress = new HashMap<>();
        nextDeliveryRequestsCustomerToAddress = new HashMap<>();
    }

    public boolean deliveryIsDue(String customerName){
        return dueDeliveryRequestsCustomerToAddress.containsKey(customerName);
    }
    public String getAddress(String customerName){
        return dueDeliveryRequestsCustomerToAddress.get(customerName);
    }

    public void enqueueDelivery(String username, String address){
        nextDeliveryRequestsCustomerToAddress.put(username, address);
    }
    public void scheduleDelivery(String username){
        if(nextDeliveryRequestsCustomerToAddress.containsKey(username)){
            dueDeliveryRequestsCustomerToAddress.put(username, nextDeliveryRequestsCustomerToAddress.get(username));
            nextDeliveryRequestsCustomerToAddress.remove(username);
        }
    }

    public void deliver(String username) throws UndueDelivery {
        if(!dueDeliveryRequestsCustomerToAddress.containsKey(username))
            throw new UndueDelivery(username);
        String address = dueDeliveryRequestsCustomerToAddress.get(username);
        System.out.println("Delivery performed for customer `"+username+"` at address `"+address+"`.");
        dueDeliveryRequestsCustomerToAddress.remove(username);
    }

    public void cancelDelivery(String username) {
        dueDeliveryRequestsCustomerToAddress.remove(username);
    }

    public static class UndueDelivery extends Throwable {
        public final String customerName;

        public UndueDelivery(String customerName) {
            super("No such delivery queued.");
            this.customerName = customerName;
        }
    }
}
