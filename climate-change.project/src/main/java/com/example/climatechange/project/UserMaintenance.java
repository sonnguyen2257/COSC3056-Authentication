package com.example.climatechange.project;

// Java SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Java Security
import java.security.MessageDigest;
// import java.security.NoSuchAlgorithmException;

// Spring Boot
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
// import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class UserMaintenance {
    public static String username;
    public static boolean ableToDownload;
    public static boolean darkMode;


    //TODO: database path may change 
    private String DATABASE = "jdbc:sqlite:climate-change.project/database/climatechange.db";
    
    public UserMaintenance(){
        username = null;
        ableToDownload = false;
        darkMode = false;
    }

    //TODO: loginPage.html page may change name
    @GetMapping("/login")
    public String loginPage(){
        if (username != null){
            return "index";
        }
        return "loginPage";
    }


    //TODO: registerPage.html file may change name
    @GetMapping("/register")
    public String registerPage(){
        if (username != null){
            return "index";
        }
        return "registerPage";
    }

    //TODO: logout.html file may change name
    @RequestMapping("/logout")
    @ResponseBody
    public String logout(){
        username = null;
        ableToDownload = false;
        darkMode = false;
        System.out.println("Logout: success");
        // return "<script>alert('Logout success'); window.location.href='/login';</script>";
        return alertPage("Log out successful!", "/");
    }


    //TODO: RequestParam may change
    @PostMapping("/login")
    @ResponseBody
    public String Signup(@RequestParam String username, @RequestParam String password){
        System.out.println("Username : "+username);
        if(authentication(username, encrypString(password))){
            this.username = username;
            Connection conn = null;

            try {
                conn = DriverManager.getConnection(DATABASE);
                Statement statement = conn.createStatement();
                statement.setQueryTimeout(30);
                ResultSet results = statement.executeQuery("SELECT DOWNLOAD_PERMIT,NIGHT_MODE FROM USER WHERE USER_NAME='"+this.username+"'");
                ableToDownload = results.getBoolean("DOWNLOAD_PERMIT");
                darkMode = results.getBoolean("NIGHT_MODE");
                statement.close();
                conn.close();
                // return "<script>alert('Login success'); window.location.href='/';</script>";
                return alertPage("Login Sucess", "/");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                
            } 
        } 

        return alertPage("Login Failed", "login");
    }
   
    //TODO: RequestParam may change
    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestParam String username, @RequestParam String password, Model model){
        // get attribute from registerPage.html
        password = encrypString(password);
        System.out.println("Sign-up User: "+ username);
        if (username == null || password == null || username.length() == 0 || password.length() == 0) {
            System.out.println("Password and username cannot be empty");
            return "registerPage";
        } else {
            //CREATE SQL TABLE IF NOT EXISTS
            updateSQL("CREATE TABLE IF NOT EXISTS USER(USER_NAME VARCHAR(30), "+
                "PASSWD VARCHAR(100), "+
                "DOWNLOAD_PERMIT BOOL DEFAULT 'FALSE' NOT NULL, "+
                "NIGHT_MODE BOOL DEFAULT'FALSE' NOT NULL, PRIMARY KEY (USER_NAME))");

            //ONLY ALLOW USER TO REGISTER IF PASSWORD IS LESS THAN 100 CHARACTERS
            if (password.length()<100){
                Connection conn = null;
                try {
                    conn = DriverManager.getConnection(DATABASE);
                    Statement statement = conn.createStatement();
                    statement.setQueryTimeout(30);
                    statement.executeUpdate("INSERT INTO USER(USER_NAME, PASSWD) VALUES ('"+username+"', '"+password+"')");
                    // statement.executeUpdate("INSERT INTO USER(USER_NAME, PASSWD) VALUES ('"+this.username.toUpperCase()+"', '"+this.password+"')");
                    statement.close();
                    conn.close();
                    // return "<script>alert('Register Success');window.location.href='/login';</script>";
                    return alertPage("Register Success", "login");          
                } catch (Exception e) {
                    System.out.println("Registration failed with reason: " + e.getMessage());        
                }
            }  else {
                System.out.println("Password is too long");
            }

        }
        
        return "<script>alert('Register Failed');window.location.href='/register';</script>";
    }

    // THIS METHOD TO ENABLE DOWNLOAD FEATURE
    private void enableDownloadFeature(){
        if (username != null) {
            ableToDownload = true;
            updateSQL("UPDATE USER SET DOWNLOAD_PERMIT = 'TRUE' WHERE USER_NAME = '"+this.username+"'");
        }
    }

    // THIS METHOD TO DISABLE DOWNLOAD FEATURE
    private void disableDownloadFeature(){
        if (username != null) {
            ableToDownload = false;
            updateSQL("UPDATE USER SET DOWNLOAD_PERMIT = 'FALSE' WHERE USER_NAME = '"+this.username+"'");
        }
    }

    // THIS METHOD TO ENABLE DARK MODE
    private void enableDarkMode(){
        if (username != null) {
            darkMode = true;
            updateSQL("UPDATE USER SET NIGHT_MODE = 'TRUE' WHERE USER_NAME = '"+username+"'");
        }
    }

    // THIS METHOD TO DISABLE DARK MODE
    private void disableDarkMode(){
        if (username != null) {
            darkMode = false;
            updateSQL("UPDATE USER SET NIGHT_MODE = 'FALSE' WHERE USER_NAME = '"+username+"'");
        }
    }

    private void updateSQL(String query){
        Connection conn = null;
        try {
            // Create a connection to the database
            conn = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = conn.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            statement.executeUpdate(query);

            statement.close();
            conn.close();
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println("SQL query "+query+" failed with reason: " + e.getMessage());
        } 
        
    }

    //THIS METHOD TO ENCRYPT STRING
    private String encrypString(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(str.getBytes());
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();

            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }

            System.out.println("String encrypted as: "+sb.toString());
            return sb.toString();
        } catch (Exception e ) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    private boolean authentication(String user, String passwd){
        Connection conn = null;
        try {
            // Create a connection to the database
            conn = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = conn.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT PASSWD FROM USER WHERE USER_NAME='"+user+"'";
            ResultSet results = statement.executeQuery(query);

            // Comparing text
            Boolean isMatch = passwd.equals(results.getString("PASSWD"));
            if (isMatch){
                System.out.println("Authentication: success");
                return true;
            } else {
                System.out.println("Authentication: failed");
                
            }

            statement.close();
            conn.close();
            return isMatch;
        } catch (Exception e) {
            System.out.println("Authentication: failed - " + e.getMessage());
            return false;
        } 
        
    }


    private String alertPage(String msgAlert, String page) {
        String htmlBody = "<script>alert('" +msgAlert + "');";
        htmlBody+="window.location.href='/"+page+"';</script>";
        return htmlBody;
    }
}
