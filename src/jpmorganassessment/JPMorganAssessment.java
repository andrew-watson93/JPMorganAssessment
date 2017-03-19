/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpmorganassessment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author andre
 */
public class JPMorganAssessment {

    private static final String STRING_REGEX = "([a-zA-Z]+)";
    private static final String SPACE_REGEX = "(\\s+)";
    private static final String DECIMAL_REGEX = "(\\d+\\.?\\d{0,2})";
    private static final String INT_REGEX = "([1-9][0-9]*)";
    private static final String OPERATOR_REGEX = "([+\\-*])";

    private static final String MESSAGE_TYPE_1_REGEX_STRING = STRING_REGEX + SPACE_REGEX + DECIMAL_REGEX;
    private static final String MESSAGE_TYPE_2_REGEX_STRING = MESSAGE_TYPE_1_REGEX_STRING + SPACE_REGEX + INT_REGEX;
    private static final String ADJUSTMENT_REGEX_STRING = STRING_REGEX + SPACE_REGEX + DECIMAL_REGEX + SPACE_REGEX + OPERATOR_REGEX;

    private static final Pattern MESSAGE_TYPE_1_REGEX_PATTERN = Pattern.compile("^" + MESSAGE_TYPE_1_REGEX_STRING + "$");
    private static final Pattern MESSAGE_TYPE_2_REGEX_PATTERN = Pattern.compile("^" + MESSAGE_TYPE_2_REGEX_STRING + "$");
    private static final Pattern ADJUSTMENT_MESSAGE_PATTERN = Pattern.compile("^" + ADJUSTMENT_REGEX_STRING + "$");

    private static final int SALES_REPORT_NUMBER = 10;
    private static final int ADJUSTMENT_REPORT_NUMBER = 50;

    private static final NumberFormat PRICE_FORMATTER = new DecimalFormat("#0.00");

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {

        Tracker tracker = new Tracker();

        int messages = 0;
        File file = args.length > 0 ? new File(args[0]) : new File("sample_data.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher messageType1Matcher = MESSAGE_TYPE_1_REGEX_PATTERN.matcher(line);
                Matcher messageType2Matcher = MESSAGE_TYPE_2_REGEX_PATTERN.matcher(line);
                Matcher adjustmentMessageMatcher = ADJUSTMENT_MESSAGE_PATTERN.matcher(line);

                //If the current line in the input file matches one of the formats of
                //acceptable messages then create the appropriate object and update
                //the tracker with it
                if (messageType1Matcher.matches()) {
                    Sale sale = new Sale(messageType1Matcher.group(1).toLowerCase(), Double.parseDouble(messageType1Matcher.group(3)), 1);
                    tracker.updateSalesByProduct(sale);

                } else if (messageType2Matcher.matches()) {
                    Sale sale = new Sale(messageType2Matcher.group(1).toLowerCase(), Double.parseDouble(messageType2Matcher.group(3)), Integer.parseInt(messageType2Matcher.group(5)));
                    tracker.updateSalesByProduct(sale);

                } else if (adjustmentMessageMatcher.matches()) {
                    Adjustment adjustment = buildAdjustment(adjustmentMessageMatcher);
                    tracker.performAdjustmentAndAddToList(adjustment);

                }

                //If log sales reports every 10 messages
                if (messages % SALES_REPORT_NUMBER == 0) {
                    logSalesReport(tracker);
                }

                //Log adjustment report every 50 messages
                if (messages % ADJUSTMENT_REPORT_NUMBER == 0) {
                    System.out.println("Please wait, sales adjustments are being recorded.");
                    logAdjustmentsReport(tracker);
                }

            }

        }
    }

    /**
     * Extract the necessary info from the matcher and create a new adjustment
     * onject
     *
     * @param adjustmentMessageMatcher
     * @return
     */
    private static Adjustment buildAdjustment(Matcher adjustmentMessageMatcher) {
        String productName = adjustmentMessageMatcher.group(1).toLowerCase();
        double adjustmentAmount = Double.parseDouble(adjustmentMessageMatcher.group(3));
        char adjustmentType = adjustmentMessageMatcher.group(5).charAt(0);
        return new Adjustment(productName, adjustmentAmount, adjustmentType);
    }

    /**
     * Loop through map of sales, totalling prices/quantities and printing to
     * the console
     *
     * @param tracker
     */
    private static void logSalesReport(Tracker tracker) {

        System.out.println("===== RECORDING SALES =====");
        System.out.println();

        for (String productName : tracker.getSalesByProduct().keySet()) {
            double price = 0.0;
            int quantity = 0;
            for (Sale sale : tracker.getSalesByProduct().get(productName)) {
                price += sale.getQuantity() * sale.getPrice();
                quantity += sale.getQuantity();
            }
            System.out.println("PRODUCT NAME = " + productName + ", TOTAL SALES VALUE = " + PRICE_FORMATTER.format(price) + ", TOTAL QUANTITY SOLD = " + quantity);
            System.out.println();

        }
    }

    /**
     * Loop through the list of adjustments, convert to a readable string fornat
     * and print to the console
     *
     * @param tracker
     */
    private static void logAdjustmentsReport(Tracker tracker) {

        System.out.println("===== RECORDING ADJUSTMENTS =====");
        System.out.println();

        for (Adjustment adjustment : tracker.getAdjustments()) {
            String productName = adjustment.getProductName();
            String operation = adjustment.createOperationString();
            double amount = adjustment.getAmount();
            System.out.println("THE PRICE OF " + productName + " WAS " + operation + " BY " + PRICE_FORMATTER.format(amount));

        }
        System.out.println();

    }

}
