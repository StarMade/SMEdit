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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * @author Robert Barefoot
 */
public abstract class Base extends AbstractAction {

	private static final long serialVersionUID = 6858558832890334531L;

	public Base() {
	}

	public Base(final String text, final Icon icon, final String description, final char accelerator) {
		super(text, icon);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(SHORT_DESCRIPTION, description);
	}

	@Override
	public abstract void actionPerformed(ActionEvent e);
}
