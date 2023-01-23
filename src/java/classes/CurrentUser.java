/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author MOHAMAD ALMBAKHER
 */
@ManagedBean(name = "CurrentUser", eager = true)
@RequestScoped
public class CurrentUser {
   static int id =0;
      static int clientId =0;
      static int level =0;
 static String fName="";
   static String lName="";
   static String image="";
   static String email="";
   static String username="";
   static String phone="";

    public CurrentUser() {
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        CurrentUser.username = username;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        CurrentUser.phone = phone;
    }

    public static int getClientId() {
        return clientId;
    }

    public static void setClientId(int clientId) {
        CurrentUser.clientId = clientId;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        CurrentUser.id = id;
    }

    public static String getfName() {
        return fName;
    }

    public static void setfName(String fName) {
        CurrentUser.fName = fName;
    }

    public static String getlName() {
        return lName;
    }

    public static void setlName(String lName) {
        CurrentUser.lName = lName;
    }

    

    public static String getImage() {
        return image;
    }

    public static void setImage(String image) {
        CurrentUser.image = image;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        CurrentUser.email = email;
    }

    public static String selectAdminOrClient() {
        if (CurrentUser.level==0) {
            return  "includes/vertical_menu.xhtml";
        }else{
            return  "includes/vertical_menu_client.xhtml";
        }

    }
    public static String showPayBillSection() {
        if (CurrentUser.level==0) {
            return  "";
        }else{
            return  "includes/pay_bill.xhtml";
        }

    }
      public static String showstatisticsSection() {
        if (CurrentUser.level==0) {
            return  "includes/statistics_admin.xhtml";
        }else{
            return  "";
        }

    }
}
