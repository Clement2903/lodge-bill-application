package view;

import model.BillModel.BillRecord;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BillingView {

    private final Stage stage;
    private TextField userField;
    private PasswordField passField;
    private Button loginButton;
    private Label errorLabel;

    private TextField guestNameField;
    private ComboBox<String> roomTypeCombo;
    private ComboBox<String> locationCombo;
    private TextField nightsField;
    private TextField extrasField;
    private TextField amountPaidField;
    private TextArea notesArea;

    private Button calculateBtn;
    private Button saveBillBtn;
    private Button newGuestBtn;
    private Button deleteBillBtn;
    private Button viewReceiptBtn;
    private Button logoutBtn;
    private Label statusLabel;

    private TableView<BillRecord> tableView;

    public BillingView(Stage stage) {
        this.stage = stage;
    }

    public void showLoginScene() {
        VBox loginLayout = new VBox(15);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(40));
        loginLayout.setStyle("-fx-background-color: #0A192F;");

        Label titleLabel = new Label("Lodge Billing Lab");
        titleLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 26px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("Login (New accounts register automatically)");
        subtitleLabel.setStyle("-fx-text-fill: #A0AEC0; -fx-font-size: 13px;");

        userField = new TextField();
        userField.setPromptText("Username");
        userField.setMaxWidth(250);
        userField.setStyle("-fx-background-color: #172A45; -fx-text-fill: white; -fx-prompt-text-fill: #718096; -fx-border-color: #FFD700; -fx-border-radius: 4;");

        passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMaxWidth(250);
        passField.setStyle("-fx-background-color: #172A45; -fx-text-fill: white; -fx-prompt-text-fill: #718096; -fx-border-color: #FFD700; -fx-border-radius: 4;");

        loginButton = new Button("Login / Register");
        loginButton.setMinWidth(250);
        loginButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #0A192F; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-radius: 4;");

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #E53E3E;");

        loginLayout.getChildren().addAll(titleLabel, subtitleLabel, userField, passField, loginButton, errorLabel);
        Scene loginScene = new Scene(loginLayout, 500, 400);
        stage.setScene(loginScene);
        stage.setTitle("Login - Lodge Billing System");
        stage.show();
    }

    public void showMainDashboard(javafx.collections.ObservableList<BillRecord> records) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F8FAFC;");

        HBox topHeader = new HBox(15);
        topHeader.setAlignment(Pos.CENTER_LEFT);
        topHeader.setPadding(new Insets(12, 20, 12, 20));
        topHeader.setStyle("-fx-background-color: #0A192F;");

        Label appTitle = new Label("Lodge Billing Lab");
        appTitle.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 20px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        logoutBtn = createHeaderMenuButton("Logout ⎋");
        topHeader.getChildren().addAll(appTitle, spacer, logoutBtn);

        HBox toolbar = new HBox(15);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(10, 20, 10, 20));
        toolbar.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-width: 0 0 1 0;");

        newGuestBtn = new Button("👤 New guest");
        viewReceiptBtn = new Button("📄 View receipt");
        deleteBillBtn = new Button("🗑 Delete selected bill");
        styleToolbarButton(newGuestBtn);
        styleToolbarButton(viewReceiptBtn);
        styleToolbarButton(deleteBillBtn);
        toolbar.getChildren().addAll(newGuestBtn, viewReceiptBtn, deleteBillBtn);

        HBox centerContent = new HBox(20);
        centerContent.setPadding(new Insets(20));

        VBox formContainer = new VBox(12);
        formContainer.setPrefWidth(600);

        Label sectionTitle = new Label("Lodge Billing Lab");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0A192F;");

        Label metaLabel = new Label("ICT261 Lecture 3  |  Mr E Nyirenda  |  Mulungushi University");
        metaLabel.setStyle("-fx-text-fill: #4A5568; -fx-font-size: 12px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(12);

        guestNameField = new TextField("John Phiri");
        roomTypeCombo = new ComboBox<>(FXCollections.observableArrayList("Standard", "Deluxe", "Family"));
        roomTypeCombo.setValue("Deluxe");
        roomTypeCombo.setMaxWidth(Double.MAX_VALUE);

        locationCombo = new ComboBox<>(FXCollections.observableArrayList("Main Campus Lodge - Kabwe", "Town Center Branch", "Great North Road Annex"));
        locationCombo.setValue("Main Campus Lodge - Kabwe");
        locationCombo.setMaxWidth(Double.MAX_VALUE);

        nightsField = new TextField("5");
        extrasField = new TextField("200");
        amountPaidField = new TextField("1000");
        notesArea = new TextArea();
        notesArea.setPrefRowCount(2);

        addFormRow(formGrid, 0, "Guest name:", guestNameField);
        addFormRow(formGrid, 1, "Location:", locationCombo);
        addFormRow(formGrid, 2, "Room type:", roomTypeCombo);
        addFormRow(formGrid, 3, "Nights:", nightsField);
        addFormRow(formGrid, 4, "Extras in K:", extrasField);
        addFormRow(formGrid, 5, "Amount paid in K:", amountPaidField);
        addFormRow(formGrid, 6, "Notes:", notesArea);

        HBox formButtons = new HBox(10);
        calculateBtn = new Button("Calculate");
        saveBillBtn = new Button("Save bill");
        calculateBtn.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #0A192F; -fx-font-weight: bold;");
        saveBillBtn.setStyle("-fx-background-color: #2B6CB0; -fx-text-fill: white; -fx-font-weight: bold;");
        formButtons.getChildren().addAll(calculateBtn, saveBillBtn);

        formContainer.getChildren().addAll(sectionTitle, metaLabel, new Separator(), formGrid, formButtons);

        VBox infoCard = new VBox(10);
        infoCard.setPadding(new Insets(15));
        infoCard.setPrefWidth(320);
        infoCard.setStyle("-fx-background-color: #EDF2F7; -fx-border-color: #CBD5E0; -fx-border-radius: 6; -fx-background-radius: 6;");

        Label cardTitle = new Label("Practice room prices");
        cardTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #0A192F;");

        Label pricesText = new Label(
                "Standard:  K 350.00 per night\n" +
                        "Deluxe:  K 550.00 per night\n" +
                        "Family:  K 750.00 per night\n\n" +
                        "---------------------------------------------------\n" +
                        "5 nights or more: 5% off the room charge.\n\n" +
                        "Extras get no discount. No tax in this lab.\n\n" +
                        "Saved bills last only until you close the app."
        );
        pricesText.setStyle("-fx-text-fill: #2D3748; -fx-font-size: 12px;");
        pricesText.setWrapText(true);
        infoCard.getChildren().addAll(cardTitle, pricesText);

        centerContent.getChildren().addAll(formContainer, infoCard);

        VBox tableContainer = new VBox(10);
        tableContainer.setPadding(new Insets(0, 20, 20, 20));

        HBox tableHeaderBox = new HBox(15);
        Label savedBillsLabel = new Label("Saved bills");
        savedBillsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0A192F;");

        statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: #2F855A; -fx-font-weight: bold;");
        tableHeaderBox.getChildren().addAll(savedBillsLabel, statusLabel);

        tableView = new TableView<>();
        tableView.setItems(records);
        tableView.setPrefHeight(150);

        TableColumn<BillRecord, String> colBill = new TableColumn<>("Bill");
        colBill.setCellValueFactory(new PropertyValueFactory<>("billId"));
        colBill.setPrefWidth(90);

        TableColumn<BillRecord, String> colGuest = new TableColumn<>("Guest");
        colGuest.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        colGuest.setPrefWidth(160);

        TableColumn<BillRecord, String> colRoom = new TableColumn<>("Room");
        colRoom.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colRoom.setPrefWidth(120);

        TableColumn<BillRecord, Integer> colNights = new TableColumn<>("Nights");
        colNights.setCellValueFactory(new PropertyValueFactory<>("nights"));
        colNights.setPrefWidth(90);

        TableColumn<BillRecord, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colTotal.setPrefWidth(130);

        TableColumn<BillRecord, String> colPaid = new TableColumn<>("Paid");
        colPaid.setCellValueFactory(new PropertyValueFactory<>("paid"));
        colPaid.setPrefWidth(130);

        TableColumn<BillRecord, String> colBalance = new TableColumn<>("Balance");
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colBalance.setPrefWidth(130);

        tableView.getColumns().addAll(colBill, colGuest, colRoom, colNights, colTotal, colPaid, colBalance);
        tableContainer.getChildren().addAll(tableHeaderBox, tableView);

        VBox topContainer = new VBox(topHeader, toolbar);
        root.setTop(topContainer);
        root.setCenter(centerContent);
        root.setBottom(tableContainer);

        Scene mainScene = new Scene(root, 980, 720);
        stage.setScene(mainScene);
        stage.setTitle("Lodge Billing System - Mulungushi University");
    }

    public Button getLoginButton() { return loginButton; }
    public TextField getUserField() { return userField; }
    public PasswordField getPassField() { return passField; }
    public Label getErrorLabel() { return errorLabel; }

    public Button getCalculateBtn() { return calculateBtn; }
    public Button getSaveBillBtn() { return saveBillBtn; }
    public Button getNewGuestBtn() { return newGuestBtn; }
    public Button getDeleteBillBtn() { return deleteBillBtn; }
    public Button getViewReceiptBtn() { return viewReceiptBtn; }
    public Button getLogoutBtn() { return logoutBtn; }

    public TextField getGuestNameField() { return guestNameField; }
    public ComboBox<String> getRoomTypeCombo() { return roomTypeCombo; }
    public ComboBox<String> getLocationCombo() { return locationCombo; }
    public TextField getNightsField() { return nightsField; }
    public TextField getExtrasField() { return extrasField; }
    public TextField getAmountPaidField() { return amountPaidField; }
    public TextArea getNotesArea() { return notesArea; }

    public TableView<BillRecord> getTableView() { return tableView; }
    public Label getStatusLabel() { return statusLabel; }

    private void addFormRow(GridPane grid, int row, String labelText, javafx.scene.Node inputField) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: #2D3748; -fx-font-weight: bold;");
        grid.add(label, 0, row);
        grid.add(inputField, 1, row);
    }

    private Button createHeaderMenuButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #172A45; -fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: #FFD700; -fx-border-radius: 4;");
        return btn;
    }

    private void styleToolbarButton(Button btn) {
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #2B6CB0; -fx-font-weight: bold; -fx-cursor: hand;");
    }
}