/**
 * Copyright 2014 
 * SMEdit https://github.com/StarMade/SMEdit
 * SMTools https://github.com/StarMade/SMTools
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **/
package jo.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import jo.util.StringUtil;

/**
 * @Auther Robert Barefoot for SMEdit - version 1.0
 **/
public class LogFormatter extends Formatter {

    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");

    private final boolean appendNewLine;

    public LogFormatter() {
        this(true);
    }

    public LogFormatter(final boolean appendNewLine) {
        this.appendNewLine = appendNewLine;
    }

    @Override
    public String format(final LogRecord record) {
        final StringBuilder result = new StringBuilder().append("[")
                .append(record.getLevel().getName()).append("] ")
                .append(new Date(record.getMillis())).append(": ")
                .append(record.getLoggerName()).append(": ")
                .append(record.getMessage())
                .append(StringUtil.throwableToString(record.getThrown()));
        if (appendNewLine) {
            result.append(LogFormatter.LINE_SEPARATOR);
        }
        return result.toString();
    }

    public String formatClass(final LogRecord record) {
        final String append = "...";
        final String[] className = record.getLoggerName().split("\\.");
        final String name = className[className.length - 1];
        final int maxLen = 16;

        return String.format(name.length() > maxLen ? name.substring(0, maxLen
                - append.length())
                + append : name);
    }

    public String formatError(final LogRecord record) {
        return StringUtil.throwableToString(record.getThrown());
    }

    @Override
    public String formatMessage(final LogRecord record) {
        return String.format(record.getMessage());
    }

    public String formatTimestamp(final LogRecord record) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        return "[" + dateFormat.format(record.getMillis()) + "]";
    }

}
