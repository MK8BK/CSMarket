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
}
