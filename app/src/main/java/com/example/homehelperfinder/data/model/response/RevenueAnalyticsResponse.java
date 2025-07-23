package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response model for revenue analytics API
 */
public class RevenueAnalyticsResponse {
    
    @SerializedName("totalRevenue")
    private double totalRevenue;
    
    @SerializedName("netRevenue")
    private double netRevenue;
    
    @SerializedName("platformFees")
    private double platformFees;
    
    @SerializedName("helperEarnings")
    private double helperEarnings;
    
    @SerializedName("averageTransactionValue")
    private double averageTransactionValue;
    
    @SerializedName("totalTransactions")
    private int totalTransactions;
    
    @SerializedName("successfulPayments")
    private int successfulPayments;
    
    @SerializedName("failedPayments")
    private int failedPayments;
    
    @SerializedName("paymentSuccessRate")
    private double paymentSuccessRate;
    
    @SerializedName("monthlyTrend")
    private List<MonthlyTrend> monthlyTrend;
    
    @SerializedName("paymentMethods")
    private List<PaymentMethod> paymentMethods;
    
    @SerializedName("revenueByService")
    private List<RevenueByService> revenueByService;
    
    @SerializedName("analyticsPeriodStart")
    private String analyticsPeriodStart;
    
    @SerializedName("analyticsPeriodEnd")
    private String analyticsPeriodEnd;
    
    // Getters
    public double getTotalRevenue() { return totalRevenue; }
    public double getNetRevenue() { return netRevenue; }
    public double getPlatformFees() { return platformFees; }
    public double getHelperEarnings() { return helperEarnings; }
    public double getAverageTransactionValue() { return averageTransactionValue; }
    public int getTotalTransactions() { return totalTransactions; }
    public int getSuccessfulPayments() { return successfulPayments; }
    public int getFailedPayments() { return failedPayments; }
    public double getPaymentSuccessRate() { return paymentSuccessRate; }
    public List<MonthlyTrend> getMonthlyTrend() { return monthlyTrend; }
    public List<PaymentMethod> getPaymentMethods() { return paymentMethods; }
    public List<RevenueByService> getRevenueByService() { return revenueByService; }
    public String getAnalyticsPeriodStart() { return analyticsPeriodStart; }
    public String getAnalyticsPeriodEnd() { return analyticsPeriodEnd; }
    
    // Setters
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    public void setNetRevenue(double netRevenue) { this.netRevenue = netRevenue; }
    public void setPlatformFees(double platformFees) { this.platformFees = platformFees; }
    public void setHelperEarnings(double helperEarnings) { this.helperEarnings = helperEarnings; }
    public void setAverageTransactionValue(double averageTransactionValue) { this.averageTransactionValue = averageTransactionValue; }
    public void setTotalTransactions(int totalTransactions) { this.totalTransactions = totalTransactions; }
    public void setSuccessfulPayments(int successfulPayments) { this.successfulPayments = successfulPayments; }
    public void setFailedPayments(int failedPayments) { this.failedPayments = failedPayments; }
    public void setPaymentSuccessRate(double paymentSuccessRate) { this.paymentSuccessRate = paymentSuccessRate; }
    public void setMonthlyTrend(List<MonthlyTrend> monthlyTrend) { this.monthlyTrend = monthlyTrend; }
    public void setPaymentMethods(List<PaymentMethod> paymentMethods) { this.paymentMethods = paymentMethods; }
    public void setRevenueByService(List<RevenueByService> revenueByService) { this.revenueByService = revenueByService; }
    public void setAnalyticsPeriodStart(String analyticsPeriodStart) { this.analyticsPeriodStart = analyticsPeriodStart; }
    public void setAnalyticsPeriodEnd(String analyticsPeriodEnd) { this.analyticsPeriodEnd = analyticsPeriodEnd; }
    
    /**
     * Nested class for monthly trend data
     */
    public static class MonthlyTrend {
        @SerializedName("year")
        private int year;
        
        @SerializedName("month")
        private int month;
        
        @SerializedName("monthName")
        private String monthName;
        
        @SerializedName("revenue")
        private double revenue;
        
        @SerializedName("platformFees")
        private double platformFees;
        
        @SerializedName("helperEarnings")
        private double helperEarnings;
        
        @SerializedName("transactionCount")
        private int transactionCount;
        
        @SerializedName("growthRate")
        private double growthRate;
        
        // Getters
        public int getYear() { return year; }
        public int getMonth() { return month; }
        public String getMonthName() { return monthName; }
        public double getRevenue() { return revenue; }
        public double getPlatformFees() { return platformFees; }
        public double getHelperEarnings() { return helperEarnings; }
        public int getTransactionCount() { return transactionCount; }
        public double getGrowthRate() { return growthRate; }
        
        // Setters
        public void setYear(int year) { this.year = year; }
        public void setMonth(int month) { this.month = month; }
        public void setMonthName(String monthName) { this.monthName = monthName; }
        public void setRevenue(double revenue) { this.revenue = revenue; }
        public void setPlatformFees(double platformFees) { this.platformFees = platformFees; }
        public void setHelperEarnings(double helperEarnings) { this.helperEarnings = helperEarnings; }
        public void setTransactionCount(int transactionCount) { this.transactionCount = transactionCount; }
        public void setGrowthRate(double growthRate) { this.growthRate = growthRate; }
    }
    
    /**
     * Nested class for payment method data
     */
    public static class PaymentMethod {
        @SerializedName("method")
        private String method;
        
        @SerializedName("count")
        private int count;
        
        @SerializedName("percentage")
        private double percentage;
        
        // Getters and Setters
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        public double getPercentage() { return percentage; }
        public void setPercentage(double percentage) { this.percentage = percentage; }
    }
    
    /**
     * Nested class for revenue by service data
     */
    public static class RevenueByService {
        @SerializedName("serviceId")
        private int serviceId;
        
        @SerializedName("serviceName")
        private String serviceName;
        
        @SerializedName("revenue")
        private double revenue;
        
        @SerializedName("percentage")
        private double percentage;
        
        // Getters and Setters
        public int getServiceId() { return serviceId; }
        public void setServiceId(int serviceId) { this.serviceId = serviceId; }
        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
        public double getRevenue() { return revenue; }
        public void setRevenue(double revenue) { this.revenue = revenue; }
        public double getPercentage() { return percentage; }
        public void setPercentage(double percentage) { this.percentage = percentage; }
    }
}
