package io.mk8bk;

import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

public class CommandUtils {
    private static final Map<String, CommandSessionRequirement> commandMap = Map.ofEntries(
            entry("login", CommandSessionRequirement.LOGGED_OUT),
            entry("logout", CommandSessionRequirement.CASHIER_OR_MANAGER),
            entry("setup", CommandSessionRequirement.MANAGER),
            entry("registerCashier", CommandSessionRequirement.MANAGER),
            entry("registerCustomer", CommandSessionRequirement.MANAGER),
            entry("addItem", CommandSessionRequirement.MANAGER),
            entry("restock", CommandSessionRequirement.MANAGER),
            entry("setCategoryDiscount", CommandSessionRequirement.MANAGER),
            entry("subscribeToPlan", CommandSessionRequirement.CASHIER),
            entry("startCheckout", CommandSessionRequirement.CASHIER),
            entry("scanItem", CommandSessionRequirement.CASHIER),
            entry("computeBill", CommandSessionRequirement.CASHIER),
            entry("requestDelivery", CommandSessionRequirement.CASHIER),
            entry("pay", CommandSessionRequirement.CASHIER),
            entry("simulatePayment", CommandSessionRequirement.CASHIER),
            entry("showInventory", CommandSessionRequirement.MANAGER),
            entry("showRevenue", CommandSessionRequirement.MANAGER),
            entry("runTest", CommandSessionRequirement.CASHIER_OR_MANAGER_OR_LOGGED_OUT),
            entry("help", CommandSessionRequirement.CASHIER_OR_MANAGER_OR_LOGGED_OUT),
            entry("quit", CommandSessionRequirement.CASHIER_OR_MANAGER_OR_LOGGED_OUT)
    );
    private static final Set<CommandSessionRequirement> managerCompatible = Set.of(
            CommandSessionRequirement.MANAGER,
            CommandSessionRequirement.CASHIER_OR_MANAGER,
            CommandSessionRequirement.CASHIER_OR_MANAGER_OR_LOGGED_OUT
    );
    private static final Set<CommandSessionRequirement> cashierCompatible = Set.of(
            CommandSessionRequirement.CASHIER,
            CommandSessionRequirement.CASHIER_OR_MANAGER,
            CommandSessionRequirement.CASHIER_OR_MANAGER_OR_LOGGED_OUT
    );
    private static final Set<CommandSessionRequirement> loggedOutCompatible = Set.of(
            CommandSessionRequirement.LOGGED_OUT,
            CommandSessionRequirement.CASHIER_OR_MANAGER_OR_LOGGED_OUT
    );

    public static boolean isInvalidCommand(String command) {
        return !commandMap.containsKey(command);
    }

    public static boolean isManagerCompatible(String command) {
        if (isInvalidCommand(command)) return false;
        return managerCompatible.contains(commandMap.get(command));
    }

    public static boolean isCashierCompatible(String command) {
        if (isInvalidCommand(command)) return false;
        return cashierCompatible.contains(commandMap.get(command));
    }

    public static boolean isLoggedOutCompatible(String command) {
        if (isInvalidCommand(command)) return false;
        return loggedOutCompatible.contains(commandMap.get(command));
    }

    public static boolean verifyCommandArguments(String command, String[] arguments) {
        if (isInvalidCommand(command)) {
            // dead branch but eh
            // assume the terminal already eliminated invalid commands
            throw new RuntimeException("Invalid Command");
        }
        if ("login".equals(command)) {
            if (arguments.length != 2) {
                System.out.println("Command `login` takes exactly two arguments: <username> <password>");
                return false;
            }
        } else if ("logout".equals(command)) {
            if (arguments.length != 0) {
                System.out.println("Command `logout` takes no arguments");
                return false;
            }
        } else if ("registerCashier".equals(command)) {
            if (arguments.length != 4) {
                System.out.println(
                        "Command `registerCashier` takes exactly four arguments: <firstname> <lastname> <username> <password>"
                );
                return false;
            }
        } else if ("addItem".equals(command)) {
            if (arguments.length != 5) {
                System.out.println(
                        "Command `addItem` takes exactly five arguments: <itemName> <categoryName> <unitPrice> <weight> <initialStock>"
                );
                return false;
            }
            try {
                int initialStock = Integer.parseInt(arguments[4]);
                int weight = Integer.parseInt(arguments[3]);
                int unitPrice = Integer.parseInt(arguments[2]);
                if (initialStock < 0 || weight < 0 || unitPrice < 0) {
                    System.out.println("Arguments <unitPrice> <weight> <initialStock> should be positive integers (centimes, grams, count)");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Arguments <unitPrice> <weight> <initialStock> should be integers (centimes, grams, count)");
                return false;
            }
        } else if ("scanItem".equals(command)) {
            if (arguments.length != 2) {
                System.out.println("Command `scanItem` takes exactly two arguments: <itemName> <quantity>"
                );
                return false;
            }
            try {
                int quantity = Integer.parseInt(arguments[1]);
                if (quantity < 0) {
                    System.out.println("Argument <quantity> should be a positive integer (count)");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Argument <quantity> should be an integer (count)");
                return false;
            }
        } else if ("startCheckout".equals(command)) {
            if (arguments.length != 1) {
                System.out.println("Command `startCheckout` takes exactly one argument: <customerName>");
                return false;
            }
        } else if ("showInventory".equals(command)) {
            if (arguments.length != 0) {
                System.out.println("Command `showInventory` takes no arguments");
                return false;
            }
        } else if ("restock".equals(command)) {
            if (arguments.length != 2) {
                System.out.println("Command `restock` takes exactly two arguments: <itemName> <quantity>");
                return false;
            }
            try {
                int quantity = Integer.parseInt(arguments[1]);
                if (quantity < 0) {
                    System.out.println("Argument <quantity> should be a positive integer (count)");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Argument <quantity> should be an integer (count)");
                return false;
            }
        } else if ("registerCustomer".equals(command)) {
            if (arguments.length != 5) {
                System.out.println(
                        "Command `registerCustomer` takes exactly five arguments: <firstname> <lastname> <username> <address> <password>"
                );
                return false;
            }
        } else if ("setup".equals(command)) {
            if (arguments.length != 0) {
                System.out.println("Command `setup` takes no arguments.");
                return false;
            }
        } else if ("setCategoryDiscount".equals(command)) {
            if (arguments.length != 2) {
                System.out.println("Command `setCategoryDiscount` takes exactly 2 arguments: <categoryName> <discountPercent>");
                return false;
            }
            try {
                int discountPercent = Integer.parseInt(arguments[1]);
            }catch (NumberFormatException e){
                System.out.println("Argument <discountPercent> has to be an integer.");
                return false;
            }
        } else if ("pay".equals(command)) {
            if(arguments.length != 2){
                System.out.println("Command `pay` takes exactly 2 arguments: <cardNumber> <pin>");
                return false;
            }
        } else if ("subscribeToPlan".equals(command)) {
            if(arguments.length != 1){
                System.out.println("Command `subscribeToPlan` takes exactly one argument: <planName>");
                return false;
            }
        } else if ("computeBill".equals(command)) {
            if(arguments.length != 0){
                System.out.println("Command `computeBill` takes no arguments.");
                return false;
            }
        } else if ("runTest".equals(command)) {
            if(arguments.length != 1){
                System.out.println("Command `runTest` takes exactly one argument: <testScenario-file");
                return false;
            }
        }



        return true;
    }
}
