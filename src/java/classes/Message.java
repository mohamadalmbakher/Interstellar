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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "Message", eager = true)
@RequestScoped
public class Message {

    int id;
    String fullName;
    String email;
    String phone;
    String message;
    String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message() {
    }

    public Message(int id, String fullName, String email, String phone, String message) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.message = message;
    }

    public List getAllMessages() throws SQLException, ClassNotFoundException {
        List ll = new LinkedList();
        Connection conn = DbConnection.getConnection();
        String selectquarey = "SELECT * from MESSAGES ORDER BY createdAt desc";
        Statement stat = conn.createStatement();
        ResultSet rsUser = stat.executeQuery(selectquarey);
        while (rsUser.next()) {
            Message m = new Message();
            m.id = rsUser.getInt("id");
            m.fullName = rsUser.getString("fullname");
            m.email = rsUser.getString("email");
            m.phone = rsUser.getString("phone");
            m.message = rsUser.getString("message");
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Timestamp input = rsUser.getTimestamp("createdAt");
            m.createdAt = df.format(input);
            ll.add(m);
        }

        return ll;
    }

    public String insertMessage() throws SQLException, ClassNotFoundException {
        if (this.getFullName() != null && this.getEmail() != null) {
            Connection conn = DbConnection.getConnection();

            String selectquarey = "INSERT INTO messages(fullname,email,phone,message) VALUES('" + this.getFullName() + "','" + this.email + "','" + this.phone + "','" + this.message + "')";

            Statement stat = conn.createStatement();

            stat.executeUpdate(selectquarey, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stat.getGeneratedKeys();
            if (rs.next()) {
                return "<p id=\"outputResulte\" class=\"success_message\">The message was sent successful!</p>";
            } else {
                return "<p id=\"outputResulte\" class=\"error_message\">There is an error please try again later</p>";
            }
        } else {
            return "";

        }
    }

    public String deleteMessage(int mId) throws SQLException, ClassNotFoundException {

        Connection conn = DbConnection.getConnection();

        PreparedStatement st = conn.prepareStatement("DELETE FROM messages WHERE id= ?");
        st.setInt(1, mId);
        st.executeUpdate();
        String selectquarey = "SELECT * from MESSAGES WHERE id=" + mId;
        Statement stat = conn.createStatement();
        ResultSet rsMessage = stat.executeQuery(selectquarey);
        if (rsMessage.next()) {
            return "";
        } else {
            return "";
        }
    }
}
