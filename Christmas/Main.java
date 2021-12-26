package com.aghajari;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Main {


    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        ArrayList<Point> snow = new ArrayList<>();
        boolean round = true;

        //noinspection InfiniteLoopStatement
        while (true) {
            round = !round;

            int top = 3;
            int left = 20;
            int tree_height = 10;
            MyChar[][] array = new MyChar[20][tree_height * 2 + left * 2];

            Iterator<Point> it = snow.iterator();
            while (it.hasNext()) {
                Point p = it.next();
                p.y++;

                if (p.y >= array.length)
                    it.remove();
                else
                    array[p.y][p.x] = new MyChar("•️", WHITE_BOLD);
            }
            for (int j = round ? 1 : 0; j < array[0].length; j += 2) {
                if (random.nextInt(8) == 2) {
                    snow.add(new Point(j, 0));
                    array[0][j] = new MyChar("•️", WHITE_BOLD);
                }
            }

            for (int i = 0; i < tree_height; ++i) {
                for (int j = 1; j < tree_height * 2; ++j) {
                    if (j > tree_height - i - 1 && j < tree_height + i + 1) {
                        if (j != tree_height - i && j != tree_height + i && random.nextInt(4) == 2) {
                            array[top + i][left + j] = new MyChar("o", getColorByIndex(random.nextInt(7)));
                        } else {
                            array[top + i][left + j] = new MyChar("*", GREEN);
                        }
                    }
                }
            }

            drawText(top + tree_height, left + tree_height - 1, array, "mWm", YELLOW);
            drawText(top + tree_height + 1, left + tree_height - 1, array, "mWm", YELLOW);
            drawText(top + tree_height + 2, left + tree_height - 7, array, "MERRY CHRISTMAS", RED);
            array[top + tree_height + 3][left + tree_height - 3] = new MyChar("2", getColorByIndex(random.nextInt(7)));
            array[top + tree_height + 3][left + tree_height - 1] = new MyChar("0", getColorByIndex(random.nextInt(7)));
            array[top + tree_height + 3][left + tree_height + 1] = new MyChar("2", getColorByIndex(random.nextInt(7)));
            array[top + tree_height + 3][left + tree_height + 3] = new MyChar("2", getColorByIndex(random.nextInt(7)));

            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[0].length; j++) {
                    /*if (array[i][j] != null && array[i][j].ansi.hashCode() == WHITE_BOLD.hashCode()) {
                        if (i >= top - 1
                                && j >= left - 4 && j < left + tree_height * 2 + 4) {
                            System.out.print(EMPTY.get());
                            continue;
                        }
                    }*/
                    System.out.print(array[i][j] == null ? EMPTY.get() : array[i][j].get());
                }
                System.out.print('\n');
            }

            Thread.sleep(200);
            //return;
            clearConsole();
        }

    }

    public static void drawText(int i, int j, MyChar[][] array, String text, String ansi) {
        for (int k = 0; k < text.length(); k++)
            array[i][j + k] = new MyChar(String.valueOf(text.charAt(k)), ansi);
    }

    public static final String RESET = "\u001B[0m";
    //public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    //public static final String BLUE = "\u001B[34m";
    //public static final String PURPLE = "\u001B[35m";
    //public static final String CYAN = "\u001B[36m";
    //public static final String WHITE = "\u001B[37m";
    public static final String WHITE_BOLD = "\u001B[1;37m";

    public static String getColorByIndex(int i) {
        return "\u001B[3" + (i + 1) + "m";
    }

    static MyChar EMPTY = new MyChar(" ");

    static class MyChar {
        final String v;
        final String ansi;

        public MyChar(String value) {
            this.v = value;
            this.ansi = "";
        }

        public MyChar(String value, String ansi) {
            this.v = value;
            this.ansi = ansi;
        }

        public String get() {
            return ansi + v + RESET;
        }
    }

    public static void clearConsole() {
        try {
            String operatingSystem = System.getProperty("os.name");

            if (operatingSystem.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();

                startProcess.waitFor();
            }
        } catch (Exception ignore) {
        }
    }
}