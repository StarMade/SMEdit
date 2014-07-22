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
package jo.sm.ui.act;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import jo.sm.data.SparseMatrix;
import jo.sm.logic.DraftImageLogic;
import jo.sm.logic.RunnableLogic;
import jo.sm.logic.StarMadeLogic;
import jo.sm.mods.IPluginCallback;
import jo.sm.mods.IRunnableWithProgress;
import jo.sm.ship.data.Block;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.logic.ShipSpec;
import jo.util.Paths;


/**
 * @author Robert Barefoot
 */
public class Shot extends Base {

	private static final long serialVersionUID = 89567527547437753L;
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
        private final RenderFrame mFrame;

	public Shot(RenderFrame frame) {
            mFrame = frame;
	}

	@Override
	public void actionPerformed(final java.awt.event.ActionEvent e) {
		doSaveFile();
	}
        
        private void doSaveFile() {
        final ShipSpec spec = StarMadeLogic.getInstance().getCurrentModel();
        final SparseMatrix<Block> original = StarMadeLogic.getModel();
        final String name = dateFormat.format(new Date());
        final File dir = new File(Paths.getScreenshotsDirectory());
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        IRunnableWithProgress t = new IRunnableWithProgress() {
            @Override
            public void run(IPluginCallback cb) {
                try {
                    DraftImageLogic.saveDrafImages(dir, name, new Dimension(1024, 1024), original, cb);
                } catch (IOException e) {
                    throw new IllegalStateException("Cannot save to location", e);
                }
            }
        };
        RunnableLogic.run(mFrame, spec.getName(), t);
    }
}
