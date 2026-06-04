package io.mk8bk;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SuperMarketCheckoutSystem {
    // a user is a cashier or a manager
    private static final UserBase userBase = new UserBase();
    // handles the current user session
    private static final UserSession userSession = new UserSession(userBase);
    // keeps track of all the registered customers
    private static final CustomerBase customerBase = new CustomerBase();
    // keeps track of all registered categories
    private static final CategoryBase categories = new CategoryBase();
    // keeps track of the items in the supermarket inventory
    private static final Inventory inventory = new Inventory();
    private static final TransactionAuthorisationSystem tas =
            new SampleBankTas();
    private static final PointOfSale pos = new PointOfSale(tas);
    private static boolean setupPerformed = false;
    private static long totalRevenueInCentimes = 0;

    // null when not currently serving a customer
    private static Checkout checkout = null;

    public static void main(String[] args) {
        try {
            userBase.registerManager("ceo", "123456789");
        } catch (UserBase.UserAlreadyRegisteredException e) {
            // dead branch
            throw new RuntimeException(e);
        }

        Scanner scanner = new Scanner(System.in);
        handleCLI(scanner, true);
    }

    private static void handleCLI(Scanner scanner, boolean interactive) {
        String input;
        if (interactive) {
            System.out.println(
                    "====================================================");
            System.out.println(" Supermarket Cash Register 2026 ");
            System.out.println(
                    "====================================================");
            printHelp();
        }
        while (true) {
            if (interactive) System.out.print("> ");
            input = scanner.nextLine().trim();
            if (!interactive) System.out.println("> " + input);
            if (input.isEmpty()) continue;
            if (input.equalsIgnoreCase("quit")) break;
            if (input.equalsIgnoreCase("help")) {
                printHelp();
                continue;
            }
            String[] parts = input.split("\\s+");
            String command = parts[0];
            String[] arguments = Arrays.copyOfRange(parts, 1, parts.length);
            handleCommand(command, arguments);
            System.out.print("\n");
        }
        scanner.close();
    }

    private static void handleCommand(String command, String[] arguments) {
        if (CommandUtils.isInvalidCommand(command)) {
            System.out.println("Command `" + command + "` is invalid, type " + "`help` for available commands.");
            return;
        }
        if (userSession.isManagerLoggedIn()) {
            if (!CommandUtils.isManagerCompatible(command)) {
                System.out.println("Command `" + command + "` is invalid for "
                        + "the current logged in user (manager).");
                return;
            }
        } else if (userSession.isCashierLoggedIn()) {
            if (!CommandUtils.isCashierCompatible(command)) {
                System.out.println("Command `" + command + "` is invalid for "
                        + "the current logged in user (cashier).");
                return;
            }
        } else { // no user logged in
            if (!CommandUtils.isLoggedOutCompatible(command)) {
                System.out.println("Command `" + command + "` is invalid for "
                        + "the current user (not logged in).");
                return;
            }
        }
        if (!CommandUtils.verifyCommandArguments(command, arguments)) {
            return;
        }
        if ("login".equals(command)) {
            handleLogin(arguments[0], arguments[1]);
        } else if ("logout".equals(command)) {
            handleLogout();
        } else if ("registerCashier".equals(command)) {
            if (userSession.getLoggedInUser().getClass() != Manager.class) {
                System.out.println("Only a Manager can register a new " +
                        "cashier" + ".");
                return;
            }
            handleRegisterCashierCommand(arguments[0], arguments[1],
                    arguments[2], arguments[3]);
        } else if ("addItem".equals(command)) {
            int initialStock = Integer.parseInt(arguments[4]);
            int weight = Integer.parseInt(arguments[3]);
            int unitPrice = Integer.parseInt(arguments[2]);
            handleAddItem(arguments[0], arguments[1], unitPrice, weight,
                    initialStock);
        } else if ("scanItem".equals(command)) {
            handleScanItem(arguments[0], arguments[1]);
        } else if ("startCheckout".equals(command)) {
            handleStartCheckout(arguments[0]);
        } else if ("showInventory".equals(command)) {
            handleShowInventory();
        } else if ("restock".equals(command)) {
            int quantity = Integer.parseInt(arguments[1]);
            handleRestock(arguments[0], quantity);
        } else if ("registerCustomer".equals(command)) {
            handleRegisterCustomerCommand(arguments[0], arguments[1],
                    arguments[2], arguments[3], arguments[4]);
        } else if ("setup".equals(command)) {
            handleSetup();
        } else if ("setCategoryDiscount".equals(command)) {
            int discountPercent = Integer.parseInt(arguments[1]);
            handleSetCategoryDiscount(arguments[0], discountPercent);
        } else if ("pay".equals(command)) {
            handlePay(arguments[0], arguments[1]);
        } else if ("computeBill".equals(command)) {

            handleComputeBill();
        } else if ("subscribeToPlan".equals(command)) {
            handleSubscribeToPlan(arguments[0]);
        } else if ("runTest".equals(command)) {
            handleRunTest(arguments[0]);
        } else if ("showRevenue".equals(command)) {
            System.out.println(String.format("Total revenue is: %.2f EUR",
                    totalRevenueInCentimes / 100.0));
        }
    }

    private static void handleRunTest(String filePath) {
        File f = new File(filePath);
        try {
            Scanner scanner = new Scanner(f);
            handleCLI(scanner, false);
        } catch (FileNotFoundException e) {
            System.out.println("File `" + filePath + "` not found.");
            return;
        } catch (NoSuchElementException e) {
            return;
        }
    }

    private static void handleSubscribeToPlan(String planName) {
        if (checkout == null) {
            System.out.println("No checkout. StartCheckout first.");
            return;
        }
        if ("NORMAL".equals(planName)) {
            checkout.subscribeToPlan(new NormalDiscountPlan());
        } else if ("PRIME".equals(planName)) {
            checkout.subscribeToPlan(new PrimeDiscountPlan());
        } else if ("PLATINUM".equals(planName)) {
            checkout.subscribeToPlan(new PlatinumDiscountPlan());
        } else {
            System.out.println("`" + planName + "` is not a valid plan name " + "{NORMAL, PRIME, PLATINUM}.");
            return;
        }
        System.out.println("Customer `" + checkout.getCustomer().username +
                "` subscribed to " + planName + " plan. Fee added and " +
                "awaiting payment.");
    }

    private static void handleComputeBill() {
        if (checkout == null) {
            System.out.println("No checkout. StartCheckout first.");
            return;
        }
        String bill = checkout.computeBill();
        System.out.println(bill);
    }

    private static void handlePay(String cardNumber, String pin) {
        if (checkout == null) {
            System.out.println("No checkout. StartCheckout first.");
            return;
        }
        if (!checkout.isBillComputed()) {
            System.out.println("Can't pay before computing bill.");
            return;
        }
        int amountInCentimes = checkout.getTotalInCentimes();
        try {
            pos.pay(cardNumber, pin, amountInCentimes);
            checkout.decrementInventory(inventory);
            totalRevenueInCentimes += amountInCentimes;
            System.out.println("Payment successfull.");
        } catch (TransactionAuthorisationSystem.NoSuchCreditCardException e) {
            System.out.println("No such credit card registered with a bank " + "authority; aborting.");
        } catch (
                TransactionAuthorisationSystem.InsufficientBalanceException e) {
            System.out.println("Insufficient bank account balance; aborting " + "payment.");
        } catch (TransactionAuthorisationSystem.InvalidPinException e) {
            System.out.println("Invalid card pin; aborting payment.");
        }
    }


    private static void handleSetCategoryDiscount(String categoryName,
                                                  int discountPercent) {
        try {
            categories.setCategoryDiscount(categoryName, discountPercent);
            System.out.println("Category `" + categoryName + "` has been " +
                    "discounted by " + discountPercent + "%.");
        } catch (CategoryBase.NoSuchCategoryException e) {
            System.out.println("No category `" + categoryName + "` registered"
                    + ". addItem with category to register said category.");
        } catch (CategoryBase.InvalidCategoryDiscountPercent e) {
            System.out.println("Argument <discountPercent> has to be in the " + "range [0, 100].");
        }
    }

    private static void handleScanItem(String itemName, String quantity) {
        if (checkout == null) {
            System.out.println("No checkout start, can't scan item.");
            return;
        }
        try {
            // no error since sanitized in CommandUtils.verifyCommandArguments
            int q = Integer.parseInt(quantity);
            Item i = inventory.getItem(itemName);
            int totalQuantity = inventory.getItemStock(itemName);
            if (totalQuantity < q) {
                System.out.println("Not enough inventory.");
                return;
            }
            checkout.scanItem(i, q);
            System.out.println(String.format("Scanned: %-30s quantity: %-6d " +
                    "price: %-6.2f EUR", itemName, q,
                    q * i.getUnitPriceInCentimes() / 100.0));
            String categoryName = i.getItemCategory().categoryName();
            int discount = categories.getCategoryDiscount(categoryName);
            if (0 != discount) {
                System.out.println(String.format("%-30s items have a %d%% " +
                        "discount applied: -%-6.2f EUR", categoryName,
                        discount,
                        i.getUnitPriceInCentimes() * discount * q / 10000.0f));
            }
            System.out.flush();
        } catch (Inventory.NoSuchItemException e) {
            System.out.println("No such item `" + itemName + "` in inventory.");
        } catch (CategoryBase.NoSuchCategoryException e) {
            // catastrophic error, inconsistent state
            throw new RuntimeException(e);
        }
    }

    private static void handleSetup() {
        if (setupPerformed) {
            System.out.println("Setup already performed.");
            return;
        }
        try {
            categories.addCategory("FRUIT_AND_VEGETABLES");
            categories.addCategory("DAIRY");
            categories.addCategory("MEAT");
            userBase.registerCashier("Jafar", "Aladin", "jafar", "jasmine");
            customerBase.registerCustomer("Normie", "McNormie", "noob",
                    "springfield", "12345678");
            SampleBankTas sampleBankTas = (SampleBankTas) tas;
            sampleBankTas.registerCard("1234567812345678", "1234", 500000);
            sampleBankTas.registerCard("9876543223456789", "4321", 20000);
            sampleBankTas.registerCard("1234678998764321", "6789", 3000);
            setupPerformed = true;
            // load items maybe idk
        } catch (CategoryBase.CategoryAlreadyRegisteredException |
                 UserBase.UserAlreadyRegisteredException |
                 CustomerBase.CustomerAlreadyRegisteredException |
                 SampleBankTas.CardAlreadyRegisteredException e) {
            System.out.println("Failed setup:\n" + e.getMessage());
        }
        System.out.println("Setup successfull.");
    }

    private static void handleStartCheckout(String customerName) {
        if (checkout != null) {
            System.out.println("A checkout is ongoing. Can't start another " + "one.");
            return;
        }
        try {
            Customer c = customerBase.getCustomer(customerName);
            checkout = new Checkout(c, categories);
            System.out.println("Checkout started for customer `" + c.firstname + "`.");
        } catch (CustomerBase.NoSuchCustomerException e) {
            System.out.println("No such customer `" + customerName + "` " +
                    "currently registered.");
        }
    }

    private static void handleRestock(String itemName, int quantity) {
        try {
            inventory.restock(itemName, quantity);
            System.out.println("Item `" + itemName + "` restocked; current " + "quantity: " + inventory.getItemStock(itemName));
        } catch (Inventory.NoSuchItemException e) {
            System.out.println("Can't restock item `" + itemName + "`, not " + "registered.");
        } catch (Inventory.NegativeRestockingQuantity e) {
            System.out.println("Cannot restock with negative value: " + quantity + ".");
        }
    }

    private static void handleShowInventory() {
        System.out.println("Inventory");
        for (String itemName : inventory.getAllItemNames()) {
            try {
                System.out.println("\t\t" + inventory.getItem(itemName) + "\t"
                        + "\t" + inventory.getItemStock(itemName));
            } catch (Inventory.NoSuchItemException e) {
                // dead branch
            }
        }
    }

    private static void handleAddItem(String itemName, String categoryName,
                                      int unitPrice, int weight,
                                      int initialStock) {
        if (inventory.hasItem(itemName)) {
            System.out.println("Item `" + itemName + "` is already " +
                    "registered" + " in the inventory.");
            return;
        }
        try {
            if (!categories.hasCategory(categoryName))
                categories.addCategory(categoryName);
            ItemCategory category = categories.getCategory(categoryName);
            Item i = new Item(itemName, category, unitPrice, weight);
            inventory.addItem(i, initialStock);
            System.out.println("Item `" + i.getName() + "` added to " +
                    "inventory" + ".");
        } catch (CategoryBase.NoSuchCategoryException |
                 CategoryBase.CategoryAlreadyRegisteredException |
                 Inventory.ItemAlreadyPresentException e) {
            // dead branches
        }
    }

    private static void handleRegisterCashierCommand(String firstname,
                                                     String lastname,
                                                     String username,
                                                     String password) {
        try {
            userBase.registerCashier(firstname, lastname, username, password);
            System.out.println("Cashier `" + username + "` successfully " +
                    "registered.");
        } catch (UserBase.UserAlreadyRegisteredException e) {
            System.out.println("A user with the username `" + username + "` " + "is already registered.");
        }
    }

    private static void handleRegisterCustomerCommand(String firstname,
                                                      String lastname,
                                                      String username,
                                                      String address,
                                                      String password) {
        try {
            customerBase.registerCustomer(firstname, lastname, username,
                    address, password);
            System.out.println("Customer `" + username + "` registered " +
                    "successfully");
        } catch (CustomerBase.CustomerAlreadyRegisteredException e) {
            System.out.println("Customer with username `" + username + "` is "
                    + "already registered.");
        }
    }


    private static void handleLogout() {
        try {
            userSession.logout();
            checkout = null;
            System.out.println("Logout successful.");
        } catch (UserSession.NoUserLoggedInException e) {
            // dead branch but eh
            System.out.println("No user logged in, can't logout.");
        }
    }

    private static void handleLogin(String username, String password) {
        try {
            userSession.login(username, password);
            User u = userSession.getLoggedInUser();
            if (u instanceof Cashier c) {
                System.out.println("Cashier `" + username + "` logged in " +
                        "successfully.");
            } else if (u instanceof Manager m) {
                System.out.println("Manager `" + username + "` logged in " +
                        "successfully.");
            } else {
                // dead branch, lookup sealed classes
                // TODO: lookup more details on sealed classes to assert
                //  correct behavior
                throw new RuntimeException("Unknown user type.");
            }
            // dead branches below, login was approved before
        } catch (UserSession.UserAlreadyLoggedInException e) {
            System.out.println("A user is already logged in (" + userSession.getLoggedInUser().getUsername() + "). Logout first.");
        } catch (UserBase.NoSuchUserException e) {
            System.out.println("No user with username `" + username + "` is " + "registered. Register first.");
        } catch (UserSession.InvalidPasswordException e) {
            System.out.println("Invalid password for user `" + username + "`" +
                    "." + " Try again.");
        }
    }


    private static void printHelp() {
        System.out.println("\n\t\tCommands");
        System.out.println("\tlogin <username> <password>"); // done and tested
        System.out.println("\tlogout"); // done and tested
        System.out.println("\tsetup"); // done
        System.out.println("\tregisterCashier <firstname> <lastname> " +
                "<username> <password>"); // done and tested
        System.out.println("\tregisterCustomer <firstname> <lastname> " +
                "<username> <address> <password>"); // done and tested
        System.out.println("\taddItem <itemName> <categoryName> <unitPrice> " + "<weight> <initialStock>"); // done and tested
        System.out.println("\trestock <itemName> <quantity>"); // done and
        // tested
        System.out.println("\tsetCategoryDiscount <categoryName> " +
                "<discountPercent>"); // done and tested
        System.out.println("\tsubscribeToPlan <planName>"); // done and tested
        System.out.println("\tstartCheckout <customerUsername>"); // done
        System.out.println("\tscanItem <itemName> <quantity>"); // done
        System.out.println("\tcomputeBill"); // done
        System.out.println("\trequestDelivery <address>");
        System.out.println("\tpay <cardNumber> <pin>"); // done
        System.out.println("\tsimulatePayment <outcome>");
        System.out.println("\tshowInventory"); // done
        System.out.println("\tshowRevenue");
        System.out.println("\trunTest <testScenario-file>"); // done
        System.out.println("\thelp"); // done
        System.out.println("\tquit"); // done
        System.out.println("\t all prices are to be entered in centimes, all " +
                "weights in grams");
    }

}
