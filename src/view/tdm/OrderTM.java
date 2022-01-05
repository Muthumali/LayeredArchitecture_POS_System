package view.tdm;

public class OrderTM {
    private String orderId;
    private String orderDate;
    private String orderTime;
    private String custId;

    public OrderTM() {
    }

    public OrderTM(String orderId, String orderDate, String orderTime, String custId) {
        this.setOrderId(orderId);
        this.setOrderDate(orderDate);
        this.setOrderTime(orderTime);
        this.setCustId(custId);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    @Override
    public String toString() {
        return "OrderTM{" +
                "orderId='" + orderId + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", custId='" + custId + '\'' +
                '}';
    }
}
