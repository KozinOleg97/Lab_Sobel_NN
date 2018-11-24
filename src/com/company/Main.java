package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    Image image;


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                ImageFrame frame = new ImageFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

}

class ImageFrame extends JFrame {
    public ImageFrame() {
        setTitle("ImageTest");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // Добавление компонента к фрейму.

        ImageComponent component = new ImageComponent();
        add(component);
    }

    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;
}

class ImageComponent extends JComponent {

    int[][] maskX, maskY;
    int[][] Gx, Gy;
    String fileName;
    int sharpness;


    public ImageComponent() {
        // Получаем изображения.
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Type the name of image to apply Sobel filter");
            fileName = in.nextLine();
            System.out.println("Enter the sharpness of Sobel filter (0 - 100)");
            sharpness = in.nextInt();
            in.nextLine();

            image = ImageIO.read(new File(fileName));


            int width = image.getWidth();
            int height = image.getHeight();

            imageOut = new BufferedImage(width - 2, height - 2, image.getType());

            maskX = new int[3][3];
            maskY = new int[3][3];
            /*maskX[0][0] = -1; maskX[0][1] = -2; maskX[0][2] = -1;
            maskX[1][0] = 0;  maskX[1][1] = 0;  maskX[1][2] = 0;
            maskX[2][0] = 1;  maskX[2][1] = 2;  maskX[2][2] = 1;

            maskY[0][0] = -1; maskY[0][1] = 0; maskY[0][2] = 1;
            maskY[1][0] = -2;  maskY[1][1] = 0;  maskY[1][2] = 2;
            maskY[2][0] = -1;  maskY[2][1] = 0;  maskY[2][2] = 1;*/

            maskX[0][0] = -1;
            maskX[0][1] = -2;
            maskX[0][2] = -1;
            maskX[1][0] = 0;
            maskX[1][1] = 0;
            maskX[1][2] = 0;
            maskX[2][0] = 1;
            maskX[2][1] = 2;
            maskX[2][2] = 1;

            maskY[0][0] = -1;
            maskY[0][1] = 0;
            maskY[0][2] = 1;
            maskY[1][0] = -2;
            maskY[1][1] = 0;
            maskY[1][2] = 2;
            maskY[2][0] = -1;
            maskY[2][1] = 0;
            maskY[2][2] = 1;


            Gx = new int[width - 2][height - 2];
            Gy = new int[width - 2][height - 2];

            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {


                    int curPixelSumX = 0;
                    int curPixelSumY = 0;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            int p = image.getRGB(x + (i - 1), y + (i - 1));
                            int rgb = p & 0xff;

                            curPixelSumX += maskX[i][j] * rgb;
                            curPixelSumY += maskY[i][j] * rgb;
                        }
                    }

                    Gx[x - 1][y - 1] = curPixelSumX;
                    //System.out.println(curPixelSumX);
                    Gy[x - 1][y - 1] = curPixelSumY;


                }
            }


            for (int y = 1; y < imageOut.getHeight() - 1; y++) {
                for (int x = 1; x < imageOut.getWidth() - 1; x++) {

                    double G = Math.sqrt((Gx[x][y] * Gx[x][y]) + (Gy[x][y] * Gy[x][y]));
                    int rgb = (int) G;
                    if (rgb < 255/100*sharpness){
                        rgb =0;
                    }

                    int p = (0xFF << 24) | (rgb << 16) | (rgb << 8) | rgb;


                    imageOut.setRGB(x, y, p);
                }
            }

            try{
                String[] fName = fileName.split("\\.");
                File f = new File("Sobel_"+ fName[0] + ".bmp");
                ImageIO.write(imageOut, "bmp", f);

                System.out.println("Sobel("+sharpness +"$)_"+ fName[0] + ".bmp");
            }catch(IOException e){
                System.out.println(e);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        if (imageOut == null) return;
        int imageWidth = imageOut.getWidth(this);
        int imageHeight = imageOut.getHeight(this);

        // Отображение рисунка в левом верхнем углу.
        g.drawImage(image, imageWidth, 0, null);
        g.drawImage(imageOut, 0, 0, null);


        //g.drawImage(q);

        // Многократный вывод изображения в панели.

      /*  for(int i = 0; i * imageWidth <= getWidth(); i++)
            for(int j = 0; j * imageHeight <= getHeight(); j++)
                if(i + j > 0)
                    g.copyArea(0, 0, imageWidth, imageHeight, i * imageWidth, j * imageHeight);
                    */
    }

    private BufferedImage image;
    private BufferedImage imageOut;
}