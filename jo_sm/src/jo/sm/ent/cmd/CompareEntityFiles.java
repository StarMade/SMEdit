package jo.sm.ent.cmd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jo.sm.logic.StarMadeLogic;

import jo.sm.logic.utils.ByteUtils;
import jo.sm.logic.utils.FileUtils;

public class CompareEntityFiles {

    private final File mModifiedDir;
    private final File mOriginalDir;
    private final Map<Byte, List<DiffReport>> mDiffs;

    public CompareEntityFiles() {

        mModifiedDir = new File(StarMadeLogic.getInstance().getBaseDir(), "/server-database");
        mOriginalDir = new File(StarMadeLogic.getInstance().getBaseDir(), "/tmp");
        mDiffs = new HashMap<>();
    }

    public void run() throws IOException {
        for (File f1 : mModifiedDir.listFiles()) {
            if (!f1.getName().endsWith(".ent")) {
                continue;
            }
            File f2 = new File(mOriginalDir, f1.getName());
            if (!f2.exists()) {
                continue;
            }
            byte[] data1 = FileUtils.readFile(f1.toString());
            byte[] data2 = FileUtils.readFile(f2.toString());
            System.out.println(f1.getName() + ":");
            compare(data1, data2);
        }
        System.out.println();
        System.out.println("Difference Report");
        System.out.println();
        for (Byte header : mDiffs.keySet()) {
            System.out.println("HEADER: " + Integer.toHexString(header));
            List<DiffReport> reps = mDiffs.get(header);
            for (DiffReport rep : reps) {
                System.out.println("\t" + rep.mOriginalLen + "\t" + rep.mModifiedLen);
            }
            System.out.println();
        }
    }

    private void compare(byte[] data1, byte[] data2) {
        int start;
        for (start = 0; start < data1.length; start++) {
            if (start >= data2.length) {
                System.out.println("Modified longer than original");
                return;
            }
            if (data1[start] != data2[start]) {
                break;
            }
        }
        if (start == data1.length) {
            if (start == data2.length) {
                System.out.println("Files identical");
            } else {
                System.out.println("Original longer than modified");
            }
            return;
        }
        int end1 = -1;
        int end2 = -1;
        for (int maxReach = 1; maxReach < 256; maxReach++) {
            for (int altReach = 0; altReach < maxReach; altReach++) {
                if (equals(data1, start + maxReach, data2, start + altReach, 32)) {
                    end1 = start + maxReach;
                    end2 = start + altReach;
                    break;
                }
                if (equals(data1, start + altReach, data2, start + maxReach, 32)) {
                    end1 = start + altReach;
                    end2 = start + maxReach;
                    break;
                }
            }
            if (end1 >= 0) {
                break;
            }
        }
        if (end1 < 0) {
            System.out.println("Don't know where end is!");
            return;
        }
        if (start >= 32) {
            System.out.println("Before:\n" + ByteUtils.toStringDump(data1, start - 32, 32));
        } else {
            System.out.println("Before:\n" + ByteUtils.toStringDump(data1, 0, start));
        }
        System.out.println("Modified:\n" + ByteUtils.toStringDump(data1, start, end1 - start));
        System.out.println("Original:\n" + ByteUtils.toStringDump(data2, start, end2 - start));
        System.out.println("After:\n" + ByteUtils.toStringDump(data1, end1, 32));
        DiffReport r = new DiffReport();
        r.mHeader = data1[start];
        r.mModifiedLen = end1 - start;
        r.mOriginalLen = end2 - start;
        addReport(r);
    }

    private void addReport(DiffReport r) {
        List<DiffReport> diffs = mDiffs.get(r.mHeader);
        if (diffs == null) {
            diffs = new ArrayList<>();
            mDiffs.put(r.mHeader, diffs);
        }
        diffs.add(r);
    }

    private boolean equals(byte[] data1, int o1, byte[] data2, int o2, int len) {
        if (o1 + len > data1.length) {
            return false;
        }
        if (o2 + len > data2.length) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (data1[o1 + i] != data2[o2 + i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        CompareEntityFiles app = new CompareEntityFiles();
        try {
            app.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class DiffReport {

        byte mHeader;
        int mModifiedLen;
        int mOriginalLen;
    }
}
