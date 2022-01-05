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

public class AdminDashBoardFormController {
    public AnchorPane AdminContext;
    public AnchorPane AdminLoadContext;
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

    public void ManageItemOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/ManageItemForm.fxml");
        Parent load = FXMLLoader.load(resource);
        AdminLoadContext.getChildren().clear();
        AdminLoadContext.getChildren().add(load);
    }


    public void SystemReportOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/SystemReportForm.fxml");
        Parent load = FXMLLoader.load(resource);
        AdminLoadContext.getChildren().clear();
        AdminLoadContext.getChildren().add(load);
    }

    public void LogoutOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/LoginForm.fxml"));
        Parent parent=loader.load();
        Scene scene=new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        Stage stage1= (Stage)AdminContext.getScene().getWindow();
        stage1.close();
    }
}
