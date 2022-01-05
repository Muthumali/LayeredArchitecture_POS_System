package controller;

import Entity.Customer;
import Entity.Item;
import Entity.Order;
import Entity.OrderDetail;
import bo.BoFactory;
import bo.custom.ManageOrderBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import view.tdm.OrderDetailTM;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ManageOrderFromController {
    public AnchorPane MakePaymentContext;
    public Label lblOrderId;
    public Label lblDate;
    public Label lblTime;
    public JFXComboBox <String>cmbCustomerId;
    public JFXTextField txtCustomerTitle;
    public JFXTextField txtCustomerName;
    public JFXTextField txtCustomerAddress;
    public JFXTextField txtCity;
    public JFXTextField txtProvince;
    public JFXTextField txtPostalCode;
    public JFXComboBox <String>cmbItemCode;
    public JFXTextField txtDescription;
    public JFXTextField txtPackSize;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtQty;
    public TableView <OrderDetailTM>tblCart;
    public TableColumn colOrderId;
    public TableColumn colItemCode;
    public TableColumn colOrderQty;
    public TableColumn colDiscount;
    public TableColumn colTotal;
    public Label lblTotal;
    private final ManageOrderBO manageOrderBO = (ManageOrderBO) BoFactory.getBOFactory().getBO(BoFactory.BoTypes.MANAGE_ORDER);
    public JFXButton btnSave;



    public void initialize() throws SQLException, ClassNotFoundException {
        loadAllCustomerIds();
        loadAllItemCodes();
        lastId();
        loadDateAndTime();

        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colOrderQty.setCellValueFactory(new PropertyValueFactory<>("OrderQty"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));


        cmbCustomerId.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> {
                    try {
                        setCustomerData( newValue);
                    } catch (SQLException | ClassNotFoundException throwables) {
                        throwables.printStackTrace();
                    }
                });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                setItemData( newValue);
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newItemCode) -> {
            txtQty.setEditable(newItemCode != null);
            btnSave.setDisable(newItemCode == null);

            if (newItemCode != null) {
                try {
                    if (!existItem(newItemCode + "")) {
                        //throw new NotFoundException("There is no such item associated with the id " + code);
                    }
                    /*Find Item*/
                    ItemDTO item = manageOrderBO.searchItem(newItemCode + "");
                    txtDescription.setText(item.getDescription());
                    txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                    Optional<OrderDetailTM> optOrderDetail = tblCart.getItems().stream().filter(detail -> detail.getItemCode().equals(newItemCode)).findFirst();
                    txtQtyOnHand.setText((optOrderDetail.isPresent() ? item.getQtyOnHand() - optOrderDetail.get().getOrderQty() : item.getQtyOnHand()) + "");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                txtDescription.clear();
                txtQty.clear();
                txtQtyOnHand.clear();
                txtUnitPrice.clear();
            }
        });



        tblCart.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedOrderDetail) -> {
            if (selectedOrderDetail != null) {
                cmbItemCode.setDisable(true);
                cmbItemCode.setValue(selectedOrderDetail.getItemCode());
                btnSave.setText("Update");
                //txtQtyOnHand.setText(Integer.parseInt(txtQtyOnHand.getText()) + selectedOrderDetail.getOrderQty() + "");
                txtQty.setText(selectedOrderDetail.getOrderQty() + "");
            } else {
                btnSave.setText("Add");
                cmbItemCode.setDisable(false);
                cmbItemCode.getSelectionModel().clearSelection();
                txtQty.clear();
            }
        });


    }

    private boolean existItem(String code) throws SQLException, ClassNotFoundException {
        return manageOrderBO.ifItemExist(code);
    }


    private void setItemData(String itemCode) throws SQLException, ClassNotFoundException {

        Item i=manageOrderBO.getItem(itemCode);
        if (i == null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set");
        } else {
            txtDescription.setText(i.getDescription());
            txtPackSize.setText(i.getPackSize());
            txtUnitPrice.setText(String.valueOf(i.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(i.getQtyOnHand()));
        }
    }

    private void setCustomerData(String customerId) throws SQLException, ClassNotFoundException {
        Customer c=manageOrderBO.getCustomer(customerId);
        if (c == null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set");
        } else {
            txtCustomerTitle.setText(c.getCustomerTitle());
            txtCustomerName.setText(c.getCustomerName());
            txtCustomerAddress.setText(c.getCustomerAddress());
            txtCity.setText(c.getCity());
            txtProvince.setText(c.getProvince());
            txtPostalCode.setText(c.getPostalCode());
        }
    }

    public void AddToCartOnAction(ActionEvent actionEvent) {
        String id=lblOrderId.getText();
        String itemCode=cmbItemCode.getSelectionModel().getSelectedItem();
        int  qtyOnHand=Integer.parseInt(txtQtyOnHand.getText());
        Double discount=50.00;
        String orderDate=lblDate.getText();
        String orderTime=lblTime.getText();
        String customerId=cmbCustomerId.getSelectionModel().getSelectedItem();
        Double unitPrice=Double.parseDouble(txtUnitPrice.getText());
        int orderQty=Integer.parseInt(txtQty.getText());
        Double total=(unitPrice*orderQty)-(discount);

        if (qtyOnHand<orderQty) {
            new Alert(Alert.AlertType.ERROR, "Invalid qty").show();
        }else {

          boolean exists = tblCart.getItems().stream().anyMatch(detail -> detail.getItemCode().equals(itemCode));

            if (exists) {
                OrderDetailTM orderDetailTM = tblCart.getItems().stream().filter(detail -> detail.getItemCode().equals(itemCode)).findFirst().get();

                if (btnSave.getText().equalsIgnoreCase("Update")) {
                    orderDetailTM.setOrderQty(orderQty);
                    orderDetailTM.setTotal(total);
                    tblCart.getSelectionModel().clearSelection();
                } else {
                    orderDetailTM.setOrderQty(orderDetailTM.getOrderQty()+ orderQty);
                    orderDetailTM.setTotal(total);
                }
                tblCart.refresh();
            } else {
                tblCart.getItems().add(new OrderDetailTM(id,itemCode,orderQty,discount,orderDate,orderTime,customerId,total));
            }
            calculateTotal();
            cmbItemCode.getSelectionModel().clearSelection();
            txtDescription.clear();
            txtPackSize.clear();
            txtQtyOnHand.clear();
            txtUnitPrice.clear();
            txtQty.clear();

        }
    }

    private void calculateTotal() {
       Double total = 0.00;
        for (OrderDetailTM detail : tblCart.getItems()) {
            total = detail.getTotal();
        }
        lblTotal.setText("Total: " + total);
    }



    public void PlaceOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
       
      boolean b = saveOrder(lblOrderId.getText(), lblDate.getText(),lblTime.getText(), cmbCustomerId.getValue(),
                tblCart.getItems().stream().map(tm -> new OrderDetailDTO(lblOrderId.getText(), tm.getItemCode(), tm.getOrderQty(), tm.getDiscount(),tm.getOrderDate(),tm.getOrderTime(),tm.getCustomerId(),tm.getTotal())).collect(Collectors.toList()));
        if (b) {
            new Alert(Alert.AlertType.INFORMATION, "Order has been placed successfully").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Order has not been placed successfully").show();
        }

        lastId();
        cmbCustomerId.getSelectionModel().clearSelection();
        txtCustomerTitle.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCity.clear();
        txtProvince.clear();
        txtPostalCode.clear();
        cmbItemCode.getSelectionModel().clearSelection();
        tblCart.getItems().clear();
        txtQty.clear();


        calculateTotal();
    }

    public boolean saveOrder(String orderId,String orderDate,String orderTime,String customerId,List<OrderDetailDTO>orderDetails) {
        try {
            OrderDTO orderDTO = new OrderDTO(orderId,orderDate,orderTime,customerId,orderDetails);
            return manageOrderBO.purchaseOrder(orderDTO);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void loadAllCustomerIds() {
        try {
            ArrayList<CustomerDTO> all = manageOrderBO.getAllCustomers();
            for (CustomerDTO customerDTO : all) {
                cmbCustomerId.getItems().add(customerDTO.getCustomerId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load customer ids").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllItemCodes() {
        try {
            ArrayList<ItemDTO> all = manageOrderBO.getAllItems();
            for (ItemDTO dto : all) {
                cmbItemCode.getItems().add(dto.getItemCode());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void lastId() throws SQLException, ClassNotFoundException {
        String Id = manageOrderBO.generateNewOrderId();;
        String finalId = "O-001";

        if (Id != null) {

            String[] splitString = Id.split("-");
            int id = Integer.parseInt(splitString[1]);
            id++;

            if (id < 10) {
                finalId = "O-00" + id;
            } else if (id < 100) {
                finalId = "O-0" + id;
            } else {
                finalId = "O-" + id;
            }
            lblOrderId.setText(finalId);
        } else {
            lblOrderId.setText(finalId);
        }
    }

    private void loadDateAndTime() {
        // load Date
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        // load Time
        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(
                    currentTime.getHour() + " : " + currentTime.getMinute() +
                            " : " + currentTime.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

}
