package view;

import model.BillModel.BillRecord;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private Timeline landscapeAnimation;

    public BillingView(Stage stage) {
        this.stage = stage;
    }

    public void showLoginScene() {
        VBox loginLayout = new VBox(15);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(40));
        loginLayout.setMaxWidth(390);
        loginLayout.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, rgba(23,42,69,0.94), rgba(10,25,47,0.84));" +
                        "-fx-background-radius: 24;" +
                        "-fx-border-color: rgba(255,215,0,0.58);" +
                        "-fx-border-radius: 24;" +
                        "-fx-border-width: 1.2;"
        );
        loginLayout.setEffect(new DropShadow(30, Color.rgb(0, 0, 0, 0.32)));

        Label titleLabel = new Label("Lodge Billing Lab");
        titleLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 26px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("Login (New accounts register automatically)");
        subtitleLabel.setStyle("-fx-text-fill: #A0AEC0; -fx-font-size: 13px;");

        userField = new TextField();
        userField.setPromptText("Username");
        userField.setMaxWidth(Double.MAX_VALUE);
        styleLoginInput(userField);

        passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMaxWidth(Double.MAX_VALUE);
        styleLoginInput(passField);

        loginButton = new Button("Login / Register");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #FFE566, #FFD700);" +
                        "-fx-text-fill: #0A192F; -fx-font-weight: bold; -fx-cursor: hand;" +
                        "-fx-background-radius: 12; -fx-border-radius: 12; -fx-padding: 11 18;"
        );

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #E53E3E;");

        loginLayout.getChildren().addAll(titleLabel, subtitleLabel, userField, passField, loginButton, errorLabel);
        StackPane loginRoot = new StackPane();
        Canvas landscape = createLandscape(loginRoot, true);
        loginRoot.getChildren().addAll(landscape, loginLayout);
        loginRoot.setPadding(new Insets(32));
        Scene loginScene = new Scene(loginRoot, 500, 460);
        animateEntrance(loginLayout, 0);
        animateEntrance(titleLabel, 80);
        animateEntrance(subtitleLabel, 130);
        animateEntrance(userField, 180);
        animateEntrance(passField, 230);
        animateEntrance(loginButton, 280);
        animateEntrance(errorLabel, 330);
        installMotion(loginLayout);
        stage.setScene(loginScene);
        stage.setTitle("Login - Lodge Billing System");
        stage.show();
    }

    public void showMainDashboard(javafx.collections.ObservableList<BillRecord> records) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: transparent;");

        HBox topHeader = new HBox(15);
        topHeader.setAlignment(Pos.CENTER_LEFT);
        topHeader.setPadding(new Insets(12, 20, 12, 20));
        topHeader.setStyle(
                "-fx-background-color: linear-gradient(to right, rgba(10,25,47,0.98), rgba(23,42,69,0.94));" +
                        "-fx-border-color: rgba(255,255,255,0.16); -fx-border-width: 0 0 1 0;"
        );

        Label appTitle = new Label("Lodge Billing Lab");
        appTitle.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 20px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        logoutBtn = createHeaderMenuButton("Logout ⎋");
        topHeader.getChildren().addAll(appTitle, spacer, logoutBtn);

        FlowPane toolbar = new FlowPane(15, 10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(10, 20, 10, 20));
        toolbar.setStyle(
                "-fx-background-color: rgba(255,255,255,0.72);" +
                        "-fx-border-color: rgba(203,213,224,0.72); -fx-border-width: 0 0 1 0;"
        );

        newGuestBtn = new Button("👤 New guest");
        viewReceiptBtn = new Button("📄 View receipt");
        deleteBillBtn = new Button("🗑 Delete selected bill");
        styleToolbarButton(newGuestBtn);
        styleToolbarButton(viewReceiptBtn);
        styleToolbarButton(deleteBillBtn);
        toolbar.getChildren().addAll(newGuestBtn, viewReceiptBtn, deleteBillBtn);

        VBox centerContent = new VBox(16);
        centerContent.setPadding(new Insets(20));
        centerContent.setFillWidth(true);

        HBox formInfoRow = new HBox(20);
        formInfoRow.setFillHeight(true);

        VBox formContainer = new VBox(12);
        formContainer.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(formContainer, Priority.ALWAYS);
        styleGlassPanel(formContainer);

        Label sectionTitle = new Label("Lodge Billing Lab");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0A192F;");

        Label metaLabel = new Label("ICT261 Lecture 3  |  Mr E Nyirenda  |  Mulungushi University");
        metaLabel.setStyle("-fx-text-fill: #4A5568; -fx-font-size: 12px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(12);
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setPercentWidth(30);
        ColumnConstraints inputColumn = new ColumnConstraints();
        inputColumn.setPercentWidth(70);
        formGrid.getColumnConstraints().addAll(labelColumn, inputColumn);

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
        styleDashboardInput(guestNameField);
        styleDashboardInput(roomTypeCombo);
        styleDashboardInput(locationCombo);
        styleDashboardInput(nightsField);
        styleDashboardInput(extrasField);
        styleDashboardInput(amountPaidField);
        styleDashboardInput(notesArea);

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
        calculateBtn.setStyle(
                "-fx-background-color: rgba(226,232,240,0.88); -fx-text-fill: #0A192F;" +
                        "-fx-font-weight: bold; -fx-background-radius: 10; -fx-border-radius: 10;" +
                        "-fx-border-color: rgba(203,213,224,0.9); -fx-padding: 9 16;"
        );
        saveBillBtn.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #4299E1, #2B6CB0);" +
                        "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;" +
                        "-fx-border-radius: 10; -fx-border-color: rgba(255,255,255,0.45); -fx-padding: 9 16;"
        );
        formButtons.getChildren().addAll(calculateBtn, saveBillBtn);

        formContainer.getChildren().addAll(sectionTitle, metaLabel, new Separator(), formGrid, formButtons);

        VBox infoCard = new VBox(10);
        infoCard.setPadding(new Insets(15));
        infoCard.setMinWidth(0);
        infoCard.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(infoCard, Priority.ALWAYS);
        styleGlassPanel(infoCard);

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

        formInfoRow.getChildren().addAll(formContainer, infoCard);

        VBox tableContainer = new VBox(10);
        tableContainer.setPadding(new Insets(0, 20, 20, 20));
        tableContainer.setStyle(
                "-fx-background-color: rgba(255,255,255,0.58);" +
                        "-fx-background-radius: 18 18 0 0;" +
                        "-fx-border-color: rgba(203,213,224,0.7); -fx-border-radius: 18 18 0 0;" +
                        "-fx-border-width: 1 1 0 1;"
        );

        HBox tableHeaderBox = new HBox(15);
        Label savedBillsLabel = new Label("Saved bills");
        savedBillsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0A192F;");

        statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: #2F855A; -fx-font-weight: bold;");
        tableHeaderBox.getChildren().addAll(savedBillsLabel, statusLabel);

        tableView = new TableView<>();
        tableView.setItems(records);
        tableView.setPrefHeight(180);
        tableView.setMinHeight(120);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setStyle(
                "-fx-background-color: rgba(255,255,255,0.78);" +
                        "-fx-background-radius: 12; -fx-border-color: rgba(203,213,224,0.8);" +
                        "-fx-border-radius: 12; -fx-padding: 4;"
        );

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

        centerContent.getChildren().addAll(formInfoRow, tableContainer);

        ScrollPane dashboardScroll = new ScrollPane(centerContent);
        dashboardScroll.setFitToWidth(true);
        dashboardScroll.setFitToHeight(false);
        dashboardScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        dashboardScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox topContainer = new VBox(topHeader, toolbar);
        root.setTop(topContainer);
        root.setCenter(dashboardScroll);

        StackPane dashboardRoot = new StackPane();
        Canvas landscape = createLandscape(dashboardRoot, false);
        dashboardRoot.getChildren().addAll(landscape, root);

        Scene mainScene = new Scene(dashboardRoot, 980, 720);
        animateEntrance(topHeader, 0);
        animateEntrance(toolbar, 70);
        animateEntrance(formContainer, 140);
        animateEntrance(infoCard, 210);
        animateEntrance(tableContainer, 280);
        animateEntrance(appTitle, 100);
        animateEntrance(guestNameField, 250);
        animateEntrance(locationCombo, 285);
        animateEntrance(roomTypeCombo, 320);
        animateEntrance(nightsField, 355);
        animateEntrance(extrasField, 390);
        animateEntrance(amountPaidField, 425);
        animateEntrance(notesArea, 460);
        animateEntrance(calculateBtn, 495);
        animateEntrance(saveBillBtn, 530);
        animateEntrance(cardTitle, 300);
        animateEntrance(pricesText, 340);
        animateEntrance(savedBillsLabel, 380);
        animateEntrance(statusLabel, 415);
        animateEntrance(tableView, 450);
        installMotion(dashboardRoot);
        mainScene.widthProperty().addListener((observable, oldWidth, newWidth) -> {
            if (newWidth.doubleValue() < 780) {
                centerContent.getChildren().setAll(formContainer, infoCard, tableContainer);
            } else {
                formInfoRow.getChildren().setAll(formContainer, infoCard);
                centerContent.getChildren().setAll(formInfoRow, tableContainer);
            }
        });
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
        btn.setStyle(
                "-fx-background-color: rgba(23,42,69,0.82); -fx-text-fill: #FFD700;" +
                        "-fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: rgba(255,215,0,0.72);" +
                        "-fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 8 14;"
        );
        return btn;
    }

    private void styleToolbarButton(Button btn) {
        btn.setStyle(
                "-fx-background-color: rgba(255,255,255,0.62); -fx-text-fill: #2B6CB0;" +
                        "-fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: rgba(203,213,224,0.8);" +
                        "-fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 8 12;"
        );
    }

    private void styleGlassPanel(Region panel) {
        panel.setPadding(new Insets(18));
        panel.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, rgba(255,255,255,0.88), rgba(237,242,247,0.76));" +
                        "-fx-background-radius: 18; -fx-border-color: rgba(255,255,255,0.95);" +
                        "-fx-border-radius: 18; -fx-border-width: 1.2;"
        );
        panel.setEffect(new DropShadow(18, Color.rgb(10, 25, 47, 0.10)));
    }

    private void styleLoginInput(TextField input) {
        input.setStyle(
                "-fx-background-color: rgba(10,25,47,0.64); -fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #A0AEC0; -fx-border-color: rgba(255,215,0,0.46);" +
                        "-fx-border-radius: 11; -fx-background-radius: 11; -fx-padding: 11 13;"
        );
    }

    private void styleDashboardInput(Control input) {
        input.setStyle(
                "-fx-background-color: rgba(255,255,255,0.82); -fx-text-fill: #0A192F;" +
                        "-fx-prompt-text-fill: #718096; -fx-border-color: rgba(203,213,224,0.95);" +
                        "-fx-border-radius: 9; -fx-background-radius: 9; -fx-padding: 7 10;"
        );
    }

    private Canvas createLandscape(StackPane container, boolean night) {
        if (landscapeAnimation != null) {
            landscapeAnimation.stop();
        }

        AnimatedLandscape landscape = new AnimatedLandscape(night);
        landscape.widthProperty().bind(container.widthProperty());
        landscape.heightProperty().bind(container.heightProperty());
        landscape.widthProperty().addListener((observable, oldValue, newValue) -> landscape.redraw());
        landscape.heightProperty().addListener((observable, oldValue, newValue) -> landscape.redraw());

        landscapeAnimation = new Timeline(new KeyFrame(Duration.millis(80), event -> landscape.advance()));
        landscapeAnimation.setCycleCount(Animation.INDEFINITE);
        landscapeAnimation.play();
        return landscape;
    }

    private void animateEntrance(Node node, double delayMillis) {
        node.setOpacity(0);
        node.setTranslateY(12);

        FadeTransition fade = new FadeTransition(Duration.millis(420), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(420), node);
        slide.setFromY(12);
        slide.setToY(0);

        PauseTransition delay = new PauseTransition(Duration.millis(delayMillis));
        delay.setOnFinished(event -> new ParallelTransition(fade, slide).play());
        delay.play();
    }

    private void installMotion(Node node) {
        if (node instanceof Button || node instanceof TextField || node instanceof TextArea
                || node instanceof ComboBox<?> || node instanceof TableView<?>) {
            node.setOnMouseEntered(event -> animateScale(node, 1.018));
            node.setOnMouseExited(event -> animateScale(node, 1));
            node.setOnMousePressed(event -> animateScale(node, 0.985));
            node.setOnMouseReleased(event -> animateScale(node, node.isHover() ? 1.018 : 1));

            if (node instanceof Control) {
                ((Control) node).focusedProperty().addListener((observable, wasFocused, isFocused) ->
                        animateScale(node, isFocused ? 1.012 : (node.isHover() ? 1.018 : 1)));
            }
        }

        if (node instanceof Parent && !(node instanceof TableView<?>)) {
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                installMotion(child);
            }
        }
    }

    private void animateScale(Node node, double scale) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(160), node);
        transition.setToX(scale);
        transition.setToY(scale);
        transition.play();
    }

    private static final class AnimatedLandscape extends Canvas {
        private final boolean night;
        private double phase;

        private AnimatedLandscape(boolean night) {
            this.night = night;
            setMouseTransparent(true);
        }

        private void advance() {
            phase += 0.035;
            if (phase >= Math.PI * 2) {
                phase -= Math.PI * 2;
            }
            redraw();
        }

        private void redraw() {
            double width = getWidth();
            double height = getHeight();
            if (width <= 0 || height <= 0) {
                return;
            }

            GraphicsContext graphics = getGraphicsContext2D();
            graphics.clearRect(0, 0, width, height);
            graphics.setFill(gradient(width * 0.8, height, skyTop(), skyBottom()));
            graphics.fillRect(0, 0, width, height);

            double sunX = width * 0.78;
            double sunY = height * 0.28 + Math.sin(phase * 0.22) * height * 0.012;
            graphics.setFill(Color.rgb(255, 215, 0, night ? 0.07 : 0.11));
            graphics.fillOval(sunX - 72, sunY - 72, 144, 144);
            graphics.setFill(Color.rgb(255, 215, 0, night ? 0.16 : 0.25));
            graphics.fillOval(sunX - 43, sunY - 43, 86, 86);
            graphics.setFill(Color.rgb(255, 215, 0, night ? 0.82 : 0.94));
            graphics.fillOval(sunX - 25, sunY - 25, 50, 50);

            drawCloud(graphics, width * 0.25 + Math.sin(phase * 0.35) * width * 0.025, height * 0.22, width);
            drawCloud(graphics, width * 0.58 + Math.cos(phase * 0.28) * width * 0.018, height * 0.16, width);

            graphics.setFill(gradient(width * 0.7, height, hillBack(), hillBackBottom()));
            graphics.fillPolygon(
                    new double[]{0, width * 0.16, width * 0.31, width * 0.49, width * 0.68, width * 0.84, width, width, 0},
                    new double[]{height * 0.59, height * 0.39, height * 0.58, height * 0.35, height * 0.57, height * 0.42, height * 0.57, height, height},
                    9
            );

            graphics.setFill(gradient(width * 0.8, height, lakeTop(), lakeBottom()));
            graphics.fillRect(0, height * 0.65, width, height * 0.35);

            graphics.setStroke(Color.rgb(255, 215, 0, night ? 0.10 : 0.16));
            graphics.setLineWidth(1.2);
            for (int i = 0; i < 9; i++) {
                double y = height * (0.69 + i * 0.035);
                double offset = Math.sin(phase + i * 0.7) * width * 0.025;
                graphics.beginPath();
                graphics.moveTo(width * 0.46 + offset, y);
                graphics.bezierCurveTo(width * 0.58 + offset, y - 4, width * 0.72 + offset, y + 4, width * 0.9 + offset, y);
                graphics.stroke();
            }

            graphics.setFill(gradient(width * 0.5, height, hillFront(), hillFrontBottom()));
            graphics.fillPolygon(
                    new double[]{0, width * 0.15, width * 0.33, width * 0.5, width * 0.7, width * 0.88, width, width, 0},
                    new double[]{height * 0.73, height * 0.61, height * 0.76, height * 0.67, height * 0.78, height * 0.62, height * 0.72, height, height},
                    9
            );
        }

        private void drawCloud(GraphicsContext graphics, double x, double y, double width) {
            double wrappedX = (x + width + 120) % (width + 120) - 60;
            graphics.setFill(Color.rgb(255, 255, 255, night ? 0.08 : 0.38));
            graphics.fillOval(wrappedX, y + 8, 54, 18);
            graphics.fillOval(wrappedX + 15, y, 32, 25);
            graphics.fillOval(wrappedX + 33, y + 6, 40, 20);
        }

        private LinearGradient gradient(double endX, double endY, Color start, Color end) {
            return new LinearGradient(0, 0, endX, endY, false, CycleMethod.NO_CYCLE,
                    new Stop(0, start), new Stop(1, end));
        }

        private Color skyTop() { return night ? Color.web("#0A192F") : Color.web("#DCEAF4"); }
        private Color skyBottom() { return night ? Color.web("#2B5575") : Color.web("#F8FAFC"); }
        private Color hillBack() { return night ? Color.web("#24445E") : Color.web("#A0AEC0"); }
        private Color hillBackBottom() { return night ? Color.web("#172A45") : Color.web("#718096"); }
        private Color lakeTop() { return night ? Color.web("#234C69") : Color.web("#90B7C9"); }
        private Color lakeBottom() { return night ? Color.web("#0A192F") : Color.web("#486D85"); }
        private Color hillFront() { return night ? Color.web("#142A43") : Color.web("#344F66"); }
        private Color hillFrontBottom() { return night ? Color.web("#0A192F") : Color.web("#172A45"); }
    }
}