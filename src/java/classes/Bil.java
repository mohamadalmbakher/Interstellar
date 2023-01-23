/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.sql.rowset.CachedRowSet;

@ManagedBean(name = "Bil", eager = true)
@RequestScoped
public class Bil {

    String subscriptionNo;
    int id;
    int subscriptionId;
    String status;
    String billDate;
    long value;

    public Bil(String subscriptionNo, int id, String status, String billDate, long value) {
        this.subscriptionNo = subscriptionNo;
        this.id = id;
        this.status = status;
        this.billDate = billDate;
        this.value = value;
    }

    public Bil() {
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionNo() {
        return subscriptionNo;
    }

    public void setSubscriptionNo(String subscriptionNo) {
        this.subscriptionNo = subscriptionNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public void payBill(int billId) throws SQLException, ClassNotFoundException {

        Connection conn = DbConnection.getConnection();
        PreparedStatement stBills = conn.prepareStatement("UPDATE bils SET STATUS='Paid' WHERE id=?");
        stBills.setInt(1, billId);
        stBills.executeUpdate();

    }

    public String getAccumulatedBillsForSub() throws SQLException, ClassNotFoundException {
        if (this.subscriptionNo != null) {

            Connection conn = DbConnection.getConnection();

            String selectquarey = "SELECT sum(bils.value) AS accumulated_bills "
                    + "FROM BILS WHERE BILS.STATUS= 'Not Paided' AND "
                    + "BILS.SUBSCRIPTIONID = (SELECT SUBSCRIPTIONS.ID AS SUBSCRIPTIONID FROM SUBSCRIPTIONS WHERE SUBSCRIPTIONS.SUBSCRIPTIONNO='" + this.subscriptionNo + "' OFFSET 0 ROWS "
                    + "FETCH NEXT 1 ROWS ONLY) ";
            Statement stat = conn.createStatement();
            ResultSet rsAccumulated_bills = stat.executeQuery(selectquarey);
            if (rsAccumulated_bills.next()) {
                if (rsAccumulated_bills.getLong("accumulated_bills") == 0) {
                    return "There is no any bill";
                } else {
                    return "Accumulated bills :" + String.valueOf(rsAccumulated_bills.getLong("accumulated_bills")) + " TL";
                }
            } else {
                return "There is no any bill";
            }
        } else {
            return "";
        }
    }

    public String CheckIfThereBilForSub(int subId) throws SQLException, ClassNotFoundException {

        Connection conn = DbConnection.getConnection();

        String selectquarey = "SELECT * from bils WHERE SUBSCRIPTIONID=" + subId + "AND STATUS='Not Paided'";
        Statement stat = conn.createStatement();
        ResultSet rsMessage = stat.executeQuery(selectquarey);
        if (rsMessage.next()) {
            return "true";
        } else {
            return "false";
        }
    }

    public ResultSet showAllBilsForSub(int subId) throws SQLException, ClassNotFoundException {

        Connection conn = DbConnection.getConnection();

        String selectquarey = "SELECT * from bils WHERE SUBSCRIPTIONID=" + subId;
        Statement stat = conn.createStatement();
        ResultSet rsBills = stat.executeQuery(selectquarey);
        CachedRowSet rowSet = null;

        rowSet = new com.sun.rowset.CachedRowSetImpl();
        rowSet.populate(rsBills);
        return rowSet;
    }

    public ResultSet getAllBillsForClient() throws SQLException, ClassNotFoundException {
        Connection conn = DbConnection.getConnection();
        String subscriptionQuarey = "SELECT "
                + "  bils.*, "
                + "  bils.id as billId, "
                + "  CAST(bils.CREATEDAT  AS date) as BILLCREATEDAT, "
                + "Subscriptions.SUBSCRIPTIONNO, "
                + "users.firstname, "
                + "users.lastname "
                + "FROM bils "
                + "JOIN Subscriptions "
                + "  ON bils.SUBSCRIPTIONID = Subscriptions.id "
                + "JOIN clients "
                + "  ON Subscriptions.clientid = clients.id "
                + "JOIN users "
                + "  ON users.id = clients.userId"
                + "  WHERE Subscriptions.clientid=" + CurrentUser.clientId
                + "  AND bils.status='Not Paided'"
                + "ORDER BY bils.CREATEDAT desc";

        Statement stat = conn.createStatement();
        ResultSet rsSubscription = stat.executeQuery(subscriptionQuarey);
        CachedRowSet rowSet = null;

        rowSet = new com.sun.rowset.CachedRowSetImpl();
        rowSet.populate(rsSubscription);
        return rowSet;
    }

    public String addNewBill(int subId) throws SQLException, ClassNotFoundException {

        Connection conn = DbConnection.getConnection();
        long valueLong = ((long) (Math.random() * 100L))+5;
        String selectquarey = "INSERT INTO bils(status,SUBSCRIPTIONID,value) VALUES('Not Paided'," + subId + "," + valueLong + ")";

        Statement stat = conn.createStatement();

        stat.executeUpdate(selectquarey, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stat.getGeneratedKeys();

        return "";

    }

    public String goToBillsForSub(int subId) {
        this.subscriptionId = subId;
        return "bills_for_sub";
    }

    public ResultSet getAllBillsForSub(int subId) throws SQLException, ClassNotFoundException {
        Connection conn = DbConnection.getConnection();
        String subscriptionQuarey = "SELECT "
                + "  bils.*, "
                + "  bils.id as billId, "
                + "  CAST(bils.CREATEDAT  AS date) as BILLCREATEDAT, "
                + "Subscriptions.SUBSCRIPTIONNO, "
                + "users.firstname, "
                + "users.lastname "
                + "FROM bils "
                + "JOIN Subscriptions "
                + "  ON bils.SUBSCRIPTIONID = Subscriptions.id "
                + "JOIN clients "
                + "  ON Subscriptions.clientid = clients.id "
                + "JOIN users "
                + "  ON users.id = clients.userId"
                + "  WHERE bils.SUBSCRIPTIONID=" + subId
                + "ORDER BY bils.CREATEDAT desc";

        Statement stat = conn.createStatement();
        ResultSet rsSubscription = stat.executeQuery(subscriptionQuarey);
        CachedRowSet rowSet = null;

        rowSet = new com.sun.rowset.CachedRowSetImpl();
        rowSet.populate(rsSubscription);
        return rowSet;
    }

}
