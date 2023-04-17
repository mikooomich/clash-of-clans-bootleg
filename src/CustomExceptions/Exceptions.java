package CustomExceptions;

public class Exceptions {

    /**
     * Exception should be raised when a users storage is full and the game is trying to add more resources
     */
    public static class FullStorageException extends Exception {

        public FullStorageException(String message, Throwable cause) {
            super(message, cause);
        }

        public FullStorageException(String message) {
            super(message);
        }
    }

    /**
     * Exception should be raised when trying to add / subtract an invalid resource from somewhere
     */
    public static class InvalidResourceException extends Exception {

        public InvalidResourceException(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidResourceException(String message) {
            super(message);
        }
    }

    /**
     * Exception should be raised when there is not enough resources in a users storage to purchase the entity
     */
    public static class NotEnoughResourcesException extends Exception {

        public NotEnoughResourcesException(String message, Throwable cause) {
            super(message, cause);
        }

        public NotEnoughResourcesException(String message) {
            super(message);
        }
    }

    /**
     * Exception should be raised when trying to place a structure in a non-placeable spot when editing village
     */
    public static class InvalidPlacementException extends Exception {

        public InvalidPlacementException(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidPlacementException(String message) {
            super(message);
        }
    }

    /**
     * Exception should be raised when a user tries to upgrade an entity when its already maxed for the village hall level
     */
    public static class InsufficientUpgradeLevelException extends Exception {

        public InsufficientUpgradeLevelException(String message, Throwable cause) {
            super(message, cause);
        }

        public InsufficientUpgradeLevelException(String message) {
            super(message);
        }
    }

    /**
     * Exception should be raised when trying to add an entity that causes the population limit to be surpassed
     */
    public static class PopulationCapReachedException extends Exception {

        public PopulationCapReachedException(String message, Throwable cause) {
            super(message, cause);
        }

        public PopulationCapReachedException(String message) {
            super(message);
        }
    }

    /**
     * Exception should be raised when there is an issue connecting with the server/another player
     */
    //Not sure if this is going to be used, just placed for now, we don't have a server so
    public static class ServerConnectionError extends Exception {

        public ServerConnectionError(String message, Throwable cause) {
            super(message, cause);
        }

        public ServerConnectionError(String message) {
            super(message);
        }
    }

    /**
     * Exception should be raised when the ID doesn't exist
     */
    public static class InvalidIDException extends Exception {

        public InvalidIDException(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidIDException(String message) {
            super(message);
        }
    }

}
