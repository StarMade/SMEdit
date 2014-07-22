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
 */
package jo.sm.plugins.ship.text;

import jo.sm.data.BlockTypes;
import jo.sm.ui.act.plugin.Description;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 */
@Description(displayName = "Write Text on Model", shortDescription = "Write text along the length of the selection area")
public class TextParameters {

    @Description(displayName = "Ink", shortDescription = "Color to use for lettering")
    private short mInk;
    @Description(displayName = "Font", shortDescription = "Font from your computer to draw with")
    private String mFont;
    @Description(displayName = "Boldface")
    private boolean mBold;
    @Description(displayName = "Italic")
    private boolean mItalic;
    @Description(displayName = "Size", shortDescription = "Size of font")
    private int mSize;
    @Description(displayName = "Text", shortDescription = "The text you want to draw")
    private String mText;

    public TextParameters() {
        mInk = BlockTypes.HULL_COLOR_BLACK_ID;
        mFont = "Dialog";
        mText = "Jo is Awesome";
        mBold = false;
        mItalic = false;
        mSize = 10;
    }

    public short getInk() {
        return mInk;
    }

    public void setInk(short ink) {
        mInk = ink;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getFont() {
        return mFont;
    }

    public void setFont(String font) {
        mFont = font;
    }

    public boolean isBold() {
        return mBold;
    }

    public void setBold(boolean bold) {
        mBold = bold;
    }

    public boolean isItalic() {
        return mItalic;
    }

    public void setItalic(boolean italic) {
        mItalic = italic;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
    }
}
