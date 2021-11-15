/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.DangNhapController;
import DAO.MyConnect;
import DAO.MyConnectUnit;
import DTO.UserDTO;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.sql.ResultSet;

/**
 *
 * @author LAPTOPTOKYO
 */
public class UserModel {
    static ArrayList<UserDTO> arUser=new ArrayList<UserDTO>();
    MyConnectUnit connect;
    ResultSet rsTaiKhoan;
    public UserModel(){
        try {
            this.connect=MyConnect.getDAO();
        } catch (Exception ex) {
            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Tìm thông tin tài khoản khi biết ten đăng nhap
    public ArrayList User(String strTenDangNhap) throws Exception{
        String userkt="ID_TK ='"+strTenDangNhap+"'";
        rsTaiKhoan=this.connect.Select("USER_TK", userkt);
        //Lấy thông tin table tai khoản
        while(rsTaiKhoan.next()){
            UserDTO user=new UserDTO();
            user.setIdUser(rsTaiKhoan.getString(1));
            user.setIdTK(rsTaiKhoan.getString(2));
            user.setLastName(rsTaiKhoan.getString(3));
            user.setNameUser(rsTaiKhoan.getString(4));
            user.setNameUser(rsTaiKhoan.getString(4));
            arUser.add(user);
        }
        return arUser;
    }
}
