package controller;

import model.BillModel;
import model.BillModel.BillRecord;
import view.BillingView;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class BillingController {

    private final BillModel model;
    private final BillingView view;

    public BillingController(Stage stage) {
        this.model = new BillModel();
        this.view = new BillingView(stage);

        view.showLoginScene();
        initLoginController();
    }

    private void initLoginController() {
        if (view.getLoginButton() != null) {
            view.getLoginButton().setOnAction(e -> handleLogin());
        }
        if (view.getRegisterButton() != null) {
            view.getRegisterButton().setOnAction(e -> handleRegister());
        }
    }

    private void handleLogin() {
        String username = view.getUserField().getText();
        String password = view.getPassField().getText();

        if (model.validateCredentials(username, password)) {
            view.showMainDashboard(model.getBillRecords());
            bindDashboardActions();
        } else {
            view.getErrorLabel().setText(model.getAuthMessage());
        }
    }

    private void handleRegister() {
        String username = view.getUserField().getText();
        String password = view.getPassField().getText();

        model.registerUser(username, password);
        view.getErrorLabel().setText(model.getAuthMessage());
    }

    private void bindDashboardActions() {
        if (view.getCalculateBtn() != null) {
            view.getCalculateBtn().setOnAction(e -> performCalculation(false));
            view.getSaveBillBtn().setOnAction(e -> performCalculation(true));
            view.getNewGuestBtn().setOnAction(e -> clearForm());
            view.getDeleteBillBtn().setOnAction(e -> handleDeleteBill());
            view.getViewReceiptBtn().setOnAction(e -> handleViewReceipt());
            view.getLogoutBtn().setOnAction(e -> handleLogout());
        }
    }

    private void performCalculation(boolean saveToTable) {
        try {
            String guestName = view.getGuestNameField().getText();
            String roomType = view.getRoomTypeCombo().getValue();
            int nights = Integer.parseInt(view.getNightsField().getText());
            double extras = Double.parseDouble(view.getExtrasField().getText());
            double amountPaid = Double.parseDouble(view.getAmountPaidField().getText());

            if (guestName.isEmpty()) {
                showAlert("Error", "Please enter a guest name.");
                return;
            }

            if (saveToTable) {
                BillRecord record = model.createBill(guestName, roomType, nights, extras, amountPaid);
                view.getStatusLabel().setText("Saved " + record.getBillId() + ": Balance: " + record.getBalance());
                view.getTableView().scrollTo(record);
                clearForm();
            } else {
                double rate = roomType.equals("Standard") ? 350 : roomType.equals("Deluxe") ? 550 : 750;
                double subtotal = rate * nights;
                if (nights >= 5) subtotal *= 0.95;
                double total = subtotal + extras;
                double balance = total - amountPaid;

                view.getStatusLabel().setText(String.format("Calculated Total: K %.2f | Balance: K %.2f", total, balance));
            }

        } catch (NumberFormatException ex) {
            showAlert("Input Error", "Please ensure Nights, Extras, and Amount Paid are valid numbers.");
        }
    }

    private void handleDeleteBill() {
        BillRecord selectedBill = view.getTableView().getSelectionModel().getSelectedItem();
        if (selectedBill != null) {
            model.deleteBill(selectedBill);
            view.getStatusLabel().setText("Deleted bill " + selectedBill.getBillId());
        } else {
            showAlert("Selection Error", "Please select a bill from the table to delete.");
        }
    }

    private void handleViewReceipt() {
        BillRecord selectedBill = view.getTableView().getSelectionModel().getSelectedItem();
        if (selectedBill != null) {
            String receiptDetails = String.format(
                    "--- LODGE BILLING LAB RECEIPT ---\n\n" +
                            "Bill ID: %s\n" +
                            "Guest Name: %s\n" +
                            "Room Type: %s\n" +
                            "Nights Stayed: %d\n" +
                            "-----------------------------------------\n" +
                            "Total Amount: %s\n" +
                            "Amount Paid: %s\n" +
                            "Balance Due: %s\n\n" +
                            "Mulungushi University - ICT261 Lab",
                    selectedBill.getBillId(),
                    selectedBill.getGuestName(),
                    selectedBill.getRoomType(),
                    selectedBill.getNights(),
                    selectedBill.getTotal(),
                    selectedBill.getPaid(),
                    selectedBill.getBalance()
            );

            Alert receiptAlert = new Alert(Alert.AlertType.INFORMATION);
            receiptAlert.setTitle("Official Receipt - " + selectedBill.getBillId());
            receiptAlert.setHeaderText("Transaction Receipt for " + selectedBill.getGuestName());
            receiptAlert.setContentText(receiptDetails);
            receiptAlert.showAndWait();
        } else {
            showAlert("Selection Error", "Please select a bill from the table to view its receipt.");
        }
    }

    private void handleLogout() {
        view.showLoginScene();
        initLoginController();
    }

    private void clearForm() {
        view.getGuestNameField().clear();
        view.getNightsField().setText("1");
        view.getExtrasField().setText("0");
        view.getAmountPaidField().setText("0");
        view.getNotesArea().clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}