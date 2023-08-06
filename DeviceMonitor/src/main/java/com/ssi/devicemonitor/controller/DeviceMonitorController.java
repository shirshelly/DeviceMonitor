package com.ssi.devicemonitor.controller;

import com.ssi.devicemonitor.entity.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;
import java.util.TimerTask;

public class DeviceMonitorController {
    @FXML
    private ListView<Device> deviceListView;

    @FXML
    private TextField deviceNameTextField;

//    @FXML
//    private Button addGeneralDeviceButton;
    @FXML
    private Button addHardwareDeviceButton;
    @FXML
    private Button addSoftwareDeviceButton;
    @FXML
    private AnchorPane mainPane;

    private DeviceMonitor deviceMonitor;
    private String filename = "data.txt";


    public void initialize() {
        deviceMonitor = new DeviceMonitor();


        deviceMonitor.addDevice(new HardwareDevice("Device 1",null,null,null,null,null));
        deviceMonitor.addDevice(new HardwareDevice("Device 2",null,null,null,null,null));
        deviceMonitor.addDevice(new SoftwareDevice("Device 3",null,null,null,null));

        deviceListView.setItems(FXCollections.observableList(deviceMonitor.getDevices()));
        deviceListView.setCellFactory(deviceListView -> new DeviceListCell());

        // Add context menu to ListView
        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeItem = new MenuItem("Remove");

        removeItem.setOnAction(event -> {
            Device selectedDevice = deviceListView.getSelectionModel().getSelectedItem();
            if (selectedDevice != null) {
                deviceMonitor.removeDevice(selectedDevice);
                refreshListView();

            }
        });

        contextMenu.getItems().addAll(removeItem);
        deviceListView.setContextMenu(contextMenu);

    }

    private class DataUpdateTask extends TimerTask {
        private Random random = new Random();

        @Override
        public void run() {
            refreshListView();
        }
    }

    @FXML
    private void addDevice(ActionEvent e) throws IOException {
//        if (e.getSource().equals(addHardwareDeviceButton)){
//            Dialog<ButtonType> dialog = new Dialog<>();
//            dialog.initOwner(mainPane.getScene().getWindow());
//
//            Parent root = FXMLLoader.load(getClass().getResource("new_hardware_device.fxml"));
//            dialog.getDialogPane().setContent(root);
//            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
//            Optional<ButtonType> result = dialog.showAndWait();
//            if (result.isPresent() && result.get() == ButtonType.OK){
//                System.out.println("ok");
//            }
//
//        } else if (e.getSource().equals(addSoftwareDeviceButton)){
//            System.out.println("c");
//
//        }
        String deviceName = deviceNameTextField.getText();
        Device newDevice = new GeneralDevice(deviceName);
        deviceMonitor.addDevice(newDevice);
        deviceNameTextField.clear();
        refreshListView();
    }

    public void refreshListView() {
        deviceListView.refresh();
    }

    private class DeviceListCell extends ListCell<Device> {
        @Override
        protected void updateItem(Device device, boolean empty) {
            super.updateItem(device, empty);

            if (device == null || empty) {
                setText(null);
                setGraphic(null);
                setStyle(""); // Reset the cell style
            } else {
                setText(device.getName() + " - " + device.getStatus());

                // Set the cell style based on the device status
                if (device.getStatus().equals("Online")) {
                    setStyle("-fx-text-fill: green;");
                } else if (device.getStatus().equals("Offline")) {
                    setStyle("-fx-text-fill: red;");
                } else {
                    setStyle(""); // Reset the cell style
                }
            }
        }
    }
    public void loadDevices() throws IOException, ClassNotFoundException {
        ObjectInputStream locFile = new ObjectInputStream( new BufferedInputStream(new FileInputStream(filename)));
        boolean eof = false;
        while (!eof){
            try {
                Device dev = (Device) locFile.readObject();
                deviceMonitor.addDevice(dev);
            } catch (EOFException e){
                eof = true;
            }

        }
    }
    public void saveDevices() throws IOException, ClassNotFoundException {
        ObjectOutputStream locFile = new ObjectOutputStream( new BufferedOutputStream(new FileOutputStream(filename)));
        for( Device dev : deviceMonitor.getDevices()){
            locFile.writeObject(dev);
        }
    }
}
