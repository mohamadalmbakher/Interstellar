/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.sql.rowset.CachedRowSet;

 @ManagedBean(name = "Statistics", eager = true)
@RequestScoped
public class Statistics {
    int sub_count;
    int clients_count;
    int users_count;

    public int getSub_count() {
        return sub_count;
    }

    public void setSub_count(int sub_count) {
        this.sub_count = sub_count;
    }

    public int getClients_count() {
        return clients_count;
    }

    public void setClients_count(int clients_count) {
        this.clients_count = clients_count;
    }

    public int getUsers_count() {
        return users_count;
    }

    public void setUsers_count(int users_count) {
        this.users_count = users_count;
    }

    public Statistics() {
    }
    
    
    public  String getStatisticsForAdmin() throws SQLException, ClassNotFoundException {
        Connection conn = DbConnection.getConnection();
        String statisticsquarey = "SELECT(SELECT COUNT(*) FROM SUBSCRIPTIONS) AS SUB_COUNT," 
             +" (SELECT COUNT(*) FROM   CLIENTS INNER JOIN USERS ON CLIENTS.USERID=USERS.ID WHERE USERS.\"LEVEL\"=1"
             + " ) AS CLIENTS_COUNT,"
             + " (SELECT COUNT(*)"
             + " FROM   USERS"
             + " ) AS USERS_COUNT "
             + "FROM SUBSCRIPTIONS OFFSET 0 ROWS "
             + "FETCH NEXT 1 ROWS ONLY";
     Statement stat = conn.createStatement();
        ResultSet rsstatistics = stat.executeQuery(statisticsquarey);
         if (rsstatistics.next()) {
             this.sub_count=rsstatistics.getInt("SUB_COUNT");
             this.clients_count=rsstatistics.getInt("CLIENTS_COUNT");
             this.users_count=rsstatistics.getInt("USERS_COUNT");
         }
         return "";
 }
}
