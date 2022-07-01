package plus.jdk.cli.common;

import lombok.Data;

public class CommandException extends Exception {

    public CommandException(String message) {
        super(message);
    }
}
