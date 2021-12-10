/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ASUS
 */
public class MySQLConnect {
    String Host ="jdbc:sqlserver://localhost:1433;databaseName=LTM;user=sa;password=123456";
    String UserName="sa";
    String Password="123456";
    String Database="LTM";
    String url="jdbc:sqlserver://localhost:1433;databaseName=csdlcaphe;user=sa;password=123456789";
    Connection connect= null;
    Statement statement=null;
    ResultSet result=null;

    public MySQLConnect(String Host, String UserName, String Password, String Database) throws ClassNotFoundException, SQLException, Exception
    {
        try{
            this.Host=Host;
            this.UserName=UserName;
            this.Password=Password;
            this.Database=Database;
            this.url = "jdbc:sqlserver://" + this.Host + ";integratedSecurity=true;databaseName="+ this.Database;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connect = DriverManager.getConnection(this.url);
            if (connect != null) {
                System.out.println("Connected");
                //DatabaseMetaData dm = (DatabaseMetaData) connect.getMetaData();
                //System.out.println("Driver name: " + dm.getDriverName());
                //System.out.println("Driver version: " + dm.getDriverVersion());
                //System.out.println("Product name: " + dm.getDatabaseProductName());
                //System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }
        }catch(Exception ex){
            System.err.println("Cannot connect database, " + ex);
        }
    }

    protected Statement getStatement() throws Exception{
        if(this.statement==null ? true:this.statement.isClosed()){
            this.statement=this.connect.createStatement();
        }
        return this.statement;
    }
    
    public ResultSet executeQuery(String Query) throws Exception{
        try{
            this.result=this.getStatement().executeQuery(Query);
//            while(result.next())
//            {
//                System.out.println(result.getInt(1));
//                System.out.println(result.getString(2));
//                System.out.println(result.getString(3));
//                System.out.println(result.getString(4));
//            }
        }catch(Exception e){
            throw new Exception("Error"+e.getMessage()+"-"+Query);
        }
        return this.result;
    }
    
    public int executeUpdate(String Query) throws Exception{
        int res=Integer.MIN_VALUE;
        try{
            res=this.getStatement().executeUpdate(Query);
        }catch(Exception e){
            throw new Exception("Error: "+e.getMessage()+"-"+Query);
        }//finally{
           // this.Close();
        //}
        return res;
    }

    public void Close() {
        try{
            this.connect.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    
}
