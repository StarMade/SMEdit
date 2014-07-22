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
package jo.sm.ui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BegPanel extends JPanel {

    private static final int TICK = 200;
    private static final int CHOP = 120;

    public static final String THE_RAIDERS_LAMENT_AUDIO = "http://podiobooks.com/title/the-raiders-lament";
    public static final String THE_RAIDERS_LAMENT = "https://www.smashwords.com/books/view/347157";
    public static final String DOCUMENTATION = "http://www.starmadewiki.com/wiki/SMEdit";

    private int mMessageOffset;
    private int mRepeats;

    private final JLabel mStatus;
    private final JButton mAudio;
    private final JButton mText;

    public BegPanel() {
        mRepeats = 3;
        // instantiate
        mStatus = new JLabel(MESSAGE.substring(0, CHOP));
        setBackground(Color.cyan);
        mAudio = new JButton("Audiobook");
        mText = new JButton("E-book");
        Dimension d1 = mAudio.getPreferredSize();
        Dimension d2 = mText.getPreferredSize();
        mStatus.setPreferredSize(new Dimension(1024 - d1.width - d2.width, Math.max(d1.height, d2.height)));
        // layout
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add("Center", mStatus);
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 2));
        buttons.add(mAudio);
        buttons.add(mText);
        add("East", buttons);
        // link
        Thread t = new Thread("beg_ticker") {
            @Override
            public void run() {
                doTicker();
            }
        };
        t.setDaemon(true);
        t.start();
        mText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                doGoto(THE_RAIDERS_LAMENT);
            }
        });
        mText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                doGoto(THE_RAIDERS_LAMENT_AUDIO);
            }
        });
    }

    private void doTicker() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        for (;;) {
            try {
                Thread.sleep(TICK);
            } catch (InterruptedException e) {
            }
            mMessageOffset++;
            if (mMessageOffset == MESSAGE.length()) {
                mMessageOffset = 0;
                mRepeats--;
                if (mRepeats < 0) {
                    return;
                }
            }
            String msg = MESSAGE.substring(mMessageOffset) + MESSAGE.substring(0, mMessageOffset);
            msg = msg.substring(0, CHOP);
            mStatus.setText(msg);
        }
    }

    private void doGoto(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Action.BROWSE)) {
                try {
                    desktop.browse(URI.create(url));
                    return;
                } catch (IOException e) {
                    // handled below
                }
            }
        }
    }

    public static final String MESSAGE = "This software is made freely available with no charge or limitation. "
            + "Even the source is included. "
            + "It was originally distributed as \"begware\", promoting my book \"The Raider's Lament\". "
            + "I wanted enough downloads/sales to earn enough to buy a Minecraft Lego kit for my daughter. "
            + "The good people of this community helped me reach the target, and a huge shout out to Kahulbane"
            + " for donating quite a bit of it! "
            + "So I've now officially considered this software 'paid for' and have removed the begware nagger. "
            + "If you are interested you can still download my book. I'd appreciate it, even more if you read it "
            + "and review it. You can still choose to donate by buying. Further proceeds will go towards buying "
            + "the other Minecraft kids for my daughter! "
            + "The buttons below will take you to the audiobook page (free) and the"
            + "eBook page (first 20%, the full book for $.99. "
            + "Thank you for using and supporting SMEdit. ";
}
