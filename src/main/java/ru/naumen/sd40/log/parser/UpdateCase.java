package ru.naumen.sd40.log.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

public class UpdateCase {
    private TimeParser timeParser;
    private GCParser.GCTimeParser gcTime;
    private HashMap<Long, DataSet> data = new HashMap<>();
    boolean flag;
    public UpdateCase(TimeParser timeP)
    {
        flag = true;
        timeParser = timeP;
    }
    public UpdateCase(GCParser.GCTimeParser gcT)
    {
        flag = false;
        gcTime = gcT;
    }
    public HashMap<Long, DataSet> updata(BufferedReader br) throws IOException, ParseException {
        String line;
        long time = 5;
        while ((line = br.readLine()) != null) {
            if (flag) {
                time = timeParser.parseLine(line);
            } else {
                time = gcTime.parseTime(line);
            }

            if (time == 0) {
                continue;
            }

            int min5 = 5 * 60 * 1000;
            long count = time / min5;
            long key = count * min5;
            if (flag) {
                data.computeIfAbsent(key, k -> new DataSet()).parseLine(line);
            }
            else {
                data.computeIfAbsent(key, k -> new DataSet()).parseGcLine(line);
            }

        }
        return data;
    }
}
