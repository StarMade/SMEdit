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

import javax.swing.JFileChooser;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class FilePropertyInfo {

    private String mApproveButtonText;
    private String mApproveButtonTooltipText;
    private String mDialogTitle;
    private int mDialogType;
    private int mFileSelectionMode;
    private String[][] mFilters;

    public FilePropertyInfo() {
        mDialogType = JFileChooser.OPEN_DIALOG;
        mFileSelectionMode = JFileChooser.FILES_ONLY;
    }

    public String[][] getFilters() {
        return mFilters;
    }

    public void setFilters(String[][] filters) {
        mFilters = filters;
    }

    public String getApproveButtonText() {
        return mApproveButtonText;
    }

    public void setApproveButtonText(String approveButtonText) {
        mApproveButtonText = approveButtonText;
    }

    public String getApproveButtonTooltipText() {
        return mApproveButtonTooltipText;
    }

    public void setApproveButtonTooltipText(String approveButtonTooltipText) {
        mApproveButtonTooltipText = approveButtonTooltipText;
    }

    public String getDialogTitle() {
        return mDialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        mDialogTitle = dialogTitle;
    }

    public int getDialogType() {
        return mDialogType;
    }

    public void setDialogType(int dialogType) {
        mDialogType = dialogType;
    }

    public int getFileSelectionMode() {
        return mFileSelectionMode;
    }

    public void setFileSelectionMode(int fileSelectionMode) {
        mFileSelectionMode = fileSelectionMode;
    }
}
