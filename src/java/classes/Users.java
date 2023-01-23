/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.sql.rowset.CachedRowSet;
 
@ManagedBean(name = "users", eager = true)
@RequestScoped
public class Users implements Serializable {

    Integer id;
    String username;
    String firstname;
    String lastname;
    String email;
    String password;
    String confilrmPassword;
    Integer level;

    public Users() {
    }

    public Users(String username, String firstname, String lastname, String email, String password, String confilrmPassword, int level) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.password = confilrmPassword;
        this.level = level;
    }

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ResultSet getAllUsers() throws SQLException, ClassNotFoundException {
        ArrayList<Users> result = null;
        Users u = new Users();
        Connection conn = DbConnection.getConnection();
        String selectquarey = "SELECT * from USERS";
        Statement stat = conn.createStatement();
        ResultSet rsUser = stat.executeQuery(selectquarey);
        CachedRowSet rowSet = null;

        rowSet = new com.sun.rowset.CachedRowSetImpl();
        rowSet.populate(rsUser);
        return rowSet;
    }

    public String userLogin() throws SQLException, ClassNotFoundException {
        Connection conn = DbConnection.getConnection();
        FacesContext context = FacesContext.getCurrentInstance();

        String selectquarey = "SELECT * from USERS WHERE USERNAME='" + getUsername() + "' AND PASSWORD ='" + DbConnection.getMd5(getPassword())
                + "' OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(selectquarey);
        if (rs.next()) {
            CurrentUser.id = rs.getInt("id");
            CurrentUser.level = rs.getInt("level");
            String currentClientquarey
                    = "SELECT CLIENTS.*,USERS.FIRSTNAME,USERS.LASTNAME,USERS.EMAIL,USERS.USERNAME from CLIENTS "
                    + "inner join users on users.id=clients.userId"
                    + " WHERE Clients.userId =" + CurrentUser.id;
            ResultSet rscurrentClient = stat.executeQuery(currentClientquarey);
            if (rscurrentClient.next()) {
                CurrentUser.clientId = rscurrentClient.getInt("id");
                CurrentUser.fName = rscurrentClient.getString("FIRSTNAME");
                CurrentUser.lName = rscurrentClient.getString("LASTNAME");
                CurrentUser.email = rscurrentClient.getString("EMAIL");
                CurrentUser.username = rscurrentClient.getString("USERNAME");
                CurrentUser.phone = rscurrentClient.getString("phone");

                CurrentUser.image = rscurrentClient.getString("image");

            }

            return "true";
        } else {
            return "false";
        }
    }

    public void logOut() throws SQLException, ClassNotFoundException {

        CurrentUser.clientId = 0;
        CurrentUser.id = 0;
        CurrentUser.fName = "";
        CurrentUser.lName = "";
        CurrentUser.email = "";
        CurrentUser.image = "";
    }

    public String checkUsername() throws SQLException, ClassNotFoundException {

        if (checkUsernameIfExist() != "") {
            return "<p id=\"usernameResulte\" class=\"error_element\"> " + checkUsernameIfExist() + " This username already exists try another </p>";
        } else {
            return "<p id=\"usernameResulte\" style=\"display: none\">true</p>";
        }
    }

    public String checkUsernameIfExist() throws SQLException, ClassNotFoundException {
        Connection conn = DbConnection.getConnection();

        String selectquarey = "SELECT * from USERS WHERE USERNAME='" + getUsername() + "'";
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(selectquarey);
        if (rs.next()) {
            return rs.getString("username");
        } else {
            return "";
        }
    }

    public static boolean emailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

    public int insertUser() throws SQLException, ClassNotFoundException {
        Connection conn = DbConnection.getConnection();
        String selectquarey = "INSERT INTO users(firstname,lastname,email,username, password, level) VALUES('" + this.firstname + "','" + this.lastname + "','" + this.email + "','" + this.username + "','" + DbConnection.getMd5(this.password) + "',1)";

        Statement stat = conn.createStatement();

        stat.executeUpdate(selectquarey, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stat.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            return 0;
        }
    }

    public String registerNewUser() throws SQLException, ClassNotFoundException {

        if (firstname != null && emailValid(email) == true && username != null && checkUsernameIfExist() == "" && password != null && confilrmPassword != null && this.password.equals(this.confilrmPassword)) {
            if (insertUser() != 0) {
                return "<p id=\"outputResulte\" style=\"display: none\">true</p>";
            } else {
                return "<p id=\"outputResulte\" class=\"error_message\">An error occurred during registration, please try again later</p>";
            }
        } else if (email == null) {
            return "";
        } else {
            String res = "<p id=\"outputResulte\" class=\"error_message\">Error: ";
            if (emailValid(email) == false) {
                res += "<br> You should enter a valid email";
            }
            if (checkUsernameIfExist() != "") {
                res += "<br> The username" + checkUsernameIfExist() + " already exists try another";

            }
            if (!this.password.equals(this.confilrmPassword)) {
                res += "<br> The passwords do not match ";
            }
            res += "</p>";
            return res;
        }

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfilrmPassword() {
        return confilrmPassword;
    }

    public void setConfilrmPassword(String confilrmPassword) {
        this.confilrmPassword = confilrmPassword;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
