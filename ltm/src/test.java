
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javax.imageio.ImageIO;
import java.util.ArrayList; 
import java.util.Calendar;
import java.util.Collections; 
import java.util.Date;
import java.util.List; 
import java.util.Random; 
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.format.DateTimeFormatter;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LAPTOPTOKYO
 */
public class test {
    public static long calculateAge(String birthDate) {
        LocalDate localDate1 = LocalDate.parse(birthDate,DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate localDate2 = LocalDate.now();
        long years = java.time.temporal.ChronoUnit.YEARS.between( localDate1 , localDate2 );
        return years;
    }
    
    public static void main(String a[]){         
         long s1 = calculateAge("2000-02-18");
         System.out.println(s1);
    }  
}
