import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeAIWithGUI {
    private static final char EMPTY = '-';
    private static final char PLAYER = 'X';
    private static final char AI = 'O';
    private static final int SIZE = 3;

    private char[][] board;
    private JButton[][] buttons;

    public TicTacToeAIWithGUI() {
        board = new char[SIZE][SIZE];
        buttons = new JButton[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Jogo da Velha com IA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(SIZE, SIZE));

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (board[row][col] == EMPTY) {
                            playerMove(row, col);
                            buttons[row][col].setText(String.valueOf(PLAYER));
                            if (checkWin(PLAYER)) {
                                JOptionPane.showMessageDialog(frame, "Você venceu!");
                                resetGame();
                                return;
                            }
                            if (isBoardFull()) {
                                JOptionPane.showMessageDialog(frame, "Empate!");
                                resetGame();
                                return;
                            }
                            aiMove();
                            updateBoard();
                            if (checkWin(AI)) {
                                JOptionPane.showMessageDialog(frame, "A IA venceu! Mais sorte na próxima vez.");
                                resetGame();
                                return;
                            }
                            if (isBoardFull()) {
                                JOptionPane.showMessageDialog(frame, "Empate!");
                                resetGame();
                            }
                        }
                    }
                });
                frame.add(buttons[i][j]);
            }
        }

        frame.setVisible(true);
    }

    private void updateBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != EMPTY) {
                    buttons[i][j].setText(String.valueOf(board[i][j]));
                }
            }
        }
    }

    private void resetGame() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
                buttons[i][j].setText(" ");
            }
        }
    }

    private boolean isBoardFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWin(char player) {
        // Verificar linhas
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }

        // Verificar colunas
        for (int j = 0; j < SIZE; j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
                return true;
            }
        }

        // Verificar diagonais
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }

        return false;
    }

    private void playerMove(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || board[row][col] != EMPTY) {
            throw new IllegalArgumentException("Movimento inválido.");
        }
        board[row][col] = PLAYER;
    }

    private void aiMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1;
        int bestCol = -1;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = AI;
                    int score = minimax(false);
                    board[i][j] = EMPTY;
                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = i;
                        bestCol = j;
                    }
                }
            }
        }

        board[bestRow][bestCol] = AI;
    }

    private int minimax(boolean isMaximizing) {
        if (checkWin(AI)) {
            return 10;
        }
        if (checkWin(PLAYER)) {
            return -10;
        }
        if (isBoardFull()) {
            return 0;
        }

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = isMaximizing ? AI : PLAYER;
                    int score = minimax(!isMaximizing);
                    board[i][j] = EMPTY;
                    if (isMaximizing) {
                        bestScore = Math.max(bestScore, score);
                    } else {
                        bestScore = Math.min(bestScore, score);
                    }
                }
            }
        }

        return bestScore;
    }

    public static void main(String[] args) {
        TicTacToeAIWithGUI game = new TicTacToeAIWithGUI();
        game.createAndShowGUI();
    }
}