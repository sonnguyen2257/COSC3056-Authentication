package com.example.climatechange.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// Java SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Controller

public class JDBC {

    @GetMapping("/sql")
    public String jdbc(){
        return "sql";
    }

    @PostMapping("/sql")
    @ResponseBody
    public String jdbcPost(@RequestParam(name="query") String query){
        String DATABASE = "jdbc:sqlite:climate-change.project/database/climatechange.db";
        
        System.out.println("Input query is" + query);
        Connection conn = null;

        try {
            String th="";
            String td="";
            String table="";
            conn =  DriverManager.getConnection(DATABASE);
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(30);

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
                    th+="<th>"+rs.getMetaData().getColumnName(i)+"</th>";
                    td+="<td>"+rs.getString(i)+"</td>";
                    System.out.println(rs.getMetaData().getColumnName(i)+": "+rs.getString(i));
                }
                table+="<tr>"+td+"</tr>";
                td="";
            }

            if (table.equals("")){
                table="<tr><td>No results found</td></tr>";
            }
            else{
                table="<table><tr>"+th+"</tr>"+table+"</table>";
            }

            stmt.close();
            conn.close();
            return table;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Result: Failed -"+e.getMessage());
        }
        return alertPage("Result: Success", "sql");
    }

    private String alertPage(String msgAlert, String page) {
        String htmlBody = "<script>alert('" +msgAlert + "');";
        htmlBody+="window.location.href='/"+page+"';</script>";
        return htmlBody;
    }
}
