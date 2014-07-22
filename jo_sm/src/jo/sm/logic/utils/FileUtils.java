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
package jo.sm.logic.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class FileUtils {

    public static void writeFile(byte[] data, File f) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(f)) {
            StreamUtils.writeStream(data, fos);
        }
    }

    public static void writeFile(String data, File f) throws IOException {
        try (FileWriter fw = new FileWriter(f)) {
            fw.write(data);
        }
    }

    public static byte[] readFile(String fname) throws IOException {
        byte[] ret;
        try (FileInputStream fis = new FileInputStream(fname)) {
            ret = StreamUtils.readStream(fis);
        }
        return ret;
    }

    public static byte[] readFile(String fname, int limit) throws IOException {
        byte[] ret;
        try (FileInputStream fis = new FileInputStream(fname)) {
            ret = StreamUtils.readStream(fis, limit);
        }
        return ret;
    }

    public static String readFileAsString(String fname) throws IOException {
        return new String(readFile(fname));
    }

    public static String readFileAsString(String fname, int limit) throws IOException {
        return new String(readFile(fname, limit));
    }

    public static String readFileAsString(String fname, String charset) throws IOException {
        return new String(readFile(fname), charset);
    }

    public static void copy(File in, File out) throws IOException {
        OutputStream os;
        try (InputStream is = new FileInputStream(in)) {
            os = new FileOutputStream(out);
            StreamUtils.copy(is, os);
        }
        os.close();
    }

    public static boolean isIdentical(File file1, File file2) {
        if (file1.length() != file2.length()) {
            return false;
        }
        InputStream is1;
        InputStream is2;
        try {
            is1 = new BufferedInputStream(new FileInputStream(file1));
            is2 = new BufferedInputStream(new FileInputStream(file2));
            long max = Math.min(1024 * 16, file1.length());
            while (max-- > 0) {
                int ch1 = is1.read();
                int ch2 = is2.read();
                if (ch1 != ch2) {
                    is1.close();
                    is2.close();
                    return false;
                }
            }
            is1.close();
            is2.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static void rmdir(File f) {
        if (f.isFile()) {
            f.delete();
        } else {
            if (f.getName().equals(".") || f.getName().equals("..")) {
                return;
            }
            File[] children = f.listFiles();
            for (File children1 : children) {
                rmdir(children1);
            }
            f.delete();
        }
    }
}
