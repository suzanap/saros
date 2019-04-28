package saros.stf.testwatcher;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class STFTestWatcherLevelTWOi extends TestWatcher {

    public static ArrayList<String> list = new ArrayList<String>();
    private static String Name;

    @Override
    public void succeeded(Description description) {

        list.add("Succeeded");

    }

    @Override
    public void failed(Throwable e, Description description) {
        list.add("Failed");

    }

    public static ArrayList<String> getList() {
        return list;
    }

    public static boolean checkIfAllSucceeded() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("Failed")) {
                return false;
            }
        }
        return true;
    }

    public static String getName() {
        return Name;
    }

    public static void setName(String name) {
        Name = name;
    }

    public static void writeFile() throws Exception {
        String fileName = String.format("c://temp//%s.txt", Name);
        File file = new File(fileName);
        PrintWriter writer = new PrintWriter(new FileWriter(file, true));
        writer.println(Name);
        for (int i = 0; i < list.size(); i++) {

            writer.println(list.get(i));
        }

        writer.close();

    }

}
