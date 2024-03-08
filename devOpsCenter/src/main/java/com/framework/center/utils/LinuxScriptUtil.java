package com.framework.center.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * linux 脚本执行器
 *
 * @author yankunw
 * @date 2023-04-14
 */
public class LinuxScriptUtil {
    public static List<String> executeNewFlow(List<String> commands) {
        List<String> rspList = new ArrayList<>();
        Runtime run = Runtime.getRuntime();
        BufferedReader in = null;
        PrintWriter out = null;
        Process proc = null;
        try {
            proc = run.exec("/bin/bash", null, null);
            in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            for (String line : commands) {
                out.println(line);
            }
            out.println("exit");
            String rspLine;
            while ((rspLine = in.readLine()) != null) {
                rspList.add(rspLine);
            }

        } catch (Exception ignored) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
            if (out != null) {
                out.close();
            }
            if (proc != null) {
                proc.destroy();
            }
        }
        return rspList;
    }
}
