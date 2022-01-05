package controller;

import Entity.Customer;
import bo.BoFactory;
import bo.custom.CustomerBO;
import dto.CustomerDTO;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import view.tdm.CustomerTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class ManageCustomerFormController {
    private final CustomerBO customerBO = (CustomerBO) BoFactory.getBOFactory().getBO(BoFactory.BoTypes.CUSTOMER);
    public AnchorPane CustomerContext;
    public TextField txtCustomerTitle;
    public TextField txtAddress;
    public TextField txtProvince;
    public TextField txtCustomerId;
    public TextField txtCustName;
    public TextField txtCity;
    public TextField txtPostalCode;
    public TableColumn colCustomerId;
    public TableColumn colCustomerName;
    public TableColumn colCustomerTitle;
    public TableColumn colProvince;
    public TableColumn colAddress;
    public TableColumn colCity;
    public TableColumn colPostalCode;
    public Button btnModify;
    public Button btnRemove;
    public Button btnAdd;
    public TableView <CustomerTM>tblCustomer;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern titlePattern = Pattern.compile("^[A-z ]{3,50}$");
    Pattern namePattern = Pattern.compile("^[A-z ]{3,50}$");
    Pattern addressPattern = Pattern.compile("^[A-z ]{3,60}$");
    Pattern cityPattern = Pattern.compile("^[A-z ]{3,60}$");
    Pattern provincePattern = Pattern.compile("^[A-z ]{3,60}$");
    Pattern postalCodePattern = Pattern.compile("^[0-9]{4,}$");

    private void storeValidations() {
        map.put(txtCustomerTitle, titlePattern);
        map.put(txtCustName, namePattern);
        map.put(txtAddress, addressPattern);
        map.put(txtCity, cityPattern);
        map.put(txtProvince, provincePattern);
        map.put(txtPostalCode, postalCodePattern);


    }

    public void initialize() throws SQLException, ClassNotFoundException {
       lastId();
       loadAllCustomers();
       storeValidations();
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustomerTitle.setCellValueFactory(new PropertyValueFactory<>("customerTitle"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           try {
                setData(newValue);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }


    public void setData(CustomerTM customerTM) throws SQLException, ClassNotFoundException {
        String Id=tblCustomer.getSelectionModel().getSelectedItem().getCustomerId();

        Customer c=customerBO.getCustomer(Id);
        if (c==null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set").show();
        } else {
            setDataToField(c);
        }
    }

    private Object validate() {
        //btnSaveBook.setDisable(true);

        for (TextField textFieldKey : map.keySet()) {
            Pattern patternValue = map.get(textFieldKey);
            if (!patternValue.matcher(textFieldKey.getText()).matches()) {
                if (!textFieldKey.getText().isEmpty()){
                    textFieldKey.getParent().setStyle("-fx-border-color: red");
                }
                return textFieldKey;
            }
            textFieldKey.getParent().setStyle("-fx-border-color: green");
        }
        // btnSaveBook.setDisable(false);
        return true;
    }

    public void setDataToField(Customer c){
        txtCustomerId.setText(c.getCustomerId());
        txtCustomerTitle.setText(c.getCustomerTitle());
        txtCustName.setText(c.getCustomerName());
        txtAddress.setText(c.getCustomerAddress());
        txtCity.setText(c.getCity());
        txtProvince.setText(c.getProvince());
        txtPostalCode.setText(c.getPostalCode());
    }

    public void OneAction(KeyEvent keyEvent) {
        Object response = validate();

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    public void onActionTwo(KeyEvent keyEvent) {
        Object response = validate();

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    public void ModifyOnAction(ActionEvent actionEvent) {
        String id=txtCustomerId.getText();
        String title=txtCustomerTitle.getText();
        String name=txtCustName.getText();
        String address=txtAddress.getText();
        String city=txtCity.getText();
        String province=txtProvince.getText();
        String postalCode=txtPostalCode.getText();
        try {
            if (!existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
            }
            CustomerDTO customerDTO = new CustomerDTO(id,title, name, address,city,province,postalCode);
            if(customerBO.updateCustomer(customerDTO)){
                new Alert(Alert.AlertType.CONFIRMATION, "Updated..").show();
                loadAllCustomers();
                lastId();

                //clear text field
                txtCustomerTitle.clear();
                txtCustName.clear();
                txtAddress.clear();
                txtCity.clear();
                txtProvince.clear();
                txtPostalCode.clear();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update the customer " + id + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void RemoveOnAction(ActionEvent actionEvent) {
        /*Delete Customer*/
        String id = tblCustomer.getSelectionModel().getSelectedItem().getCustomerId();
        try {
            if (!existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
            }
            if(customerBO.deleteCustomer(id)){
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted..").show();
                loadAllCustomers();
                lastId();

                //clear text field
                txtCustomerTitle.clear();
                txtCustName.clear();
                txtAddress.clear();
                txtCity.clear();
                txtProvince.clear();
                txtPostalCode.clear();
                tblCustomer.getItems().remove(tblCustomer.getSelectionModel().getSelectedItem());
                tblCustomer.getSelectionModel().clearSelection();

            }


        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the customer " + id).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void AddCustomerOnAction(ActionEvent actionEvent) {
        String id=txtCustomerId.getText();
        String title=txtCustomerTitle.getText();
        String name=txtCustName.getText();
        String address=txtAddress.getText();
        String city=txtCity.getText();
        String province=txtProvince.getText();
        String postalCode=txtPostalCode.getText();
        try {
            if (existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, id + " already exists").show();
            }
            CustomerDTO customerDTO = new CustomerDTO(id,title, name, address,city,province,postalCode);
            tblCustomer.getItems().add(new CustomerTM(id,title, name, address,city,province,postalCode));
            if(customerBO.addCustomer(customerDTO)){
                new Alert(Alert.AlertType.CONFIRMATION, "Saved..").show();
                loadAllCustomers();
                lastId();

                //clear text field
                txtCustomerTitle.clear();
                txtCustName.clear();
                txtAddress.clear();
                txtCity.clear();
                txtProvince.clear();
                txtPostalCode.clear();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save the customer " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerBO.ifCustomerExist(id);
    }

    private void loadAllCustomers() {
        tblCustomer.getItems().clear();
        /*Get all customers*/
        try {
            ArrayList<CustomerDTO> allCustomers = customerBO.getAllCustomer();
            for (CustomerDTO customer : allCustomers) {
                tblCustomer.getItems().add(new CustomerTM(customer.getCustomerId(), customer.getCustomerTitle(), customer.getCustomerName()
                ,customer.getCustomerAddress(),customer.getCity(),customer.getProvince(),customer.getPostalCode()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void lastId() throws SQLException, ClassNotFoundException {
        String Id = customerBO.generateNewID();;
        String finalId = "C-001";

        if (Id != null) {

            String[] splitString = Id.split("-");
            int id = Integer.parseInt(splitString[1]);
            id++;

            if (id < 10) {
                finalId = "C-00" + id;
            } else if (id < 100) {
                finalId = "C-0" + id;
            } else {
                finalId = "C-" + id;
            }
            txtCustomerId.setText(finalId);
        } else {
           txtCustomerId.setText(finalId);
        }
    }
}
