package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {


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
    ImageFrame() {
        setTitle("ImageTest");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // Добавление компонента к фрейму.

        ImageComponent component = new ImageComponent();
        add(component);
    }

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;
}

class ImageComponent extends JComponent {


    ImageComponent() {

        int directionFrom;
        // Получаем изображения.
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Type the name of image to apply Bug filter");
            String fileName = in.nextLine();


            image = ImageIO.read(new File(fileName));


            int width = image.getWidth();
            int height = image.getHeight();

            imageOut = new BufferedImage(width - 1, height - 1, image.getType());


            int bgColor = 0;
            int mainColor = 255;
            int flag = 1;
            int rgbOutOld = 0;
            int rgbOutCur;
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {

                    int p = image.getRGB(x, y);
                    int rgb = p & 0xff;

                    rgbOutCur = imageOut.getRGB(x, y) & 0xff;

                    if (rgb == 255 && flag == 1) {

                        int curX = x;
                        int curY = y;

                        int curCol = rgb;
                        int iter = 0;


                        directionFrom = 0;
                        while (true) {
                            int dx = 0;
                            int dy = 0;

                            System.out.println(curX + " ---- " + curY);

                            p = image.getRGB(curX, curY);
                            curCol = p & 0xff;

                            switch (directionFrom) {
                                case 0:
                                    if (curCol != bgColor) {
                                        dy--;
                                        directionFrom = 1;
                                    } else {
                                        dy++;
                                        directionFrom = 3;
                                    }
                                    break;
                                case 1:
                                    if (curCol != bgColor) {
                                        dx--;
                                        directionFrom = 2;
                                    } else {
                                        dx++;
                                        directionFrom = 0;
                                    }
                                    break;
                                case 2:
                                    if (curCol != bgColor) {
                                        dy++;
                                        directionFrom = 3;
                                    } else {
                                        dy--;
                                        directionFrom = 1;
                                    }
                                    break;
                                case 3:
                                    if (curCol != bgColor) {
                                        dx++;
                                        directionFrom = 0;
                                    } else {
                                        dx--;
                                        directionFrom = 2;
                                    }
                                    break;
                            }


                            curX += dx;
                            curY += dy;

                            int colBoarder = (0xFF << 24) | (mainColor << 16) | (mainColor << 8) | mainColor;
                            imageOut.setRGB(curX, curY, colBoarder);

                            if (curX == x && curY == y) {
                                System.out.println(x + "  " + y);
                            }

                            if (curX == x && curY == y && iter > 4) {
                                break;
                            }
                            iter++;

                        }
                    }


                    p = imageOut.getRGB(x, y);
                    rgbOutCur = p & 0xff;

                    if (rgbOutCur == mainColor && rgbOutOld == bgColor) {
                        flag = 0;
                    }
                    if (rgbOutCur == bgColor && rgbOutOld == mainColor && rgb == bgColor) {
                        flag = 1;
                    }


                    rgbOutOld = rgbOutCur;


                }
            }


            try {
                String[] fName = fileName.split("\\.");
                File f = new File("Bug_" + fName[0] + ".bmp");
                ImageIO.write(imageOut, "bmp", f);


            } catch (IOException e) {
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


    }

    private BufferedImage image;
    private BufferedImage imageOut;
}