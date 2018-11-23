package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
 Image image;


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                ImageFrame frame = new ImageFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

}
class ImageFrame extends JFrame
{
    public ImageFrame()
    {
        setTitle("ImageTest");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // Добавление компонента к фрейму.

        ImageComponent component = new ImageComponent();
        add(component);
    }
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;
}
class ImageComponent extends JComponent
{

    public ImageComponent()
    {
        // Получаем изображения.
        try
        {
            Scanner in = new Scanner(System.in);
            String fileName = in.nextLine();
            image = ImageIO.read(new File(fileName));

            int width = image.getWidth();
            int height = image.getHeight();

            //convert to grayscale
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    int p = image.getRGB(x,y);


                    int a = (p>>24)&0xff;
                    int r = (p>>16)&0xff;
                    int g = (p>>8)&0xff;
                    int b = p&0xff;

                    //calculate average
                    int avg = (r+g+b)/3;

                    //replace RGB value with avg
                    p = (a<<24) | (avg<<16) | (avg<<8) | avg;

                    image.setRGB(x, y, p);
                }
            }

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void paintComponent(Graphics g)
    {
        if(image == null) return;
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);

        // Отображение рисунка в левом верхнем углу.
        g.drawImage(image, 0, 0, null);


        //g.drawImage(q);

        // Многократный вывод изображения в панели.

      /*  for(int i = 0; i * imageWidth <= getWidth(); i++)
            for(int j = 0; j * imageHeight <= getHeight(); j++)
                if(i + j > 0)
                    g.copyArea(0, 0, imageWidth, imageHeight, i * imageWidth, j * imageHeight);
                    */
    }
    private BufferedImage image;
}