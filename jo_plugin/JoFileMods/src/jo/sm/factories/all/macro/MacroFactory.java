
package jo.sm.factories.all.macro;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.utils.FileUtils;
import jo.sm.logic.utils.IntegerUtils;
import jo.sm.logic.utils.StringUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IStarMadePlugin;
import jo.sm.mods.IStarMadePluginFactory;
import jo.util.Paths;


public class MacroFactory implements IStarMadePluginFactory {
    private static final Logger log = Logger.getLogger(MacroFactory.class.getName());

    private long mLastLoad;
    private File mMacroDir;
    private final List<MacroDefinition> mDefs;
    private final List<IStarMadePlugin> mPlugins;

    public MacroFactory() {
        this.mDefs = new ArrayList<>();
        this.mPlugins = new ArrayList<>();
    }

    @Override
    public IStarMadePlugin[] getPlugins() {
        updateDefinitions();
        return mPlugins.toArray(new IStarMadePlugin[0]);
    }

    private void updatePlugins() {
        for (MacroDefinition fd : mDefs) {
            mPlugins.add(new MacroPlugin(fd));
        }
    }

    private void updateDefinitions() {
        if (mMacroDir == null) {
            File plugins = new File(Paths.getPluginsDirectory());
            mMacroDir = new File(plugins, "macros");
        }
        if (mMacroDir.lastModified() < mLastLoad) {
            return;
        }
        mDefs.clear();
        mPlugins.clear();
        if (!mMacroDir.exists()) {
            return;
        }
        File[] macros = mMacroDir.listFiles();
        if (macros == null) {
            return;
        }
        for (File macro : macros) {
            addMacro(macro);
        }
        updatePlugins();
    }

    private void addMacro(File macroFile) {
        try {
            if (!macroFile.getName().endsWith(".js")) {
                return;
            }
            String macroText = FileUtils.readFileAsString(macroFile.toString());
            MacroDefinition def = new MacroDefinition();
            def.setScript(macroFile);
            def.setTitle(StringUtils.extract(macroText, "Name:", "\n").trim());
            if (StringUtils.isTrivial(def.getTitle())) {
                return;
            }
            def.setDescription(StringUtils.extract(macroText, "DescriptionStart", "DescriptionEnd").trim());
            def.setAuthor(StringUtils.extract(macroText, "Author:", "\n").trim());
            List<String> sClassifications = new ArrayList<>();
            extractArray(macroText, "Classification:", "\n", sClassifications);
            List<int[]> aClassifications = new ArrayList<>();
            for (String classification : sClassifications) {
                classification = classification.trim();
                StringTokenizer st = new StringTokenizer(classification, " ");
                if (st.countTokens() < 2) {
                    continue;
                }
                int enablement = parseEnablement(st.nextToken());
                if (enablement < 0) {
                    continue;
                }
                int placement = parsePlacement(st.nextToken());
                if (placement < 0) {
                    continue;
                }
                if (st.hasMoreTokens()) {
                    aClassifications.add(new int[]{enablement, placement, IntegerUtils.parseInt(st.nextToken())});
                } else {
                    aClassifications.add(new int[]{enablement, placement});
                }
            }
            if (aClassifications.isEmpty()) {
                return;
            }
            def.setClassifications(aClassifications.toArray(new int[0][]));
            mDefs.add(def);
        } catch (IOException e) {
            log.log(Level.WARNING, "macroFile failed!", e);
        }
    }

    private void extractArray(String macroText, String prefix,
            String suffix, List<String> array) {
        int o = macroText.indexOf(prefix);
        if (o < 0) {
            return;
        }
        macroText = macroText.substring(o + prefix.length());
        o = macroText.indexOf(suffix);
        if (o < 0) {
            return;
        }
        array.add(macroText.substring(0, o));
        macroText = macroText.substring(o + suffix.length());
        extractArray(macroText, prefix, suffix, array);
    }

    private int parseEnablement(String str) {
        if (str.equalsIgnoreCase("all")) {
            return IBlocksPlugin.TYPE_ALL;
        }
        if (str.equalsIgnoreCase("floatingrock")) {
            return IBlocksPlugin.TYPE_FLOATINGROCK;
        }
        if (str.equalsIgnoreCase("planet")) {
            return IBlocksPlugin.TYPE_PLANET;
        }
        if (str.equalsIgnoreCase("ship")) {
            return IBlocksPlugin.TYPE_SHIP;
        }
        if (str.equalsIgnoreCase("shop")) {
            return IBlocksPlugin.TYPE_SHOP;
        }
        if (str.equalsIgnoreCase("station")) {
            return IBlocksPlugin.TYPE_STATION;
        }
        return -1;
    }

    private int parsePlacement(String str) {
        if (str.equalsIgnoreCase("edit")) {
            return IBlocksPlugin.SUBTYPE_EDIT;
        }
        if (str.equalsIgnoreCase("file")) {
            return IBlocksPlugin.SUBTYPE_FILE;
        }
        if (str.equalsIgnoreCase("generate")) {
            return IBlocksPlugin.SUBTYPE_GENERATE;
        }
        if (str.equalsIgnoreCase("modify")) {
            return IBlocksPlugin.SUBTYPE_MODIFY;
        }
        if (str.equalsIgnoreCase("paint")) {
            return IBlocksPlugin.SUBTYPE_PAINT;
        }
        if (str.equalsIgnoreCase("view")) {
            return IBlocksPlugin.SUBTYPE_VIEW;
        }
        return -1;
    }
}
