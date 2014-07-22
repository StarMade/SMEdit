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

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Auther Robert Barefoot for SMEdit - version 1.0
 **/
public class LogOutputStream extends OutputStream {

    protected boolean hasBeenClosed = false;
    protected Logger category;
    protected Level priority;
    protected StringBuilder buffer;

    public LogOutputStream(final Logger category, final Level priority) {
        this.priority = priority;
        this.category = category;
        buffer = new StringBuilder();
    }

    @Override
    public void close() {
        flush();
        hasBeenClosed = true;
    }

    @Override
    public void flush() {
        final String txt = buffer.toString().replace("\\s+$", "");
        if (txt.trim().length() != 0) {
            category.log(priority, txt);
        }
        reset();
    }

    private void reset() {
        buffer.setLength(0);
    }

    @Override
    public void write(final int b) throws IOException {
        if (hasBeenClosed) {
            throw new IOException("The stream has been closed.");
        } else if (b != 0) {
            buffer.append((char) (b & 0xff));
        }
    }
}
