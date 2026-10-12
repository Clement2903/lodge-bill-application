package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.HashMap;
import java.util.Map;

public class BillModel {

    public static class BillRecord {
        private final String billId;
        private final String guestName;
        private final String roomType;
        private final int nights;
        private final String total;
        private final String paid;
        private final String balance;

        public BillRecord(String billId, String guestName, String roomType, int nights, String total, String paid, String balance) {
            this.billId = billId;
            this.guestName = guestName;
            this.roomType = roomType;
            this.nights = nights;
            this.total = total;
            this.paid = paid;
            this.balance = balance;
        }

        public String getBillId() { return billId; }
        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
        public int getNights() { return nights; }
        public String getTotal() { return total; }
        public String getPaid() { return paid; }
        public String getBalance() { return balance; }
    }

    private final ObservableList<BillRecord> billRecords = FXCollections.observableArrayList(
            new BillRecord("B002", "Mary Banda", "Standard", 2, "K 800.00", "K 500.00", "K 300.00"),
            new BillRecord("B003", "John Phiri", "Deluxe", 5, "K 2812.50", "K 1000.00", "K 1812.50")
    );

    private final Map<String, String> userDatabase = new HashMap<>();
    private String authMessage = "";
    private int nextBillIdNum = 4;

    public ObservableList<BillRecord> getBillRecords() {
        return billRecords;
    }

    public String getAuthMessage() {
        return authMessage;
    }

    public boolean validateCredentials(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            authMessage = "Please enter both username and password.";
            return false;
        }

        if (!userDatabase.containsKey(username)) {
            userDatabase.put(username, password);
            authMessage = "New account registered successfully!";
            return true;
        } else {
            if (userDatabase.get(username).equals(password)) {
                authMessage = "";
                return true;
            } else {
                authMessage = "Wrong password. Please try again.";
                return false;
            }
        }
    }

    public BillRecord createBill(String guestName, String roomType, int nights, double extras, double amountPaid) {
        double nightlyRate = switch (roomType) {
            case "Standard" -> 350.00;
            case "Deluxe" -> 550.00;
            case "Family" -> 750.00;
            default -> 350.00;
        };

        double roomCharge = nightlyRate * nights;
        if (nights >= 5) {
            roomCharge *= 0.95;
        }

        double totalAmount = roomCharge + extras;
        double balanceAmount = totalAmount - amountPaid;

        String billId = String.format("B%03d", nextBillIdNum++);
        String totalStr = String.format("K %.2f", totalAmount);
        String paidStr = String.format("K %.2f", amountPaid);
        String balanceStr = String.format("K %.2f", balanceAmount);

        BillRecord newRecord = new BillRecord(billId, guestName, roomType, nights, totalStr, paidStr, balanceStr);
        billRecords.add(newRecord);
        return newRecord;
    }

    public void deleteBill(BillRecord record) {
        billRecords.remove(record);
    }
}