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

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import jo.sm.logic.utils.StringUtils;
import jo.sm.mods.IPluginCallback;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@SuppressWarnings("serial")
public class PluginProgressDlg extends JDialog implements IPluginCallback {

    private final JLabel mMessage;
    private final JProgressBar mProgress;
    private final JButton mCancel;

    private String mErrorTitle;
    private String mErrorDescription;
    private Throwable mError;

    private int mTotalUnits;
    private int mUnitsDone;
    private boolean mPleaseCancel;

    public PluginProgressDlg(JFrame base, String title) {
        super(base, title, Dialog.ModalityType.DOCUMENT_MODAL);
        mMessage = new JLabel("");
        mProgress = new JProgressBar(0, 100);
        mProgress.setStringPainted(true);
        mProgress.setVisible(false);
        mCancel = new JButton("Cancel");
        // layout
        JPanel client = new JPanel();
        getContentPane().add(client);
        client.setLayout(new BorderLayout());
        client.add(BorderLayout.NORTH, mMessage);
        client.add(BorderLayout.CENTER, mProgress);
        client.add(BorderLayout.SOUTH, mCancel);
        //links
        mCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mPleaseCancel = true;
            }
        });
        setSize(320, 240);
        setLocationRelativeTo(base);
    }

    private void updateProgress() {
        mProgress.setMaximum(mTotalUnits);
        mProgress.setValue(mUnitsDone);
    }

    @Override
    public void setStatus(String status) {
        System.out.println(status);
        mMessage.setText(status);
    }

    @Override
    public void startTask(int size) {
        mTotalUnits = size;
        mUnitsDone = 0;
        mProgress.setVisible(true);
        updateProgress();
    }

    @Override
    public void workTask(int amnt) {
        mUnitsDone += amnt;
        updateProgress();
        if (mPleaseCancel) {
            throw new IllegalStateException("Operation canceled at user request");
        }
    }

    @Override
    public void endTask() {
        mUnitsDone = mTotalUnits;
        updateProgress();
        mProgress.setVisible(false);
    }

    @Override
    public boolean isPleaseCancel() {
        return mPleaseCancel;
    }

    public void setPleaseCancel(boolean pleaseCancel) {
        mPleaseCancel = pleaseCancel;
    }

    public String getErrorTitle() {
        return mErrorTitle;
    }

    @Override
    public void setErrorTitle(String errorTitle) {
        mErrorTitle = errorTitle;
    }

    public String getErrorDescription() {
        return mErrorDescription;
    }

    @Override
    public void setErrorDescription(String errorDescription) {
        mErrorDescription = errorDescription;
    }

    public Throwable getError() {
        return mError;
    }

    @Override
    public void setError(Throwable error) {
        mError = error;
        if (StringUtils.isTrivial(mErrorTitle)) {
            setTitle(mError.toString());
        }
    }
}
