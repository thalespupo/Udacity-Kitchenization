package com.tapura.kitchenization.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class IngredientsItem {

    @SerializedName("quantity")
    private double quantity;

    @SerializedName("measure")
    private String measure;

    @SerializedName("ingredient")
    private String ingredient;

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getMeasure() {
        return measure;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getIngredient() {
        return ingredient;
    }

    @Override
    public String toString() {
        return
                "IngredientsItem{" +
                        "quantity = '" + quantity + '\'' +
                        ",measure = '" + measure + '\'' +
                        ",ingredient = '" + ingredient + '\'' +
                        "}";
    }
}