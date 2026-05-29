package io.mk8bk;

import java.util.Arrays;
import java.util.Scanner;

public class SuperMarketCheckoutSystem {
    static final UserBase userBase = new UserBase();
    static final UserSession userSession = new UserSession(userBase);
    static final CustomerBase customerBase = new CustomerBase();
    static final Inventory inventory = new Inventory();
    static final CategoryRepository categories = new CategoryRepository();

    public static void main(String[] args) {
        try {
            userBase.registerManager("ceo", "123456789");
        } catch (UserBase.UserAlreadyRegisteredException e) {
            // dead branch
            throw new RuntimeException(e);
        }
        handleCLI();
    }

    public static void handleCLI() {
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("====================================================");
        System.out.println(" Supermarket Cash Register 2026 ");
        System.out.println("====================================================");
        printHelp();
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine().trim();
            if (input.isEmpty())
                continue;
            if (input.equalsIgnoreCase("quit"))
                break;
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
            System.out.println("Command `" + command + "` is invalid, type `help` for available commands.");
            return;
        }
        if (userSession.isManagerLoggedIn()) {
            if (!CommandUtils.isManagerCompatible(command)) {
                System.out.println("Command `" + command + "` is invalid for the current logged in user (manager).");
                return;
            }
        } else if (userSession.isCashierLoggedIn()) {
            if (!CommandUtils.isCashierCompatible(command)) {
                System.out.println("Command `" + command + "` is invalid for the current logged in user (cashier).");
                return;
            }
        } else { // no user logged in
            if (!CommandUtils.isLoggedOutCompatible(command)) {
                System.out.println("Command `" + command + "` is invalid for the current user (not logged in).");
                return;
            }
        }
        if ("login".equals(command)) {
            if (arguments.length != 2) {
                System.out.println("Command `login` takes exactly two arguments: <username> <password>");
                return;
            }
            handleLogin(arguments[0], arguments[1]);
        } else if ("logout".equals(command)) {
            if (arguments.length != 0) {
                System.out.println("Command `logout` takes no arguments");
                return;
            }
            handleLogout();
        } else if ("registerCashier".equals(command)) {
            if (userSession.getLoggedInUser().getClass() != Manager.class) {
                System.out.println("Only a Manager can register a new cashier.");
                return;
            }
            if (arguments.length != 4) {
                System.out.println(
                        "Command `registerCashier` takes exactly four arguments: <firstname> <lastname> <username> <password>"
                );
                return;
            }
            handleRegisterCashierCommand(arguments[0], arguments[1], arguments[2], arguments[3]);
        } else if ("addItem".equals(command)) {
            if (arguments.length != 5) {
                System.out.println(
                        "Command `addItem` takes exactly five arguments: <itemName> <categoryName> <unitPrice> <weight> <initialStock>"
                );
                return;
            }
            try {
                int initialStock = Integer.parseInt(arguments[4]);
                int weight = Integer.parseInt(arguments[3]);
                int unitPrice = Integer.parseInt(arguments[2]);
                handleAddItem(arguments[0], arguments[1], unitPrice, weight, initialStock);
            } catch (NumberFormatException e) {
                System.out.println("Arguments <unitPrice> <weight> <initialStock> should be integers (centimes).");
            }

        } else if ("showInventory".equals(command)) {
            if (arguments.length != 0) {
                System.out.println("Command `showInventory` takes no arguments");
                return;
            }
            handleShowInventory();
        } else if ("restock".equals(command)) {
            if (arguments.length != 2) {
                System.out.println("Command `restock` takes exactly two arguments: <itemName> <quantity>");
                return;
            }
            try {
                int quantity = Integer.parseInt(arguments[1]);
                handleRestock(arguments[0], quantity);
            } catch (NumberFormatException e) {
                System.out.println("Argument <quantity> should be an integer.");
            }
        } else if ("registerCustomer".equals(command)) {
            if (arguments.length != 5) {
                System.out.println(
                        "Command `registerCustomer` takes exactly five arguments: <firstname> <lastname> <username> <address> <password>"
                );
                return;
            }
            handleRegisterCustomerCommand(arguments[0], arguments[1], arguments[2], arguments[3], arguments[4]);
        } else if ("setup".equals(command)) {
            if (arguments.length != 0) {
                System.out.println(
                        "Command `setup` takes no arguments."
                );
            }
        }
    }

    private static void handleRestock(String itemName, int quantity) {
        try {
            inventory.restock(itemName, quantity);
        } catch (Inventory.NoSuchItemException e) {
            System.out.println("Can't restock item `" + itemName + "`, not registered.");
        } catch (Inventory.NegativeRestockingQuantity e) {
            System.out.println("Cannot restock with negative value: " + quantity + ".");
        }
    }

    private static void handleShowInventory() {
        System.out.println("Inventory");
        for (String itemName : inventory.getAllItemNames()) {
            try {
                System.out.println("\t\t" + inventory.getItem(itemName) + "\t\t" + inventory.getItemStock(itemName));
            } catch (Inventory.NoSuchItemException e) {
                // dead branch
            }
        }
    }

    private static void handleAddItem(String itemName, String categoryName, int unitPrice, int weight, int initialStock) {
        if (inventory.hasItem(itemName)) {
            System.out.println("Item `" + itemName + "` is already registered in the inventory.");
            return;
        }
        try {
            if (!categories.hasCategory(categoryName)) {
                categories.addCategory(categoryName);
            }
            ItemCategory category = categories.getCategory(categoryName);
            Item i = new Item(itemName, category, unitPrice, weight);
            inventory.addItem(i, initialStock);
            System.out.println("Item `" + i.getName() + "` added to inventory.");
        } catch (CategoryRepository.NoSuchCategoryException | CategoryRepository.CategoryAlreadyRegisteredException |
                 Inventory.ItemAlreadyPresentException e) {
            // dead branches
        }
    }

    private static void handleRegisterCashierCommand(String firstname, String lastname, String username, String password) {
        try {
            userBase.registerCashier(firstname, lastname, username, password);
            System.out.println("Cashier `" + username + "` successfully registered.");
        } catch (UserBase.UserAlreadyRegisteredException e) {
            System.out.println("A user with the username `" + username + "` is already registered.");
        }
    }

    private static void handleRegisterCustomerCommand(String firstname, String lastname, String username, String address, String password) {
        try {
            customerBase.registerCustomer(firstname, lastname, username, address, password);
            System.out.println("Customer `" + username + "` registered successfully");
        } catch (CustomerBase.CustomerAlreadyRegisteredException e) {
            System.out.println("Customer with username `" + username + "` is already registered.");
        }
    }


    private static void handleLogout() {
        try {
            userSession.logout();
            System.out.println("Logout successfull.");
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
                System.out.println("Cashier `" + username + "` logged in successfully.");
            } else if (u instanceof Manager m) {
                System.out.println("Manager `" + username + "` logged in successfully.");
            } else {
                // dead branch, lookup sealed classes
                // TODO: lookup more details on sealed classes to assert correct behavior
                throw new RuntimeException("Unknown user type.");
            }
            // dead branches below, login was approved before
        } catch (UserSession.UserAlreadyLoggedInException e) {
            System.out.println("A user is already logged in (" + userSession.getLoggedInUser().getUsername() + "). Logout first.");
        } catch (UserBase.NoSuchUserException e) {
            System.out.println("No user with username `" + username + "` is registered. Register first.");
        } catch (UserSession.InvalidPasswordException e) {
            System.out.println("Invalid password for user `" + username + "`. Try again.");
        }
    }


    static void printHelp() {
        System.out.println("\n\t\tCommands");
        System.out.println("\tlogin <username> <password>"); // done
        System.out.println("\tlogout"); // done
        System.out.println("\tsetup");
        System.out.println("\tregisterCashier <firstname> <lastname> <username> <password>"); // done
        System.out.println("\tregisterCustomer <firstname> <lastname> <username> <address> <password>"); // done
        System.out.println("\taddItem <itemName> <categoryName> <unitPrice> <weight> <initialStock>"); // done
        System.out.println("\trestock <itemName> <quantity>"); // done
        System.out.println("\tsetCategoryDiscount <categoryName> <discountPercent>");
        System.out.println("\tsubscribeToPlan <planName>");
        System.out.println("\tstartCheckout <customerUsername>");
        System.out.println("\tscanItem <itemName> <quantity>");
        System.out.println("\tcomputeBill");
        System.out.println("\trequestDelivery <address>");
        System.out.println("\tpay <cardNumber> <pin>");
        System.out.println("\tsimulatePayment <outcome>");
        System.out.println("\tshowInventory"); // done
        System.out.println("\tshowRevenue");
        System.out.println("\trunTest <testScenario-file>");
        System.out.println("\thelp"); // done
        System.out.println("\tquit"); // done
    }

}
