import java.util.Random;

/**
 * A stupid AI player.
 * This AI will randomly choose a column.
 */
public class StupidPlayer implements ConnectPlayer {
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
    public StupidPlayer() {
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

        // Random choose a valid column, and return it
        int col;
        do {
            col = random.nextInt(WIDTH);
        } while (!isColumnValid(col));

        // Store self piece
        dropPiece(col, Status.TWO);

        return col;
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
     *
     * @param col
     * @param piece
     */
    private void dropPiece(int col, Status piece) {
        if (!isColumnValid(col)) {
            return;
        }

        // Find the row that we can put a piece
        int row = -1;
        for (int i = HEIGHT - 1; i >= 0 && row < 0; i--) {
            if (board[i][col] == Status.NEITHER) {
                row = i;
            }
        }

        board[row][col] = piece;
    }
}
