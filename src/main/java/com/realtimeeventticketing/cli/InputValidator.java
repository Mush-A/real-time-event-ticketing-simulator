package com.realtimeeventticketing.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class InputValidator {
    private static final Logger log = LogManager.getLogger(InputValidator.class);

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
                log.info("Invalid input. Please enter a non-negative integer:");
            }
        }
    }
}
