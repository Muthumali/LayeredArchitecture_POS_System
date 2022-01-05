package dto;

import Entity.Order;

import java.io.Serializable;
import java.util.List;

public class OrderDTO implements Serializable {
    private String orderId;
    private String orderDate;
    private String orderTime;
    private String custId;
    private List<OrderDetailDTO> orderDetail;

    public OrderDTO() {
    }

    public OrderDTO(String orderId, String orderDate, String orderTime, String custId, List<OrderDetailDTO> orderDetail) {
        this.setOrderId(orderId);
        this.setOrderDate(orderDate);
        this.setOrderTime(orderTime);
        this.setCustId(custId);
        this.setOrderDetail(orderDetail);
    }

    public OrderDTO(Order orders) {
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

    public List<OrderDetailDTO> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderDetailDTO> orderDetail) {
        this.orderDetail = orderDetail;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId='" + orderId + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", custId='" + custId + '\'' +
                ", orderDetail=" + orderDetail +
                '}';
    }
}
