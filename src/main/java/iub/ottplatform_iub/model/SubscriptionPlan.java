package iub.ottplatform_iub.model;

import java.io.Serializable;

public class SubscriptionPlan implements Serializable {
    private String planId;
    private String name;
    private String description;
    private double price;
    private int durationMonths;
    private boolean isActive;
    private String features;

    public SubscriptionPlan(String planId, String name, String description, double price,
            int durationMonths, String features) {
        this.planId = planId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationMonths = durationMonths;
        this.isActive = true;
        this.features = features;
    }

    // Getters and Setters
    public String getPlanId() {
        return planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }
}