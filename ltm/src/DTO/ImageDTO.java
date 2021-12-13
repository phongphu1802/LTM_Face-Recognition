/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DTO;

public class ImageDTO {
    private String user_id;
    private String image_name;
    public ImageDTO() {
        this.user_id = "";
        this.image_name = "";
    }
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getImage_name() {
        return image_name;
    }
    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
    public ImageDTO(String user_id, String image_name) {
        super();
        this.user_id = user_id;
        this.image_name = image_name;
    }
}
