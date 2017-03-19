/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpmorganassessment;

/**
 *
 * @author andrew
 */
public class Adjustment {

    private String productName;
    private double amount;
    private char adjustmentType;
    
    public Adjustment(){
        
    }
    
    public Adjustment(String productName, double amount, char adjustmentType){
        this.productName = productName;
        this.amount = amount;
        this.adjustmentType = adjustmentType;
    }
            

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return the adjustmentType
     */
    public char getAdjustmentType() {
        return adjustmentType;
    }

    /**
     * @param adjustmentType the adjustmentType to set
     */
    public void setAdjustmentType(char adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    String createOperationString() {
        switch(adjustmentType){
            case '+':
                return "INCREASED";
            case '-':
                return "DECREASED";
            case '*':
                return "MULTIPLIED";
            default:
                return "";
        }
    }

}
