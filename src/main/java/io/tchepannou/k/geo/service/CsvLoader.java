package io.tchepannou.k.geo.service;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.base.Strings;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class CsvLoader implements Loader {
    protected abstract char getSeparator();

    protected abstract boolean process (int row, String[] cells);

    protected abstract Logger getLogger ();

    @Override
    public int load (InputStream in) throws IOException{

        int row = 0;
        int count = 0;
        try(
            final InputStreamReader reader = new InputStreamReader(in);
            final CSVReader csv = new CSVReader(reader, getSeparator())
        ){
            String[] cells;
            while ((cells = csv.readNext()) != null){
                row++;
                if (cells[0].startsWith("#")){
                    continue;
                }

                try {
                    if (process(row, cells)) {
                        count++;
                    }
                } catch (Exception e) {
                    logError(row, "Unexpected error", e);
                }
            }
        }
        return count;
    }

    protected Double toDouble(final String value){
        try {
            return value == null || value.length() == 0
                    ? null
                    : Double.parseDouble(value);
        } catch (Exception e){
            return null;
        }
    }

    protected Long toLong(final String value){
        Double xvalue = toDouble(value);
        return xvalue != null ? xvalue.longValue() : null;
    }

    protected Integer toInt(final String value){
        Double xvalue = toDouble(value);
        return xvalue != null ? xvalue.intValue() : null;
    }

    protected Date toDate(final String value, final String pattern){
        if (Strings.isNullOrEmpty(value)){
            return null;
        }

        final DateFormat df = new SimpleDateFormat(pattern, Locale.US);
        try {
            return df.parse(value);
        } catch (Exception e){
            return null;
        }
    }

    protected void logError(int row, String message, Throwable ex){
        final String msg = "row=" + row + ", Message=" + message;
        if (ex == null) {
            getLogger().error(msg);
        } else {
            getLogger().error(msg, ex);
        }
    }
}
