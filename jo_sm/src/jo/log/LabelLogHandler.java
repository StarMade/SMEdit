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

import java.awt.Color;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.swing.JLabel;

/**
 * @Auther Robert Barefoot for SMEdit - version 1.0
 **/
public class LabelLogHandler extends Handler {

    public final JLabel label = new JLabel();
    private final Color defaultColor;

    public LabelLogHandler() {
        super();
        defaultColor = label.getForeground();
    }

    @Override
    public void close() throws SecurityException {
    }

    @Override
    public void flush() {
    }

    @Override
    public void publish(final LogRecord record) {
        String msg = record.getMessage();
        if (record.getLevel().intValue() > Level.WARNING.intValue()) {
            label.setForeground(new Color(0xcc0000));
        } else {
            label.setForeground(defaultColor);
            msg += " ...";
        }
        label.setText(msg);
    }

}
