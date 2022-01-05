package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CashierDashBoardFormController {

    public AnchorPane CashierContext;
    public AnchorPane CashierLoadContext;
    public TextField txtDate;
    public TextField txtTime;

    public void initialize() {
        //----------------------Set Date & time--------------------------------------------------------------
        try {
            new Timer(1000, e -> {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss aa");
                String time = simpleDateFormat.format(new Date());
                String date = new SimpleDateFormat("MMM dd, YYYY", Locale.ENGLISH).format(new Date());
                txtDate.setText(date);
                txtTime.setText(time);
            }).start();
        } catch (Exception exception) {
        }


    }

    public void ManageCustomerOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/ManageCustomerForm.fxml");
        Parent load = FXMLLoader.load(resource);
        CashierLoadContext.getChildren().clear();
        CashierLoadContext.getChildren().add(load);
    }

    public void ManageOrderOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/ManageOrderForm.fxml");
        Parent load = FXMLLoader.load(resource);
        CashierLoadContext.getChildren().clear();
        CashierLoadContext.getChildren().add(load);
    }

    public void LogOutOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/LoginForm.fxml"));
        Parent parent=loader.load();
        Scene scene=new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        Stage stage1= (Stage)CashierContext.getScene().getWindow();
        stage1.close();
    }
}
