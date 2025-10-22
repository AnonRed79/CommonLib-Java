package net.anonhub.commonlib.configParser;

public class MissingSemicolonException extends ConfigParserException{
    public MissingSemicolonException(String msg) {
        super(msg);
    }
}
