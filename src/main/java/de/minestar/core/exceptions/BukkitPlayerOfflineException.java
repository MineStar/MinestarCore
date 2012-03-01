package de.minestar.core.exceptions;

public class BukkitPlayerOfflineException extends RuntimeException {

    private static final long serialVersionUID = -1134077131156224579L;

    public BukkitPlayerOfflineException(String playerName) {
        super("Could not update player '" + playerName + "'. He seems to be offline!");
    }
}
