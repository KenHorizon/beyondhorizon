package com.kenhorizon.loggers;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class SkillLoggers {
    public static final Marker MARKER = MarkerFactory.getMarker("Skill");
    public static final Marker MARKER_ERROR = MarkerFactory.getMarker("Skill");
    public static final Marker MARKER_DEBUG = MarkerFactory.getMarker("Skill");
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void info(String info) {
        LoggerFactory.getLogger(BeyondHorizon.ID).info(MARKER, info);
    }
    public static void debug(String info) {
        LoggerFactory.getLogger(BeyondHorizon.ID).debug(MARKER, info);
    }
    public static void error(String info) {
        LoggerFactory.getLogger(BeyondHorizon.ID).error(MARKER, info);
    }

    public static void info(String info, Object... args) {
        String format = String.format(info, args);
        LoggerFactory.getLogger(BeyondHorizon.ID).info(MARKER, format);
    }
    public static void debug(String info, Object... args) {
        String format = String.format(info, args);
        LoggerFactory.getLogger(BeyondHorizon.ID).debug(MARKER, format);
    }
    public static void error(String info, Object... args) {
        String format = String.format(info, args);
        LoggerFactory.getLogger(BeyondHorizon.ID).error(MARKER, format);
    }
}
