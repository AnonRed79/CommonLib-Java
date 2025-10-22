package net.anonhub.commonlib.configParser;

public class MissingCommaException extends ConfigParserException{
    public MissingCommaException(String msg) {
        super(msg);
    }
}
