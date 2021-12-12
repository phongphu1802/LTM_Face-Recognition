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
public class HinhanhDTO {
    private String sName, sImage;
    private float iConfident;
    
    public HinhanhDTO(){
        this.sName="";
        this.sImage="";
        this.iConfident=0;
    }
    
    public HinhanhDTO(String sName,String sImage,float iConfident){
        this.sName=sName;
        this.sImage=sImage;
        this.iConfident=iConfident;
    }
    /**
     * @return the sName
     */
    public String getsName() {
        return sName;
    }

    /**
     * @param sName the sName to set
     */
    public void setsName(String sName) {
        this.sName = sName;
    }

    /**
     * @return the sImage
     */
    public String getsImage() {
        return sImage;
    }

    /**
     * @param sImage the sImage to set
     */
    public void setsImage(String sImage) {
        this.sImage = sImage;
    }

    /**
     * @return the iConfident
     */
    public float getiConfident() {
        return iConfident;
    }

    /**
     * @param iConfident the iConfident to set
     */
    public void setiConfident(float iConfident) {
        this.iConfident = iConfident;
    }
}
