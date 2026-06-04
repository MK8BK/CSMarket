package io.mk8bk;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomAddressBookService implements AddressBookService {
    private final Map<String, Integer> addressesToDistanceKm;
    public RandomAddressBookService(){
        addressesToDistanceKm = new HashMap<>();
    }

    @Override
    public int getDistance(String address) {
        if(addressesToDistanceKm.containsKey(address))
            return addressesToDistanceKm.get(address);
        Random r = new Random();
        int d = r.nextInt(71);
        addressesToDistanceKm.put(address, d);
        return d;
    }
}
