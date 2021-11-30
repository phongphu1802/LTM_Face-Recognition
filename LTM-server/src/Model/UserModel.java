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
import java.util.HashMap;

/**
 *
 * @author LAPTOPTOKYO
 */
public class UserModel {
    static ArrayList<UserDTO> arUser=new ArrayList<UserDTO>();
    MyConnectUnit connect;
    ResultSet rsUser;
    public UserModel(){
        try {
            this.connect=MyConnect.getDAO();
        } catch (Exception ex) {
            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int select_Count() throws Exception{
        rsUser=this.connect.Select("USER_TK");
            //Lấy thông tin table tai khoản
            while(rsUser.next()){
                UserDTO user=new UserDTO();
                user.setIdUser(rsUser.getString(1));
                user.setIdTK(rsUser.getString(2));
                user.setLastName(rsUser.getString(3));
                user.setNameUser(rsUser.getString(4));
                user.setNameUser(rsUser.getString(4));
                arUser.add(user);
            }
        return arUser.size();
    }
    
    //Tìm thông tin tài khoản khi biết ten đăng nhap
    public ArrayList user(String strTenDangNhap) throws Exception{
        String userkt="ID_TK ='"+strTenDangNhap+"'";
        rsUser=this.connect.Select("USER_TK", userkt);
        //Lấy thông tin table tai khoản
        while(rsUser.next()){
            UserDTO user=new UserDTO();
            user.setIdUser(rsUser.getString(1));
            user.setIdTK(rsUser.getString(2));
            user.setLastName(rsUser.getString(3));
            user.setNameUser(rsUser.getString(4));
            user.setNameUser(rsUser.getString(4));
            arUser.add(user);
        }
        return arUser;
    }
    
    public void insert(UserDTO arUser){ 
        try {
            HashMap<String, Object>map=new HashMap<String,Object>();
            map.put("ID_user", arUser.getIdUser());
            map.put("ID_TK", arUser.getIdTK());
            map.put("Lastname", arUser.getLastName());
            map.put("Nameuser", arUser.getNameUser());
            map.put("Date_of_birth", arUser.getDate_of_birth());
            map.put("Status", arUser.getStatus());
            this.connect.Insert("USER_TK", map);
        } catch (Exception ex) {
            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
