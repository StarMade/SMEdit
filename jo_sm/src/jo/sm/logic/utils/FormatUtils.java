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

import java.util.Calendar;
import java.util.Date;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FormatUtils {

    public static int parseInt(Object asc) {
        return parseInt(asc, 0);
    }

    public static int parseInt(Object asc, int def) {
        if (asc == null) {
            return def;
        }
        try {
            return Integer.parseInt(asc.toString().trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static long parseLong(Object asc) {
        if (asc == null) {
            return 0;
        }
        try {
            return Long.parseLong(asc.toString().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static double parseDouble(Object asc) {
        if (asc == null) {
            return 0;
        }
        try {
            return Double.parseDouble(asc.toString().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String diceRoll(int n, int d, int m) {
        StringBuilder ret = new StringBuilder();
        if (n > 0) {
            ret.append(n);
        }
        ret.append("d");
        ret.append(d);
        if (m > 0) {
            ret.append("+");
            ret.append(m);
        } else if (m < 0) {
            ret.append(m);
        }
        return ret.toString();
    }

    public static String formatDate(long ticks) {
        Date d = new Date(ticks);
        return d.toString();
    }

    public static String formatDateShort(long ticks) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(ticks);
        StringBuilder sb = new StringBuilder();
        sb.append(zeroPrefix(c.get(Calendar.DAY_OF_MONTH), 2));
        sb.append(" ");
        sb.append(monthShort(c.get(Calendar.MONTH)));
        sb.append(" ");
        sb.append(zeroPrefix(c.get(Calendar.YEAR) % 100, 2));
        sb.append(" ");
        sb.append(zeroPrefix(c.get(Calendar.HOUR_OF_DAY) % 100, 2));
        sb.append(":");
        sb.append(zeroPrefix(c.get(Calendar.MINUTE) % 100, 2));

        return sb.toString();
    }

    public static String prefix(String str, int width, String with) {
        StringBuilder ret = new StringBuilder(str);
        while (ret.length() < width) {
            ret.insert(0, with);
        }
        return ret.toString();
    }

    public static String suffix(String str, int width, String with) {
        StringBuilder ret = new StringBuilder(str);
        while (ret.length() < width) {
            ret.append(with);
        }
        return ret.toString();
    }

    public static String zeroPrefix(int val, int width) {
        return prefix(String.valueOf(val), width, "0");
    }

    public static String leftJustify(String str, int width) {
        return suffix(str, width, " ");
    }

    public static String rightJustify(String str, int width) {
        return prefix(str, width, " ");
    }

    public static String monthShort(int month) {
        switch (month) {
            case Calendar.JANUARY:
                return "Jan";
            case Calendar.FEBRUARY:
                return "Feb";
            case Calendar.MARCH:
                return "Mar";
            case Calendar.APRIL:
                return "Apr";
            case Calendar.MAY:
                return "May";
            case Calendar.JUNE:
                return "Jun";
            case Calendar.JULY:
                return "Jul";
            case Calendar.AUGUST:
                return "Aug";
            case Calendar.SEPTEMBER:
                return "Sep";
            case Calendar.OCTOBER:
                return "Oct";
            case Calendar.NOVEMBER:
                return "Nov";
            case Calendar.DECEMBER:
                return "Dec";
        }
        return "???";
    }

    public static String formatPercent(double pc) {
        String ret = String.valueOf(pc);
        int i = ret.indexOf(".");
        if (i < ret.length() - 2) {
            ret = ret.substring(0, i + 2);
        }
        ret += "%";
        return ret;
    }

    public static String formatDouble(double v, int decimalPlaces) {
        String ret = String.valueOf(v);
        int i = ret.indexOf(".");
        if (ret.length() > i + decimalPlaces + 1) {
            ret = ret.substring(0, i + decimalPlaces + 1);
        } else {
            ret += "0000000000".substring(0, i + decimalPlaces + 1 - ret.length());
        }
        return ret;
    }

    public static final String CURRENCY_SYMBOL = "$";

    public static String formatMoney(int dosh) {
        String ret = CURRENCY_SYMBOL + String.valueOf(dosh);
        return ret;
    }
}
