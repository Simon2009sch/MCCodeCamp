package me.simoncrafter.mCCodeCamp.lib;

import me.simoncrafter.mCCodeCamp.MCCodeCamp;

import java.util.logging.Logger;

public class Logs {

    private static int loglevel = 1;
    private static Logger logger = MCCodeCamp.getInstance().getLogger();

    public static void info(String text) {
        info(text, 0);
    }

    public static void info(String text, int _loglevel) {
        if (loglevel >= _loglevel) {
            logger.info(text);
        }
    }

    public static void warn(String text) {
        warn(text, 0);
    }

    public static void warn(String text, int _loglevel) {
        if (loglevel >= _loglevel) {
            logger.warning(text);
        }
    }

    public static void error(String text) {
        error(text, 0);
    }

    public static void error(String text, int _loglevel) {
        if (loglevel >= _loglevel) {
            logger.severe(text);
        }
    }




    /**
     * Returns the detail at what detail logs get printed with
     * @return
     */
    public static int getLoglevel() {
        return loglevel;
    }

    /**
     * Sets the level of detail the logs get printed with
     * @return
     */
    public static void setLoglevel(int loglevel) {
        Logs.loglevel = loglevel;
    }

}
