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
package jo.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.tools.ToolProvider;

import jo.util.GlobalConfiguration;
import jo.util.OperatingSystem;
import jo.util.Paths;

/**
 * @author Robert Barefoot - version 1.0
 */
public class JavaCompiler {

    private final static String JAVACARGS = "-g:none";

    private static int compileNative(final javax.tools.JavaCompiler javac,
            final InputStream source, final String classPath)
            throws FileNotFoundException {
        final FileOutputStream[] out = new FileOutputStream[2];
        for (int i = 0; i < 2; i++) {
            out[i] = new FileOutputStream(new File(
                    Paths.getCollectDirectory(), "compile."
                    + Integer.toString(i) + ".txt"));
        }
        return javac.run(source, out[0], out[1], JAVACARGS, "-cp", classPath);
    }

    private static void compileSystem(final File source, final String classPath)
            throws IOException {
        final String javac = findJavac();
        if (javac == null) {
            throw new IOException();
        }
        Runtime.getRuntime().exec(
                new String[]{javac, JAVACARGS, "-cp", classPath,
                    source.getAbsolutePath()});
    }

    public static boolean compileWeb(final String source, final File out) {
        try {
            HttpClient.download(
                    new URL(
                            source
                            + "?v="
                            + Integer.toString(GlobalConfiguration
                                    .getVersion()) + "&s="
                            + URLEncoder.encode(source, "UTF-8")), out);
        } catch (final IOException ignored) {
            return false;
        }
        if (out.length() == 0) {
            out.delete();
        }
        return out.exists();
    }

    private static String findJavac() {
        try {
            if (GlobalConfiguration.getCurrentOperatingSystem() == OperatingSystem.WINDOWS) {
                String currentVersion = readProcess("REG QUERY \"HKLM\\SOFTWARE\\JavaSoft\\Java Development Kit\" /v CurrentVersion");
                currentVersion = currentVersion.substring(
                        currentVersion.indexOf("REG_SZ") + 6).trim();
                String binPath = readProcess("REG QUERY \"HKLM\\SOFTWARE\\JavaSoft\\Java Development Kit\\"
                        + currentVersion + "\" /v JavaHome");
                binPath = binPath.substring(binPath.indexOf("REG_SZ") + 6)
                        .trim() + "\\bin\\javac.exe";
                return new File(binPath).exists() ? binPath : null;
            } else {
                final String whichQuery = readProcess("which javac");
                return whichQuery == null || whichQuery.length() == 0 ? null
                        : whichQuery.trim();
            }
        } catch (final IOException ignored) {
            return null;
        }
    }

    public static boolean isAvailable() {
        return !(ToolProvider.getSystemJavaCompiler() == null && findJavac() == null);
    }

    private static String readProcess(final String exec) throws IOException {
        final Process compiler = Runtime.getRuntime().exec(exec);
        final InputStream is = compiler.getInputStream();
        try {
            compiler.waitFor();
        } catch (final InterruptedException ignored) {
            return null;
        }
        final StringBuilder result = new StringBuilder(256);
        int r;
        while ((r = is.read()) != -1) {
            result.append((char) r);
        }
        return result.toString();
    }

    public static boolean run(final File source, final String classPath) {
        final javax.tools.JavaCompiler javac = ToolProvider
                .getSystemJavaCompiler();
        try {
            if (javac != null) {
                return compileNative(javac, new FileInputStream(source),
                        classPath) == 0;
            } else {
                compileSystem(source, classPath);
                return true;
            }
        } catch (final IOException ignored) {
        }
        return false;
    }
}
