/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author LAPTOPTOKYO
 */
public class MyConnect {
    public static MyConnectUnit getDAO() throws Exception{
        return new MyConnectUnit("localhost:1433","sa","123456","LTM");
    }
}
