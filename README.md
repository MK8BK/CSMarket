# CSMarket

Software engineering project for sg8 CentraleSupelec

This project requires maven, a .jar is also provided. The entryPoint of the application is the SuperMarketCheckoutSystem class

Three sample categories are setup: `DAIRY`, `FRUIT_AND_VEGETABLES` and `MEAT`.
A manager with username `ceo` and password `123456789` is provided.
A default cashier `jafar` with password `jasmine` is also provided.
A default customer `noob` with password `12345678` is also setup.


There are three sample bank cards with the following information:

CARD_NUMBER:1234567812345678 | PIN:1234 | BALANCE: 500000 CENTIMES

CARD_NUMBER:9876543223456789 | PIN:4321 | BALANCE: 20000 CENTIMES

CARD_NUMBER:1234678998764321 | PIN:6789 | BALANCE: 3000 CENTIMES

There is a `comprehensiveTestFile.txt` under the `src/main/resources` directory.

All money quantities entered in need to be in `CENTIMES`. All weights need to be in `GRAMS`.

Three custmer discount plans are provided: `NORMAL`, `PRIME` and `PLATINUM`.
