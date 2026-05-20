package io.mk8bk;

import java.util.Arrays;
import java.util.Scanner;

public class SuperMarketCheckoutSystem {
    static PointOfSale POS;
    static TransactionAuthorisationSystem TAS;

    public static void main(String[] args) {
        loadData();
        handleCLI();
    }

    private static void loadData() {
    }

    public static void handleCLI(){
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("Welcome to t");
        System.out.println("Available commands:");
        System.out.println("  HELP - Show this help message");
        System.out.println("  GREET [name] - Greet a person");
        System.out.println("  CALC [operation] [num1] [num2] - Perform calculation");
        System.out.println("  STOP - Exit the program");
        System.out.println("Enter your commands below:");

        while (true) {
            System.out.print("> ");
            input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("STOP")) {
                System.out.println("Exiting the program...");
                break;
            }

            if (input.isEmpty()) {
                continue;
            }

            // Split input into command and arguments
            String[] parts = input.split("\\s+");
            String command = parts[0].toUpperCase();
            String[] arguments = Arrays.copyOfRange(parts, 1, parts.length);

            switch (command) {
                case "COMMAND":
                    break;
                default:
                    System.out.println("Unknown command: " + command);
                    System.out.println("Type HELP for available commands");
            }
        }
        scanner.close();
    }
}
