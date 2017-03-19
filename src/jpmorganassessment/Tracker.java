/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpmorganassessment;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author andre
 */
public class Tracker {

    private ArrayList<Adjustment> adjustments = new ArrayList<>();
    private TreeMap<String, ArrayList<Sale>> salesByProduct = new TreeMap<>();

    /**
     * @return the adjustments
     */
    public ArrayList<Adjustment> getAdjustments() {
        return adjustments;
    }

    /**
     * @param adjustments the adjustments to set
     */
    public void setAdjustments(ArrayList<Adjustment> adjustments) {
        this.adjustments = adjustments;
    }

    /**
     * @return the salesByProduct
     */
    public TreeMap<String, ArrayList<Sale>> getSalesByProduct() {
        return salesByProduct;
    }

    /**
     * @param salesByProduct the salesByProduct to set
     */
    public void setSalesByProduct(TreeMap<String, ArrayList<Sale>> salesByProduct) {
        this.salesByProduct = salesByProduct;
    }

    /**
     * Create and add new entry for product of sale in question if product not
     * currently in map, or add to its entry otherwise.
     *
     * @param sale
     */
    public void updateSalesByProduct(Sale sale) {
        String productName = sale.getProductName();
        if (!salesByProduct.containsKey(productName)) {
            salesByProduct.put(productName, new ArrayList<>());
        }
        salesByProduct.get(productName).add(sale);
    }

    /**
     * Add the new adjustment to the list of adjustments and perform the new
     * adjustment based on its type
     *
     * @param adjustment
     */
    public void performAdjustmentAndAddToList(Adjustment adjustment) {
        adjustments.add(adjustment);
        char adjustmentType = adjustment.getAdjustmentType();
        switch (adjustmentType) {
            case '+':
                performPriceIncrease(adjustment.getAmount(), adjustment.getProductName());
                break;
            case '-':
                performPriceIncrease(adjustment.getAmount() * -1.0, adjustment.getProductName());
                break;
            case '*':
                performPriceMultiply(adjustment.getAmount(), adjustment.getProductName());
                break;
        }

    }

    /**
     * Increase all sales of the productName in question by the desired amount
     * @param amount
     * @param productName 
     */
    private void performPriceIncrease(double amount, String productName) {
        for (Sale sale : salesByProduct.get(productName)) {
            sale.setPrice(sale.getPrice() + amount);
        }
    }

    /**
     * Multiply all sales of the productName in question by the desired amount
     * @param amount
     * @param productName 
     */
    private void performPriceMultiply(double amount, String productName) {
        for (Sale sale : salesByProduct.get(productName)) {
            sale.setPrice(sale.getPrice() * amount);

        }
    }
}
