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
public class UserDTO {
    private String idUser, idTK, lastName, nameUser, Date_of_birth, Status;
    
    public UserDTO(){
        this.idUser="";
        this.idTK="";
        this.lastName="";
        this.nameUser="";
        this.Date_of_birth="";
        this.Status="";
    }
    
    public UserDTO(String idUser, String idTK, String lastName, String nameUser, String Date_of_birth, String Status){
        this.idUser=idUser;
        this.idTK=idTK;
        this.lastName=lastName;
        this.nameUser=nameUser;
        this.Date_of_birth=Date_of_birth;
        this.Status=Status;
    }
    
    /**
     * @return the idUser
     */
    public String getIdUser() {
        return idUser;
    }

    /**
     * @param idUser the idUser to set
     */
    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    /**
     * @return the idTK
     */
    public String getIdTK() {
        return idTK;
    }

    /**
     * @param idTK the idTK to set
     */
    public void setIdTK(String idTK) {
        this.idTK = idTK;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the nameUser
     */
    public String getNameUser() {
        return nameUser;
    }

    /**
     * @param nameUser the nameUser to set
     */
    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    /**
     * @return the Date_of_birth
     */
    public String getDate_of_birth() {
        return Date_of_birth;
    }

    /**
     * @param Date_of_birth the Date_of_birth to set
     */
    public void setDate_of_birth(String Date_of_birth) {
        this.Date_of_birth = Date_of_birth;
    }

    /**
     * @return the Status
     */
    public String getStatus() {
        return Status;
    }

    /**
     * @param Status the Status to set
     */
    public void setStatus(String Status) {
        this.Status = Status;
    }
}
