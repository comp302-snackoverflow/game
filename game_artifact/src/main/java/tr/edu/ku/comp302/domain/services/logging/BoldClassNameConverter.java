package tr.edu.ku.comp302.domain.services.logging;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "BoldClassNameConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"boldClass"})
@PerformanceSensitive("allocation")
public class BoldClassNameConverter extends LogEventPatternConverter {
    public BoldClassNameConverter(String[] options) {
        super("boldClass", "boldClass");
    }

    public static BoldClassNameConverter newInstance(final String[] options) {
        return new BoldClassNameConverter(options);
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        toAppendTo.append("\u001B[1m"); // Start bold formatting
        toAppendTo.append(event.getLoggerName());
        toAppendTo.append("\u001B[0m"); // End bold formatting
    }
}
