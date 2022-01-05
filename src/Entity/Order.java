package Entity;

import java.time.LocalDate;
import java.util.ArrayList;

public class Order {
    private String orderId;
    private String orderDate;
    private String orderTime;
    private String custId;
    private ArrayList<OrderDetail> items;

    public Order() {
    }

    public Order(String orderId, String orderDate, String orderTime, String custId, ArrayList<OrderDetail> items) {
        this.setOrderId(orderId);
        this.setOrderDate(orderDate);
        this.setOrderTime(orderTime);
        this.setCustId(custId);
        this.setItems(items);
    }



    public Order(String orderId, String orderDate, String orderTime, String custId) {
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

    public ArrayList<OrderDetail> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderDetail> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", custId='" + custId + '\'' +
                ", items=" + items +
                '}';
    }
}
