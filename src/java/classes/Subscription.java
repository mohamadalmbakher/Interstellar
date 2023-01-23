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
import javax.sql.rowset.CachedRowSet;

@ManagedBean(name = "Subscription", eager = true)
@RequestScoped
public class Subscription {

    int id;
    int clientid;
    String subscriptionno;
    String address;

    public Subscription() {
    }

    public Subscription(int id, int clientid, String subscriptionno, String address) {
        this.id = id;
        this.clientid = clientid;
        this.subscriptionno = subscriptionno;
        this.address = address;
    }
    public ResultSet getAllMySubscriptions() throws SQLException, ClassNotFoundException {
        CurrentUser.id=1;
    return getAllSubscriptionsForClient(CurrentUser.clientId);
    }
    
    public ResultSet getAllSubscriptionsForClient(int clientId) throws SQLException, ClassNotFoundException {
        Users u = new Users();
        Connection conn = DbConnection.getConnection();
        String subscriptionQuarey = "SELECT Subscriptions.*,BILLS.accumulated_bills AS accumulated_bills from Subscriptions"
                + " LEFT join (SELECT BILS.SUBSCRIPTIONID AS SUBSCRIPTIONID,sum(bils.value) AS accumulated_bills"
                + " FROM BILS WHERE BILS.STATUS= 'Not Paided' GROUP BY BILS.SUBSCRIPTIONID) AS BILLS"
                + " ON Subscriptions.ID = BILLS.SUBSCRIPTIONID"
                + " WHERE Subscriptions.CLIENTID=" + clientId;

        Statement stat = conn.createStatement();
        ResultSet rsSubscription = stat.executeQuery(subscriptionQuarey);
        CachedRowSet rowSet = null;

        rowSet = new com.sun.rowset.CachedRowSetImpl();
        rowSet.populate(rsSubscription);
        return rowSet;
    }

    public void deleteSubscription(int subId) throws SQLException, ClassNotFoundException {
        Connection conn = DbConnection.getConnection();
        PreparedStatement stBills = conn.prepareStatement("DELETE FROM Bils WHERE Subscriptionid= ?");
        stBills.setInt(1, subId);
        stBills.executeUpdate();
        PreparedStatement stUser = conn.prepareStatement("DELETE FROM Subscriptions WHERE id= ?");
        stUser.setInt(1, subId);
        stUser.executeUpdate();
    }
  public void goToEditPage(int subId) throws SQLException, ClassNotFoundException {
        Connection conn = DbConnection.getConnection();
        String subscriptionQuarey = "SELECT * from Subscriptions WHERE id=" + subId;

        Statement stat = conn.createStatement();
        ResultSet rsSubscription = stat.executeQuery(subscriptionQuarey);
        if (rsSubscription.next()) {
          this.address=rsSubscription.getString("address");
          this.id=subId;
          this.subscriptionno=rsSubscription.getString("subscriptionno");
      }
    }
    public static ResultSet getAllSubscriptions() throws SQLException, ClassNotFoundException {
        Connection conn = DbConnection.getConnection();
        String subscriptionQuarey = "SELECT "
                + "  Subscriptions.*, "
                + " users.firstname, "
                + " users.lastname, "
                + " BILLS.accumulated_bills AS accumulated_bills"
                + " FROM Subscriptions "
                + " inner JOIN clients "
                + "  ON Subscriptions.clientid = clients.id "
                + " inner JOIN users "
                + " ON users.id = clients.userId"
                + " LEFT join (SELECT BILS.SUBSCRIPTIONID AS SUBSCRIPTIONID,sum(bils.value) AS accumulated_bills"
                + " FROM BILS WHERE BILS.STATUS= 'Not Paided' GROUP BY BILS.SUBSCRIPTIONID) AS BILLS"
                + " ON Subscriptions.ID = BILLS.SUBSCRIPTIONID";

        Statement stat = conn.createStatement();
        ResultSet rsSubscription = stat.executeQuery(subscriptionQuarey);
        CachedRowSet rowSet = null;

        rowSet = new com.sun.rowset.CachedRowSetImpl();
        rowSet.populate(rsSubscription);
        return rowSet;
    }
    public void addNewSubscription() throws SQLException, ClassNotFoundException {
      
        if (this.address!=null) {
             Connection conn = DbConnection.getConnection();
              long subscriptionNoLong = (long) (Math.random() * 10000000000L);
        String subscriptionNo=String.valueOf(subscriptionNoLong);
        String addSubQuarey = "INSERT INTO subscriptions(clientid,SUBSCRIPTIONNO,address) VALUES(" + CurrentUser.getClientId() + ",'" + subscriptionNo + "','" + this.address + "')";

        Statement stat = conn.createStatement();

        stat.executeUpdate(addSubQuarey, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stat.getGeneratedKeys();
        
        }
        
       
    }
public void editSubscription() throws SQLException, ClassNotFoundException {
      
        if (this.address!=null) {
             Connection conn = DbConnection.getConnection();
        PreparedStatement stSubscriptions = conn.prepareStatement("UPDATE Subscriptions SET address=? WHERE id= ?");
         stSubscriptions.setString(1, this.address);
         stSubscriptions.setInt(2, this.id);
        stSubscriptions.executeUpdate();
        
        }
        
       
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientid() {
        return clientid;
    }

    public void setClientid(int clientid) {
        this.clientid = clientid;
    }

    public String getSubscriptionno() {
        return subscriptionno;
    }

    public void setSubscriptionno(String subscriptionno) {
        this.subscriptionno = subscriptionno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
