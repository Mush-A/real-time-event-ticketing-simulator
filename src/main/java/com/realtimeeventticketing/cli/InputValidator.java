package com.realtimeeventticketing.cli;

import java.util.Scanner;

public class InputValidator {
    public static int getValidIntInput(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input);
                if (value < 0) {
                    throw new NumberFormatException();
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a non-negative integer:");
            }
        }
    }
}
