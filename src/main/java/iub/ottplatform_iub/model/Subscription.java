package iub.ottplatform_iub.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Subscription implements Serializable {
    private String subscriptionId;
    private String userId;
    private String planType; // BASIC, PREMIUM
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;
    private boolean isActive;
    private String paymentMethod;

    public Subscription(String subscriptionId, String userId, String planType,
            LocalDate startDate, LocalDate endDate, double price, String paymentMethod) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.planType = planType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.isActive = true;
    }

    // Getters and Setters
    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPlanType() {
        return planType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getPrice() {
        return price;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(endDate);
    }
}