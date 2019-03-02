/**
 * A Connect Controller creator, which hide the specify implement of ConnectController.
 */
public class ControllerFactory {
    public static ConnectController makeController(GameDisplay gameDisplay) {
        return new GameLogic(gameDisplay);
    }
}