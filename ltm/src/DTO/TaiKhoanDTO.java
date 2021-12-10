/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author LAPTOPTOKYO
 */
public class TaiKhoanDTO {
    private String id_TK, strUser, strPass, strStatus;
    
    public TaiKhoanDTO(){
        this.id_TK="";
        this.strUser="";
        this.strPass="";
        this.strStatus="";
    }
    
    public TaiKhoanDTO(String id_TK, String strUser, String strPass, String strStatus){
        this.id_TK=id_TK;
        this.strUser=strUser;
        this.strPass=strPass;
        this.strStatus=strStatus;
    }
    
    /**
     * @return the id_TK
     */
    public String getId_TK() {
        return id_TK;
    }

    /**
     * @param id_TK the id_TK to set
     */
    public void setId_TK(String id_TK) {
        this.id_TK = id_TK;
    }

    /**
     * @return the strUser
     */
    public String getStrUser() {
        return strUser;
    }

    /**
     * @param strUser the strUser to set
     */
    public void setStrUser(String strUser) {
        this.strUser = strUser;
    }

    /**
     * @return the strPass
     */
    public String getStrPass() {
        return strPass;
    }

    /**
     * @param strPass the strPass to set
     */
    public void setStrPass(String strPass) {
        this.strPass = strPass;
    }

    /**
     * @return the strStatus
     */
    public String getStrStatus() {
        return strStatus;
    }

    /**
     * @param strStatus the strStatus to set
     */
    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }
}
