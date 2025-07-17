package top.jpower.core.log.color;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiElement;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mr.g
 * @date 2025-7-18 0:11
 * @description
 */
public class ColorConverter extends CompositeConverter<ILoggingEvent> {

    private static final Map<String, AnsiElement> ELEMENTS;

    static {
        Map<String, AnsiElement> ansiElements = new HashMap<>();
        ansiElements.put("faint", AnsiStyle.FAINT);
        ansiElements.put("red", AnsiColor.RED);
        ansiElements.put("green", AnsiColor.GREEN);
        ansiElements.put("yellow", AnsiColor.YELLOW);
        ansiElements.put("blue", AnsiColor.BLUE);
        ansiElements.put("magenta", AnsiColor.MAGENTA);
        ansiElements.put("cyan", AnsiColor.CYAN);

        ansiElements.put("bold", AnsiStyle.BOLD);
        ansiElements.put("italic", AnsiStyle.ITALIC);
        ansiElements.put("underline", AnsiStyle.UNDERLINE);
        ansiElements.put("bright_black", AnsiColor.BRIGHT_BLACK);
        ansiElements.put("bright_red", AnsiColor.BRIGHT_RED);
        ansiElements.put("bright_green", AnsiColor.BRIGHT_GREEN);
        ansiElements.put("bright_yellow", AnsiColor.BRIGHT_YELLOW);
        ansiElements.put("bright_blue", AnsiColor.BRIGHT_BLUE);
        ansiElements.put("bright_magenta", AnsiColor.BRIGHT_MAGENTA);
        ansiElements.put("bright_cyan", AnsiColor.BRIGHT_CYAN);
        ansiElements.put("bright_white", AnsiColor.BRIGHT_WHITE);

        ELEMENTS = Collections.unmodifiableMap(ansiElements);
    }

    private static final Map<Integer, AnsiElement> LEVELS;

    static {
        Map<Integer, AnsiElement> ansiLevels = new HashMap<>();
        ansiLevels.put(Level.ERROR_INTEGER, AnsiColor.RED);
        ansiLevels.put(Level.WARN_INTEGER, AnsiColor.YELLOW);
        LEVELS = Collections.unmodifiableMap(ansiLevels);
    }

    @Override
    protected String transform(ILoggingEvent event, String in) {
        AnsiElement element = ELEMENTS.get(getFirstOption());
        if (element == null) {
            // Assume highlighting
            element = LEVELS.get(event.getLevel().toInteger());
            element = (element != null) ? element : AnsiColor.GREEN;
        }
        return toAnsiString(in, element);
    }

    protected String toAnsiString(String in, AnsiElement element) {
        return AnsiOutput.toString(element, in);
    }

}