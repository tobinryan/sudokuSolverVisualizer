import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;

public class GamePanel extends JPanel {

    //region Variable Declaration
    private static final int SIZE = 9;
    static final int SCREEN_WIDTH = 270;
    static final int SCREEN_HEIGHT = 270;
    static final int UNIT_SIZE = SCREEN_WIDTH/SIZE;
    static final int GAME_DELAY = 1;
    static final int[][] board = {
            {7, 0, 2, 0, 5, 0, 6, 0, 0},
            {0, 0, 0, 0, 0, 3, 0, 0, 0},
            {1, 0, 0, 0, 0, 9, 5, 0, 0},
            {8, 0, 0, 0, 0, 0, 0, 9, 0},
            {0, 4, 3, 0, 0, 0, 7, 5, 0},
            {0, 9, 0, 0, 0, 0, 0, 0, 8},
            {0, 0, 9, 7, 0, 0, 0, 0, 5},
            {0, 0, 0, 2, 0, 0, 0, 0, 0},
            {0, 0, 7, 0, 4, 0, 2, 0, 3}
    };
    private SwingWorker<Boolean, Void> sudokuSolver;
    private boolean solving;
    //endregion

    GamePanel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    solving = true;
                    solveBoard(board);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public void paintComponent(Graphics g){
        Graphics2D graphics = (Graphics2D) g;
        super.paintComponent(graphics);
        for (int row = 0; row < SIZE; row++){
            if (row % 3 == 0 && row != 0){
                graphics.setStroke(new BasicStroke(5));
            } else {
                graphics.setStroke(new BasicStroke(1));
            }
            graphics.draw(new Line2D.Float(0, row * UNIT_SIZE, SCREEN_WIDTH, row * UNIT_SIZE));
            for (int column = 0; column < SIZE; column++){
                if (column % 3 == 0 && column != 0){
                    graphics.setStroke(new BasicStroke(5));
                } else {
                    graphics.setStroke(new BasicStroke(1));
                }
                graphics.draw(new Line2D.Float(column * UNIT_SIZE, 0, column * UNIT_SIZE, SCREEN_HEIGHT));
                graphics.setStroke(new BasicStroke(1));
                if (solving && board[row][column] != 0){
                    graphics.setColor(new Color(25, 125, 2));
                } else if (solving) {
                    graphics.setColor(new Color(255, 0, 34));
                }
                graphics.drawString(String.valueOf(board[row][column]), column * UNIT_SIZE + 12, row * UNIT_SIZE + 22);
                graphics.setColor(Color.black);
            }
        }
    }

    private static boolean isInRow(int[][] board, int num, int row){
        for (int i = 0; i < SIZE; i++){
            if (board[row][i] == num){
                return true;
            }
        }
        return false;
    }

    private static boolean isInColumn(int[][] board, int num, int column){
        for (int i = 0; i < SIZE; i++){
            if (board[i][column] == num){
                return true;
            }
        }
        return false;
    }

    private static boolean isInBox(int[][] board, int num, int row, int column){
        int rowStart = row - row % 3;
        int columnStart = column - column % 3;
        for (int i = rowStart; i < rowStart + 3; i++){
            for (int j = columnStart; j < columnStart + 3; j++) {
                if (board[i][j] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    private static  boolean isValidPlace(int[][] board, int num, int row, int column){
        return !isInRow(board, num, row) &&
                !isInColumn(board, num, column) &&
                !isInBox(board, num, row, column);
    }

    private void solveBoard(int[][] board) {
        sudokuSolver = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                for (int row = 0; row < SIZE; row++) {
                    for (int column = 0; column < SIZE; column++) {
                        if (board[row][column] == 0) {
                            for (int num = 1; num <= SIZE; num++) {
                                if (isValidPlace(board, num, row, column)) {
                                    board[row][column] = num;
                                    repaint();
                                    Thread.sleep(GAME_DELAY);
                                    if (doInBackground()) {
                                        return true;
                                    } else {
                                        board[row][column] = 0;
                                        repaint();
                                        Thread.sleep(GAME_DELAY);
                                    }
                                }
                            }
                            return false;
                        }
                    }
                }
                return true;
            }
        };
        sudokuSolver.execute();
    }

    private static void printBoard(int[][] board){
        for (int row = 0; row < SIZE; row++){
            if (row % 3 == 0 && row != 0){
                System.out.println("-------------------");
            }
            for (int column = 0; column < SIZE; column++){
                if (column % 3 == 0 && column != 0){
                    System.out.print("|");
                }
                System.out.print(board[row][column] + " ");
            }
            System.out.println();
        }
    }
}
