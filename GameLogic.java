/**
 * Game Logic, the connect four game control centre..
 */
public class GameLogic implements ConnectController {
    /**
     * Width of game board.
     */
    private final int WIDTH = 7;

    /**
     * Height of game board.
     */
    private final int HEIGHT = 6;

    /**
     * Game board.
     */
    private Status[][] board;

    /**
     * Game GUI, to interact with human player.
     */
    private GameDisplay gameDisplay;

    /**
     * AI player.
     */
    private ConnectPlayer aiPlayer;

    /**
     * Constructor.
     *
     * @param gameDisplay
     */
    public GameLogic(GameDisplay gameDisplay) {
        this.gameDisplay = gameDisplay;

        // Create a AI player
        aiPlayer = new StupidPlayer();

        // Initialize game board
        board = new Status[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                board[i][j] = Status.NEITHER;
            }
        }
    }

    /**
     * Informs the controller that a piece has been played in the column 'col'.
     * <p>
     * The method should return false only when the move is invalid.
     *
     * @param col column where the piece will be put
     * @return
     */
    @Override
    public boolean addPiece(int col) {
        if (!isColumnValid(col)) {
            return false;
        }

        // The row where the piece was put
        int row;

        // drop the piece
        row = dropPiece(col, Status.ONE);
        if (isConnectFour(row, col)) {
            gameDisplay.updateBoard(board);
            gameDisplay.gameOver(Status.ONE);
            return true;
        }

        // AI piece column
        int aiCol = aiPlayer.makeMove(col);

        // AI go
        row = dropPiece(aiCol, Status.TWO);
        if (isConnectFour(row, aiCol)) {
            gameDisplay.updateBoard(board);
            gameDisplay.gameOver(Status.TWO);
            return true;
        }

        // is tied
        if (isFull()) {
            gameDisplay.updateBoard(board);
            gameDisplay.gameOver(Status.NEITHER);
            return true;
        }

        gameDisplay.updateBoard(board);
        return true;
    }

    /**
     * Resets the game.
     * <p>
     * It will be called when the player requests a new game.
     */
    @Override
    public void reset() {
        // Prompt a opponent difficulty
        int difficulty = gameDisplay.promptForOpponentDifficulty(2);
        if (difficulty == 1) {
            aiPlayer = new StupidPlayer();
        } else {
            aiPlayer = new SmartPlayer();
        }

        // Initialize game board
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                board[i][j] = Status.NEITHER;
            }
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
     * Return whether is a connect four at the given (row, column).
     *
     * @param row
     * @param col
     * @return
     */
    private boolean isConnectFour(int row, int col) {
        // Connect four pieces counter
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
        if (count >= 4) {
            return true;
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
        if (count >= 4) {
            return true;
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
        if (count >= 4) {
            return true;
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
        if (count >= 4) {
            return true;
        }

        // Not found connect four
        return false;
    }

    /**
     * Return whether the board is full.
     *
     * @return
     */
    private boolean isFull() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j] == Status.NEITHER) {
                    return false;
                }
            }
        }
        return true;
    }
}
