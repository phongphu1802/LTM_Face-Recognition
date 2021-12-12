/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltm_face_recognition;

import DTO.HinhanhDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.json.simple.parser.ParseException;

/**
 *
 * @author LAPTOPTOKYO
 */
public class test {
    public static void main(String[] args){
        ArrayList<HinhanhDTO> arRI=new ArrayList<HinhanhDTO>();
        arRI.add(new HinhanhDTO("A","A", (float) 20.2));
         arRI.add(new HinhanhDTO("B","B", (float) 18.2));
          arRI.add(new HinhanhDTO("C","C", (float) 16.2));
           arRI.add(new HinhanhDTO("D","D", (float) 17.2));
            arRI.add(new HinhanhDTO("E","E", (float) 19.2));
             arRI.add(new HinhanhDTO("F","F", (float) 11.2));
             
        //Sắp xếp danh sách theo số điểm giảm dần!
        Collections.sort(arRI, new Comparator<HinhanhDTO>() {
            @Override
            public int compare(HinhanhDTO sv1, HinhanhDTO sv2) {
                if (sv1.getiConfident() < sv2.getiConfident()) {
                    return 1;
                } else {
                    if (sv1.getiConfident() == sv2.getiConfident()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }
        });
        
        System.out.println("Danh sách sắp xếp theo thứ tự điểm giảm dần là: ");
        for (int i = 0; i < arRI.size(); i++) {
            System.out.println("Tên: " + arRI.get(i).getsName());
        }
    }
}
