/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import com.helper.UploadHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.Part;
import javax.sql.rowset.CachedRowSet;

 
@ManagedBean(name = "Client", eager = true)
@RequestScoped
public class Client extends Users {

    Part imageFile;
    int ClienttId;
    int userId;
    String phone;
    String image;
    ResultSet Subscriptions;

    public int getUserId() {
        return userId;
    }

    public Part getImageFile() {
        return imageFile;
    }

    public void setImageFile(Part imageFile) {
        this.imageFile = imageFile;
    }

 
    public ResultSet getSubscriptions() {
        return Subscriptions;
    }

    public void setSubscriptions(ResultSet Subscriptions) {
        this.Subscriptions = Subscriptions;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getClienttId() {
        return ClienttId;
    }

    public void setClienttId(int ClienttId) {
        this.ClienttId = ClienttId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Client(int ClienttId, int userId, String phone, String image, String username, String firstname, String lastname, String email, String password, String confilrmPassword, int level) {
        super(username, firstname, lastname, email, password, confilrmPassword, level);
        this.ClienttId = ClienttId;
        this.userId = userId;
        this.phone = phone;
        this.image = image;
    }

    public Client() {
        super();
    }
  
    public ResultSet getAllClients() throws SQLException, ClassNotFoundException {
        Connection conn = DbConnection.getConnection();
        String selectquarey = "SELECT CLIENTS.*,USERS.FIRSTNAME,USERS.LASTNAME,USERS.EMAIL,USERS.USERNAME from CLIENTS "
                + "inner join users on users.id=clients.userId "
                + "WHERE USERS.LEVEL!=0";
        Statement stat = conn.createStatement();
        ResultSet rsUser = stat.executeQuery(selectquarey);
        CachedRowSet rowSet = null;

        rowSet = new com.sun.rowset.CachedRowSetImpl();
        rowSet.populate(rsUser);
        return rowSet;
    }

    public void getClientById(ActionEvent event) throws SQLException, ClassNotFoundException {

        ClienttId = (int) event.getComponent().getAttributes().get("clienttId");

        Connection conn = DbConnection.getConnection();
        String selectquarey
                = "SELECT CLIENTS.*,USERS.FIRSTNAME,USERS.LASTNAME,USERS.EMAIL,USERS.USERNAME from CLIENTS "
                + "inner join users on users.id=clients.userId"
                + " WHERE Clients.id =" + ClienttId;
        Statement stat = conn.createStatement();
        ResultSet rsClient = stat.executeQuery(selectquarey);
        if (rsClient.next()) {
            firstname = rsClient.getString("FIRSTNAME");
            lastname = rsClient.getString("LASTNAME");
            email = rsClient.getString("EMAIL");
            username = rsClient.getString("USERNAME");
            phone = rsClient.getString("phone");
            image = rsClient.getString("image");
        }

    }

    public void goToEditProfilePage() throws SQLException, ClassNotFoundException {
        id = CurrentUser.clientId;
        firstname = CurrentUser.fName;
        lastname = CurrentUser.lName;
        email = CurrentUser.email;
        username = CurrentUser.username;
        phone = CurrentUser.phone;

    }

    public void editClient() throws SQLException, ClassNotFoundException {
         UploadHelper uploadHelper = new UploadHelper();
        this.image = uploadHelper.processUpload(this.imageFile);
        Connection conn = DbConnection.getConnection();
        if (this.confilrmPassword.equals(this.password)) {

            if ("".equals(this.password)) {
                PreparedStatement stUser = conn.prepareStatement("UPDATE users SET firstname=?, lastname=?,email=? WHERE id=?");
                stUser.setString(1, this.firstname);
                stUser.setString(2, this.lastname);
                stUser.setString(3, this.email);
                stUser.setInt(4, CurrentUser.id);
                stUser.executeUpdate();
            } else {
                PreparedStatement stUser = conn.prepareStatement("UPDATE users SET firstname=?, lastname=?,email=?,password=? WHERE id=?");
                stUser.setString(1, this.firstname);
                stUser.setString(2, this.lastname);
                stUser.setString(3, this.email);
                stUser.setString(4, this.password);
                stUser.setInt(5, CurrentUser.id);
                stUser.executeUpdate();
            }
            String stImage = "";
            if (!"1.jpg".equals(this.image)) {
                stImage = ",image=?";
            }

            PreparedStatement stClient = conn.prepareStatement("UPDATE clients SET phone=?" + stImage + " WHERE id=?");
            stClient.setString(1, this.phone);

            if (!"1.jpg".equals(this.image)) {
                stClient.setString(2, this.image);
                stClient.setInt(3, CurrentUser.clientId);
                CurrentUser.image = this.image;

            } else {
                stClient.setInt(2, CurrentUser.clientId);
            }
            stClient.executeUpdate();
            CurrentUser.setfName(this.firstname);
            CurrentUser.setlName(this.lastname);
            CurrentUser.email = this.email;
            CurrentUser.phone = this.phone;
        }
    }

    public int insertClient() throws SQLException, ClassNotFoundException {
        UploadHelper uploadHelper = new UploadHelper();
        this.image = uploadHelper.processUpload(this.imageFile);
        Connection conn = DbConnection.getConnection();
        int insertedId = super.insertUser();
        this.userId = insertedId;
        String selectquarey = "INSERT INTO clients(userId,phone,image) VALUES(" + this.userId + ",'" + this.phone + "','" + this.image + "')";

        Statement stat = conn.createStatement();

        stat.executeUpdate(selectquarey, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stat.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            return 0;
        }
    }

    public String registerNewClient() throws SQLException, ClassNotFoundException {

        if (super.getFirstname() != null && Users.emailValid(super.getEmail()) == true && super.getUsername() != null && super.checkUsernameIfExist() == "" && super.getPassword() != null && super.getConfilrmPassword() != null && super.getPassword().equals(super.getConfilrmPassword())) {
            if (insertClient() != 0) {
                return "<p id=\"outputResulte\" style=\"display: none\">true</p>";
            } else {
                return "<p id=\"outputResulte\" class=\"error_message\">An error occurred during registration, please try again later</p>";
            }
        } else if (super.getEmail() == null) {
            return "";
        } else {
            String res = "<p id=\"outputResulte\" class=\"error_message\">Error: ";
            if (Users.emailValid(super.getEmail()) == false) {
                res += "<br> You should enter a valid email";
            }
            if (super.checkUsernameIfExist() != "") {
                res += "<br> The username already exists try another";

            }
            if (!super.getPassword().equals(super.getConfilrmPassword())) {
                res += "<br> The passwords do not match ";
            }
            res += "</p>";
            return res;
        }

    }
}
