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
package jo.sm.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IStarMadePluginFactory;
import jo.sm.ship.data.Block;
import jo.sm.ui.logic.ShipSpec;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class StarMade extends PCSBean {

    private File mBaseDir;
    private List<String> mBlueprints;
    private List<String> mDefaultBlueprints;
    private List<Entity> mEntities;
    private ClassLoader mModLoader;
    private List<IBlocksPlugin> mBlocksPlugins;
    private List<IStarMadePluginFactory> mPluginFactories;
    private Properties mProps;
    private short mSelectedBlockType;
    private Point3i mSelectedUpper;
    private Point3i mSelectedLower;
    private String mStatusMessage;
    private IBlocksPlugin mViewFilter;
    private ShipSpec mCurrentModel;
    private SparseMatrix<Block> mModel;

    public StarMade() {
        mBlocksPlugins = new ArrayList<>();
        mPluginFactories = new ArrayList<>();
        mSelectedBlockType = -1;
    }

    public File getBaseDir() {
        return mBaseDir;
    }

    public void setBaseDir(File baseDir) {
        mBaseDir = baseDir;
    }

    public List<String> getBlueprints() {
        return mBlueprints;
    }

    public void setBlueprints(List<String> blueprints) {
        mBlueprints = blueprints;
    }

    public List<String> getDefaultBlueprints() {
        return mDefaultBlueprints;
    }

    public void setDefaultBlueprints(List<String> defaultBlueprints) {
        mDefaultBlueprints = defaultBlueprints;
    }

    public List<Entity> getEntities() {
        return mEntities;
    }

    public void setEntities(List<Entity> entities) {
        mEntities = entities;
    }

    public ClassLoader getModLoader() {
        return mModLoader;
    }

    public void setModLoader(ClassLoader modLoader) {
        mModLoader = modLoader;
    }

    public List<IBlocksPlugin> getBlocksPlugins() {
        return mBlocksPlugins;
    }

    public void setBlocksPlugins(List<IBlocksPlugin> blocksPlugins) {
        mBlocksPlugins = blocksPlugins;
    }

    public Properties getProps() {
        return mProps;
    }

    public void setProps(Properties props) {
        mProps = props;
    }

    public short getSelectedBlockType() {
        return mSelectedBlockType;
    }

    public void setSelectedBlockType(short selectedBlockType) {
        mSelectedBlockType = selectedBlockType;
    }

    public Point3i getSelectedUpper() {
        return mSelectedUpper;
    }

    public void setSelectedUpper(Point3i selectedUpper) {
        mSelectedUpper = selectedUpper;
    }

    public Point3i getSelectedLower() {
        return mSelectedLower;
    }

    public void setSelectedLower(Point3i selectedLower) {
        mSelectedLower = selectedLower;
    }

    public String getStatusMessage() {
        return mStatusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        queuePropertyChange("statusMessage", mStatusMessage, statusMessage);
        mStatusMessage = statusMessage;
        firePropertyChange();
    }

    public IBlocksPlugin getViewFilter() {
        return mViewFilter;
    }

    public void setViewFilter(IBlocksPlugin viewFilter) {
        mViewFilter = viewFilter;
    }

    public ShipSpec getCurrentModel() {
        return mCurrentModel;
    }

    public void setCurrentModel(ShipSpec currentModel) {
        queuePropertyChange("currentModel", mCurrentModel, currentModel);
        mCurrentModel = currentModel;
        firePropertyChange();
    }

    public SparseMatrix<Block> getModel() {
        return mModel;
    }

    public void setModel(SparseMatrix<Block> model) {
        queuePropertyChange("model", mModel, model);
        mModel = model;
        firePropertyChange();
    }

    public List<IStarMadePluginFactory> getPluginFactories() {
        return mPluginFactories;
    }

    public void setPluginFactories(List<IStarMadePluginFactory> pluginFactories) {
        mPluginFactories = pluginFactories;
    }
}
