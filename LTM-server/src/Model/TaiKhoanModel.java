/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import DAO.MyConnect;
import DAO.MyConnectUnit;
import Controller.DangNhapController;
import DTO.TaiKhoanDTO;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LAPTOPTOKYO
 */
public class TaiKhoanModel {
    static ArrayList<TaiKhoanDTO> arTK;
    MyConnectUnit connect;
    ResultSet rsTaiKhoan;
    public TaiKhoanModel(){
        try {
            this.connect=MyConnect.getDAO();
        } catch (Exception ex) {
            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Tìm thông tin tài khoản khi biết ten đăng nhap
    public int select_Count() throws Exception{
        arTK=new ArrayList<TaiKhoanDTO>();
        rsTaiKhoan=this.connect.Select("TAIKHOAN");
        //Lấy thông tin table tai khoản
        while(rsTaiKhoan.next()){
            TaiKhoanDTO nv=new TaiKhoanDTO();
            nv.setId_TK(rsTaiKhoan.getString(1));
            nv.setStrUser(rsTaiKhoan.getString(2));
            nv.setStrPass(rsTaiKhoan.getString(3));
            nv.setStrStatus(rsTaiKhoan.getString(4));
            arTK.add(nv);
        }
        return arTK.size();
    }
    //Tìm thông tin tài khoản khi biết ten đăng nhap
    public ArrayList taiKhoan(String strTenDangNhap) throws Exception{
        arTK=new ArrayList<TaiKhoanDTO>();
        String userkt="UserName ='"+strTenDangNhap+"'";
        rsTaiKhoan=this.connect.Select("TAIKHOAN", userkt);
        //Lấy thông tin table tai khoản
        if (rsTaiKhoan.getRow()!=0) {
            while(rsTaiKhoan.next()){
                TaiKhoanDTO nv=new TaiKhoanDTO();
                nv.setId_TK(rsTaiKhoan.getString(1));
                nv.setStrUser(rsTaiKhoan.getString(2));
                nv.setStrPass(rsTaiKhoan.getString(3));
                nv.setStrStatus(rsTaiKhoan.getString(4));
                arTK.add(nv);
            }   
        }
        return arTK;
    }
    //Insert dữ liệu vào database
    public void insert(TaiKhoanDTO TaiKhoan){
        try {
            HashMap<String, Object>map=new HashMap<String,Object>();
            map.put("ID_TK",TaiKhoan.getId_TK());
            map.put("UserName",TaiKhoan.getStrUser());
            map.put("Password", TaiKhoan.getStrPass());
            map.put("Status", TaiKhoan.getStrStatus());
            this.connect.Insert("TAIKHOAN", map);
        } catch (Exception ex) {
            Logger.getLogger(TaiKhoanModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
