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
package jo.sm.ui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import jo.log.LogFormatter;
import jo.util.StringUtil;

/**
 * @Auther Robert Barefoot for SMEdit Classic - version 1.0
 **/
public class LogTextArea extends JList {

	public static final int MAX_ENTRIES = 100;
	public static final Rectangle BOTTOM_OF_WINDOW = new Rectangle(0,
			Integer.MAX_VALUE, 0, 0);
	private static final long serialVersionUID = 568094664651684086L;
    private static final Formatter formatter = new Formatter() {
        
        private final SimpleDateFormat dateFormat = new SimpleDateFormat(
                "hh:mm:ss");
        
        @Override
        public String format(final LogRecord record) {
            final String[] className = record.getLoggerName().split("\\.");
            final String name = className[className.length - 1];
            final int maxLen = 22;
            final String append = "...";
            
            return String.format(
                    "[%s] %-" + maxLen + "s %s %s",
                    dateFormat.format(record.getMillis()),
                    name.length() > maxLen ? name.substring(0,
                            maxLen - append.length())
                            + append : name, record.getMessage(),
                            StringUtil.throwableToString(record.getThrown()));
        }
    };
    private static final Formatter copyPasteFormatter = new LogFormatter(false);
    private static final Logger LOG = Logger.getLogger(LogTextArea.class.getName());
	private final LogQueue logQueue = new LogQueue();

	private final LogAreaListModel model = new LogAreaListModel();

	private final Runnable scrollToBottom = new Runnable() {

		@Override
		public void run() {
			scrollRectToVisible(LogTextArea.BOTTOM_OF_WINDOW);
		}
	};


        @SuppressWarnings("unchecked")
	public LogTextArea() {
		setModel(model);
		setCellRenderer(new Renderer());
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		String fontName = Font.MONOSPACED;
		for (final Font font : GraphicsEnvironment
				.getLocalGraphicsEnvironment().getAllFonts()) {
			final String name = font.getName();
			if (name.matches("Monaco|Consolas")) {
				fontName = name;
				break;
			}
		}
		setFont(new Font(fontName, Font.PLAIN, 12));

		new Thread(logQueue, "LogGuiQueue").start();
	}

	/**
	 * Logs a new entry to be shown in the list. Thread safe.
	 * 
	 * @param logRecord
	 *            The entry.
	 */
	public void log(final LogRecord logRecord) {
		logQueue.queue(new WrappedLogRecord(logRecord));
	}

    private static class Renderer implements ListCellRenderer {

        private final Border EMPTY_BORDER = new EmptyBorder(1, 1, 1, 1);
        private final Border SELECTED_BORDER = UIManager
                .getBorder("List.focusCellHighlightBorder");
        private final Color DARK_GREEN = new Color(0, 0xcc, 0);
        private final Color DARK_RED = new Color(0xcc, 0, 0);
        private final Color DARK_PURPLE = new Color(0xcc, 0, 0xcc);
        private final Color DARK_BLUE = new Color(0, 0, 0xcc);
        private final Color GREY = new Color(0xcc, 0xcc, 0xcc);

        @Override
        public Component getListCellRendererComponent(final JList list,
                final Object value, final int index, final boolean isSelected,
                final boolean cellHasFocus) {
            if (!(value instanceof WrappedLogRecord)) {
                return new JLabel();
            }
            
            final WrappedLogRecord wlr = (WrappedLogRecord) value;
            
            final JTextPane result = new JTextPane();
            result.setDragEnabled(true);
            result.setText(wlr.formatted);
            result.setComponentOrientation(list.getComponentOrientation());
            result.setFont(list.getFont());
            result.setBorder(cellHasFocus || isSelected ? SELECTED_BORDER
                    : EMPTY_BORDER);
            
            if (wlr.record.getLevel() == Level.CONFIG) {
                result.setForeground(DARK_BLUE);
            }
            
            if (wlr.record.getLevel() == Level.SEVERE) {
                result.setBackground(DARK_RED);
                result.setForeground(Color.WHITE);
            }
            
            if (wlr.record.getLevel() == Level.WARNING) {
                result.setForeground(DARK_RED);
            }
            
            if (wlr.record.getLevel() == Level.FINE
                    || wlr.record.getLevel() == Level.FINER
                    || wlr.record.getLevel() == Level.FINEST) {
                result.setForeground(DARK_GREEN);
            }
            
            final Object[] parameters = wlr.record.getParameters();
            if (parameters != null) {
                for (final Object parameter : parameters) {
                    if (parameter == null) {
                        continue;
                    }
                    
                    if (parameter instanceof Color) {
                        result.setForeground((Color) parameter);
                    } else if (parameter instanceof Font) {
                        result.setFont((Font) parameter);
                    }
                }
            }
            
            return result;
        }
    }

    private class LogAreaListModel extends AbstractListModel {

        private static final long serialVersionUID = 0;
        private List<WrappedLogRecord> records = new ArrayList<>(
                LogTextArea.MAX_ENTRIES);

        public void addAllElements(final List<WrappedLogRecord> obj) {
            records.addAll(obj);
            if (getSize() > LogTextArea.MAX_ENTRIES) {
                records = records.subList(
                        (getSize() - LogTextArea.MAX_ENTRIES), getSize());
                
                fireContentsChanged(this, 0, (getSize() - 1));
            } else {
                fireIntervalAdded(this, (getSize() - 1), (getSize() - 1));
            }
        }

        @Override
        public Object getElementAt(final int index) {
            return records.get(index);
        }

                @Override
                public int getSize() {
                    return records.size();
                }
    }

    /**
     * Flushes every #FLUSH_RATE (milliseconds)
     */
    private class LogQueue implements Runnable {

        public static final int FLUSH_RATE = 1000;
        private final Object lock = new Object();
        private List<WrappedLogRecord> queue = new ArrayList<>(
                100);

        public void queue(final WrappedLogRecord record) {
            synchronized (lock) {
                queue.add(record);
            }
        }

        @Override
        public void run() {
            while (true) {
                List<WrappedLogRecord> toFlush = null;
                
                synchronized (lock) {
                    if (!queue.isEmpty()) {
                        toFlush = new ArrayList<>(queue);
                        queue = queue.subList(0, 0);
                    }
                }
                if (toFlush != null) { // Hold the lock for as little time as
                    // possible
                    model.addAllElements(toFlush);
                    SwingUtilities.invokeLater(scrollToBottom);
                }
                try {
                    Thread.sleep(LogQueue.FLUSH_RATE);
                } catch (final InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Wrap the log records so we can control the copy paste text (via
     * #toString)
     */
    private class WrappedLogRecord {

        public final LogRecord record;
        public final String formatted;

        WrappedLogRecord(final LogRecord record) {
            this.record = record;
            formatted = LogTextArea.formatter.format(record);
        }

        @Override
        public String toString() {
            return LogTextArea.copyPasteFormatter.format(record);
        }
    }
}