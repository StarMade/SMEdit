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
package jo.sm.ui.act.plugin;

import java.beans.PropertyEditorSupport;

import jo.sm.data.BlockTypes;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class ColorPropertyEditor extends PropertyEditorSupport {

    public ColorPropertyEditor() {
        super();
    }

    public ColorPropertyEditor(Object bean) {
        super(bean);
    }

    @Override
    public String getAsText() {
        Short c = (Short) getValue();
        if (c == null) {
            c = BlockTypes.HULL_COLOR_GREY_ID;
        }
        String txt = "Grey";
        if (c == BlockTypes.HULL_COLOR_GREY_ID) {
            return "Grey";
        }
        if (c == BlockTypes.HULL_COLOR_PURPLE_ID) {
            return "Purple";
        }
        if (c == BlockTypes.HULL_COLOR_BROWN_ID) {
            return "Brown";
        }
        if (c == BlockTypes.HULL_COLOR_BLACK_ID) {
            return "Black";
        }
        if (c == BlockTypes.HULL_COLOR_BLUE_ID) {
            return "Blue";
        }
        if (c == BlockTypes.HULL_COLOR_RED_ID) {
            return "Red";
        }
        if (c == BlockTypes.HULL_COLOR_GREEN_ID) {
            return "Green";
        }
        if (c == BlockTypes.HULL_COLOR_YELLOW_ID) {
            return "Yellow";
        }
        if (c == BlockTypes.HULL_COLOR_WHITE_ID) {
            return "White";
        }
        //System.out.println("Getting "+getValue()+" -> "+txt);
        return txt;
    }

    @Override
    public String[] getTags() {
        return new String[]{
            "Grey",
            "Purple",
            "Brown",
            "Black",
            "Red",
            "Blue",
            "Green",
            "Yellow",
            "White",};
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        switch (text) {
            case "Grey":
                setValue(BlockTypes.HULL_COLOR_GREY_ID);
                break;
            case "Purple":
                setValue(BlockTypes.HULL_COLOR_PURPLE_ID);
                break;
            case "Brown":
                setValue(BlockTypes.HULL_COLOR_BROWN_ID);
                break;
            case "Black":
                setValue(BlockTypes.HULL_COLOR_BLACK_ID);
                break;
            case "Red":
                setValue(BlockTypes.HULL_COLOR_RED_ID);
                break;
            case "Blue":
                setValue(BlockTypes.HULL_COLOR_BLUE_ID);
                break;
            case "Green":
                setValue(BlockTypes.HULL_COLOR_GREEN_ID);
                break;
            case "Yellow":
                setValue(BlockTypes.HULL_COLOR_YELLOW_ID);
                break;
            case "White":
                setValue(BlockTypes.HULL_COLOR_WHITE_ID);
                break;
            default:
                setValue(BlockTypes.HULL_COLOR_GREY_ID);
                break;
        }
    }
}
