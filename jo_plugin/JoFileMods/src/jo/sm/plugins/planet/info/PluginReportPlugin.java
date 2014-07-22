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
package jo.sm.plugins.planet.info;

import java.awt.Desktop;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ui.act.plugin.DescribedBeanInfo;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class PluginReportPlugin implements IBlocksPlugin {

    public static final String NAME = "Plugin Report";
    public static final String DESC = "Report on all plugins";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_EDIT, 95},
                {TYPE_STATION, SUBTYPE_EDIT, 95},
                {TYPE_SHOP, SUBTYPE_EDIT, 95},
                {TYPE_FLOATINGROCK, SUBTYPE_EDIT, 95},
                {TYPE_PLANET, SUBTYPE_EDIT, 95},};

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     *
     * @return
     */
    @Override
    public String getDescription() {
        return DESC;
    }

    /**
     *
     * @return
     */
    @Override
    public String getAuthor() {
        return AUTH;
    }

    /**
     *
     * @return
     */
    @Override
    public Object newParameterBean() {
        return null;
    }

    /**
     *
     * @param original
     * @param params
     * @param sm
     * @param cb
     */
    @Override
    public void initParameterBean(SparseMatrix<Block> original, Object params,
            StarMade sm, IPluginCallback cb) {
    }

    /**
     *
     * @return
     */
    @Override
    public int[][] getClassifications() {
        return CLASSIFICATIONS;
    }

    /**
     *
     * @param original
     * @param p
     * @param sm
     * @param cb
     * @return
     */
    @Override
    public SparseMatrix<Block> modify(SparseMatrix<Block> original,
            Object p, StarMade sm, IPluginCallback cb) {
        try {
            File repFile;
            repFile = File.createTempFile("smReport", ".htm");
            try (PrintWriter wtr = new PrintWriter(repFile)) {
                wtr.println("<html>");
                wtr.println("<body>");
                reportBlockPlugins(wtr, sm);
                wtr.println("</body>");
                wtr.println("</html>");
            }
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(repFile);
            }
        } catch (IOException e) {
            cb.setError(e);
        }
        return null;
    }

    private void reportBlockPlugins(PrintWriter wtr, StarMade sm) {
        wtr.println("<h1>BLOCK PLUGINS</h1>");
        for (IBlocksPlugin plugin : sm.getBlocksPlugins()) {
            Object params;
            params = plugin.newParameterBean();
            wtr.println("<h3>" + plugin.getName() + "</h3>");
            wtr.println("Author: <b>" + plugin.getAuthor() + "</b><br/>");
            wtr.println("<i>" + plugin.getDescription() + "</i><br/>");
            wtr.println("Classifications:<br/>");
            wtr.println("<table>");
            for (int[] classification : plugin.getClassifications()) {
                wtr.println("<tr>");
                wtr.println("<td>");
                switch (classification[0]) {
                    case TYPE_ALL:
                        wtr.print("All ");
                        break;
                    case TYPE_FLOATINGROCK:
                        wtr.print("Floating Rock ");
                        break;
                    case TYPE_PLANET:
                        wtr.print("Planet ");
                        break;
                    case TYPE_SHIP:
                        wtr.print("Ship ");
                        break;
                    case TYPE_SHOP:
                        wtr.print("Shop ");
                        break;
                    case TYPE_STATION:
                        wtr.print("Station ");
                        break;
                    default:
                        wtr.print(classification[0] + " ");
                        break;
                }
                wtr.println("</td>");
                wtr.println("<td>");
                switch (classification[1]) {
                    case SUBTYPE_EDIT:
                        wtr.print("- Edit ");
                        break;
                    case SUBTYPE_FILE:
                        wtr.print("- File ");
                        break;
                    case SUBTYPE_GENERATE:
                        wtr.print("- Generate ");
                        break;
                    case SUBTYPE_MODIFY:
                        wtr.print("- Modify ");
                        break;
                    case SUBTYPE_PAINT:
                        wtr.print("- Paint ");
                        break;
                    case SUBTYPE_VIEW:
                        wtr.print("- Paint ");
                        break;
                    default:
                        wtr.print("- " + classification[1] + " ");
                        break;
                }
                wtr.println("</td>");
                wtr.println("<td>");
                if (classification.length > 2) {
                    wtr.print("(" + classification[2] + ")");
                }
                wtr.println("</td>");
                wtr.println("</tr>");
            }
            wtr.println("</table>");
            if (params == null) {
            } else {
                DescribedBeanInfo info;
                info = new DescribedBeanInfo(plugin.newParameterBean());
                if (info.getOrderedProps().size() > 0) {
                    wtr.println("Parameters:<br/>");
                    wtr.println("<table>");
                    for (PropertyDescriptor prop : info.getOrderedProps()) {
                        wtr.println("<tr>");
                        wtr.println("<td>");
                        wtr.println(prop.getName());
                        wtr.println("</td>");
                        wtr.println("<td>");
                        wtr.println(prop.getDisplayName());
                        wtr.println("</td>");
                        wtr.println("<td>");
                        wtr.println(prop.getShortDescription());
                        wtr.println("</td>");
                        wtr.println("<td>");
                        wtr.println(prop.getPropertyType().getSimpleName());
                        wtr.println("</td>");
                        wtr.println("</tr>");
                    }
                    wtr.println("</table>");
                }
            }
            wtr.println("<hr/>");
        }
    }
}
