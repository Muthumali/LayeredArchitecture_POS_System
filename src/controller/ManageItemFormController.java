package controller;

import Entity.Customer;
import Entity.Item;
import bo.BoFactory;
import bo.custom.ItemBO;
import dto.CustomerDTO;
import dto.ItemDTO;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import view.tdm.CustomerTM;
import view.tdm.ItemTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ManageItemFormController {
    public AnchorPane ItemContext;
    public TextField txtItemCode;
    public TextField txtDescription;
    public TextField txtPackSize;
    public TextField txtUnitPrice;
    public TextField txtQty;
    public Button btnAddItem;
    public TableView<ItemTM> tblItems;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colPackSize;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public Button btnModifyItem;
    public Button btnRemove;
    private final ItemBO itemBO = (ItemBO) BoFactory.getBOFactory().getBO(BoFactory.BoTypes.ITEM);


    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern descriptionPattern = Pattern.compile("^[A-z ]{3,50}$");
    Pattern packSizePattern = Pattern.compile("^[0-9 ]{2,50}$");
    Pattern unitPricePattern = Pattern.compile("^[0-9]{2,60}$");
    Pattern qtyPattern = Pattern.compile("^[0-9]{2,40}$");

    private void storeValidations() {
        map.put(txtDescription,descriptionPattern);
        map.put(txtPackSize, packSizePattern);
        map.put(txtPackSize, unitPricePattern);
        map.put(txtQty, qtyPattern);

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


    public void initialize() throws SQLException, ClassNotFoundException {
        lastId();
        loadAllItems();

        storeValidations();
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        tblItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                setData(newValue);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void setData(ItemTM itemTM) throws SQLException, ClassNotFoundException {
        String Id=tblItems.getSelectionModel().getSelectedItem().getItemCode();

        Item i=itemBO.getItem(Id);
        if (i==null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set").show();
        } else {
            setDataToField(i);
        }
    }

    public void setDataToField(Item i){
       txtItemCode.setText(i.getItemCode());
       txtDescription.setText(i.getDescription());
       txtPackSize.setText(i.getPackSize());
       txtUnitPrice.setText(String.valueOf(i.getUnitPrice()));
       txtQty.setText(String.valueOf(i.getQtyOnHand()));
    }

    public void OnActionOne(KeyEvent keyEvent) {
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

    public void AddItemOnAction(ActionEvent actionEvent) {
        String id=txtItemCode.getText();
        String description=txtDescription.getText();
        String packSize=txtPackSize.getText();
        Double unitPrice=Double.parseDouble(txtUnitPrice.getText());
        int qty=Integer.parseInt(txtQty.getText());

        try {
            if (existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, id + " already exists").show();
            }
            ItemDTO itemDTO= new ItemDTO(id,description,packSize,unitPrice,qty);
            tblItems.getItems().add(new ItemTM(id,description,packSize,unitPrice,qty));
            if(itemBO.addItem(itemDTO)){
                new Alert(Alert.AlertType.CONFIRMATION, "Saved..").show();
                loadAllItems();
                lastId();

                //clear text field
                txtDescription.clear();
                txtPackSize.clear();
                txtQty.clear();
                txtUnitPrice.clear();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save the Item" + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void ModifyItemOnAction(ActionEvent actionEvent) {
        String id=txtItemCode.getText();
        String description=txtDescription.getText();
        String packSize=txtPackSize.getText();
        Double unitPrice=Double.parseDouble(txtUnitPrice.getText());
        int qty=Integer.parseInt(txtQty.getText());

        try {
            if (!existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such item associated with the id " + id).show();
            }
           ItemDTO itemDTO = new ItemDTO(id,description,packSize,unitPrice,qty);
            if(itemBO.updateItem(itemDTO)){
                new Alert(Alert.AlertType.CONFIRMATION, "Updated..").show();
                loadAllItems();
                lastId();

                //clear text field
                txtDescription.clear();
                txtPackSize.clear();
                txtQty.clear();
                txtUnitPrice.clear();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update the Item " + id + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void RemoveItemOnAction(ActionEvent actionEvent) {
        /*Delete Item*/
        String id = tblItems.getSelectionModel().getSelectedItem().getItemCode();
        try {
            if (!existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
            }
            if(itemBO.deleteItem(id)){
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted..").show();
                loadAllItems();
                lastId();

                //clear text field
                txtDescription.clear();
                txtPackSize.clear();
                txtQty.clear();
                txtUnitPrice.clear();
                tblItems.getItems().remove(tblItems.getSelectionModel().getSelectedItem());
                tblItems.getSelectionModel().clearSelection();

            }


        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the customer " + id).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return itemBO.ifItemExist(id);
    }

    public void lastId() throws SQLException, ClassNotFoundException {
        String Id = itemBO.generateNewID();;
        String finalId = "I-001";

        if (Id != null) {

            String[] splitString = Id.split("-");
            int id = Integer.parseInt(splitString[1]);
            id++;

            if (id < 10) {
                finalId = "I-00" + id;
            } else if (id < 100) {
                finalId = "I-0" + id;
            } else {
                finalId = "I-" + id;
            }
            txtItemCode.setText(finalId);
        } else {
            txtItemCode.setText(finalId);
        }
    }

    private void loadAllItems() {
        tblItems.getItems().clear();
        /*Get all item*/
        try {
            ArrayList<ItemDTO> allItem=itemBO.getAllItems();
            for (ItemDTO itemDTO: allItem) {
                tblItems.getItems().add(new ItemTM(itemDTO.getItemCode(),itemDTO.getDescription(),
                        itemDTO.getPackSize(),itemDTO.getUnitPrice(),itemDTO.getQtyOnHand()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}
