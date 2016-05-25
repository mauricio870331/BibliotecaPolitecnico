/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MysqlExporter {

    public static void export() {

        int BUFFER = 10485760;

        String path = "D:/ruta_backup.sql";
        String dumpCommand = "C:/xampp/mysql/bin/mysqldump –host=127.0.0.1 -u" + "root" + " -p" + "" + " " + " biblioteca_politecnico";
        FileWriter fw = null;
        String tst = path;
        try {
            fw = new FileWriter(tst);
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Runtime rt = Runtime.getRuntime();
        try {
            Process proc = rt.exec(dumpCommand);
            InputStream in = proc.getInputStream();
            InputStreamReader read = new InputStreamReader(in, "latin1");

            BufferedReader br = new BufferedReader(read);
            br = new BufferedReader(read);
            BufferedWriter bw = new BufferedWriter(new FileWriter(tst, true));
            StringBuffer buffer = new StringBuffer();
            int count;
            char[] cbuf = new char[BUFFER];
            while ((count = br.read(cbuf, 0, BUFFER)) != -1) {
                buffer.append(cbuf, 0, count);
            }
            System.out.println("terminado");
            String toWrite = buffer.toString();
            bw.write(toWrite);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
