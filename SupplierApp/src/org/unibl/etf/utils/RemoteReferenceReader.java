package org.unibl.etf.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RemoteReferenceReader {

    private static final String PATH = "resources";
    private static final String FILE_NAME = "remote_reference.txt";

    public static String readSupplierName() {
        File file = new File(PATH + File.separator + FILE_NAME);
        if (!file.exists()) {
            System.err.println("Fajl " + FILE_NAME + " ne postoji!");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
