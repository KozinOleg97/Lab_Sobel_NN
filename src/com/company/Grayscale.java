package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Grayscale{
    public static void main(String args[])throws IOException{
        BufferedImage img = null;
        File f = null;

        Scanner in = new Scanner(System.in);
        System.out.println("Type the name of image to convert to grayscale");
        String fileName = in.nextLine();

        //read image
        try{
            f = new File(fileName);
            img = ImageIO.read(f);
        }catch(IOException e){
            System.out.println(e);
        }

        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();

        //convert to grayscale
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int p = img.getRGB(x,y);

                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;

                //calculate average
                int avg = (r+g+b)/3;

                //replace RGB value with avg
                p = (a<<24) | (avg<<16) | (avg<<8) | avg;

                img.setRGB(x, y, p);
            }
        }

        //write image
        try{
            String[] fName = fileName.split("\\.");
            f = new File("Output_"+ fName[0] + ".bmp");
            ImageIO.write(img, "bmp", f);

            System.out.println("Output_"+ fName[0] + ".bmp");
        }catch(IOException e){
            System.out.println(e);
        }
    }//main() ends here
}//class ends here