import java.util.Random;

/**
 * A smart AI player.
 */
public class SmartPlayer implements ConnectPlayer {
    /**
     * Width of game board.
     */
    private final int WIDTH = 7;

    /**
     * Height of game board.
     */
    private final int HEIGHT = 6;

    /**
     * AI's memory, a board.
     * Status.ONE always represent opponent,
     * Status.TWO always represent himself (the AI).
     */
    private Status[][] board;

    /**
     * Random number generator.
     */
    private Random random;

    /**
     * Constructor.
     */
    public SmartPlayer() {
        random = new Random();

        // Initialize AI's memory
        board = new Status[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                board[i][j] = Status.NEITHER;
            }
        }
    }

    /**
     * Take the last move made by the (human) player and return
     * the next move made by the AI.
     *
     * @param lastCol
     * @return
     */
    @Override
    public int makeMove(int lastCol) {
        // Store opponent's piece
        dropPiece(lastCol, Status.ONE);

        // Standing on the opponent's point of view
        int opponentBestCol = -1;
        int opponentBestScore = -1;
        for (int col = 0; col < WIDTH; col++) {
            if(isColumnValid(col)) {
                int row = dropPiece(col, Status.ONE);
                int score = getMaxNumOfConnectedPieces(row, col);
                if (score > opponentBestScore) {
                    opponentBestCol = col;
                    opponentBestScore = score;
                }
                popPiece(col);
            }
        }

        // Standing on ai self point of view
        int selfBestCol = -1;
        int selfBestScore = -1;
        for (int col = 0; col < WIDTH; col++) {
            if(isColumnValid(col)) {
                int row = dropPiece(col, Status.TWO);
                int score = getMaxNumOfConnectedPieces(row, col);
                if (score > selfBestScore) {
                    selfBestCol = col;
                    selfBestScore = score;
                }
                popPiece(col);
            }
        }

        if(selfBestScore >= opponentBestScore) {
            // Store self piece
            dropPiece(selfBestCol, Status.TWO);
            return selfBestCol;
        } else {
            // Store self piece
            dropPiece(opponentBestCol, Status.TWO);
            return opponentBestCol;
        }
    }

    /**
     * Return whether the given column is valid.
     *
     * @param col
     * @return
     */
    private boolean isColumnValid(int col) {
        if (col < 0 || col >= WIDTH) {
            return false;
        }

        return board[0][col] == Status.NEITHER;
    }

    /**
     * Drop a piece into a column.
     * If the given column is not valid, do nothing.
     * <p>
     * Return the row where the piece was put.
     *
     * @param col
     * @param piece
     * @return
     */
    private int dropPiece(int col, Status piece) {
        // Find the row that we can put a piece
        int row = -1;
        for (int i = HEIGHT - 1; i >= 0 && row < 0; i--) {
            if (board[i][col] == Status.NEITHER) {
                row = i;
            }
        }

        board[row][col] = piece;
        return row;
    }

    /**
     * Popup a piece in the given column.
     *
     * @param col
     */
    private void popPiece(int col) {
        int row = -1;
        for (int i = 0; i < HEIGHT && row < 0; i++) {
            if (board[i][col] != Status.NEITHER) {
                row = i;
            }
        }

        board[row][col] = Status.NEITHER;
    }


    /**
     * Get the maximum number of connected pieces.
     *
     * @param row
     * @param col
     * @return
     */
    private int getMaxNumOfConnectedPieces(int row, int col) {
        // max number of connected pieces
        int maxNum = -1;

        // Pieces counter
        int count;
        Status piece = board[row][col];

        // Horizontal
        count = 0;
        // Count the pieces on the left side
        for (int i = col; i >= 0 && board[row][i] == piece; i--) {
            count++;
        }
        // Count the pieces on the left side
        for (int i = col + 1; i < WIDTH && board[row][i] == piece; i++) {
            count++;
        }
        // is connect four
        if (count > maxNum) {
            maxNum = count;
        }

        // Vertical
        count = 0;
        // top
        for (int i = row; i >= 0 && board[i][col] == piece; i--) {
            count++;
        }
        // bottom
        for (int i = row + 1; i < HEIGHT && board[i][col] == piece; i++) {
            count++;
        }
        // is connect four
        if (count > maxNum) {
            maxNum = count;
        }

        // Anti-Diagonal
        count = 0;
        // top left
        for (int i = 0; row - i >= 0 && col - i >= 0 && board[row - i][col - i] == piece; i++) {
            count++;
        }
        // bottom right
        for (int i = 1; row + i < HEIGHT && col + i < WIDTH && board[row + i][col + i] == piece; i++) {
            count++;
        }
        // is connect four
        if (count > maxNum) {
            maxNum = count;
        }

        // Diagonal
        count = 0;
        // top right
        for (int i = 0; row - i >= 0 && col + i < WIDTH && board[row - i][col + i] == piece; i++) {
            count++;
        }
        // bottom left
        for (int i = 1; row + i < HEIGHT && col - i >= 0 && board[row + i][col - i] == piece; i++) {
            count++;
        }
        // is connect four
        if (count > maxNum) {
            maxNum = count;
        }

        return maxNum;
    }
}
