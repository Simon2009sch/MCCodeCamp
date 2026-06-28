package me.simoncrafter.mCCodeCamp.lib.input;

import java.util.Map;

public class AbstractInput {

    protected String key;
    protected Map<String, Object> args;

    public AbstractInput(String key, Map<String, Object> args) {
        this.key = key;
        this.args = args;
    }

    public String getKey() {
        return key;
    }

    public Map<String, Object> getArgs() {
        return args;
    }
}
