package hackassembler;

import java.util.ArrayList;
import java.util.HashMap;

public class Parse {

    private ArrayList<String> lines;


    public Parse(ArrayList<String> file) {

        lines = file;

    }

    public void removeComments() {
        for (int i = 0; i < lines.size();) {
            String line = lines.get(i);
            if (line.contains("//")) {
                line = line.replace(line.substring(line.indexOf("/"), line.length()),"");
                lines.set(i, line.trim());
            }
            if (line.trim().isEmpty()) {
                lines.remove(line);
            } else {
                line = line.trim();
                lines.set(i, line);
                i++;
            }
        }
    }

    public ArrayList<String> parseFile() {
        removeComments();
        HashMap<String, Integer> labelTable = new HashMap();
        HashMap<String, Integer> memTable = new HashMap();
        int mem = 16;

        for (int i = 0; i < lines.size();) {

            String s = lines.get(i);
            int loc;

            if (s.contains("(")) {
                s = s.substring(1,s.length()-1);
                loc = lines.indexOf("(" + s + ")");
                labelTable.put(s, loc);
                lines.remove(loc);
            } else {
                i++;
            }

        }

        for (String s : lines) {
            if (s.contains("@")) {
                s = s.substring(1,s.length());
                if (!labelTable.containsKey(s) && !memTable.containsKey(s)
                        && !Lex.labels.containsKey("@" + s) && Character.isLetter(s.charAt(0))) {
                    memTable.put(s, mem);
                    mem++;
                }
            }
        }

        for (int i = 0; i < lines.size(); i++) {

            String s = lines.get(i);

            if (s.contains("@")) {
                s = s.substring(1,s.length());
            }
            if (labelTable.containsKey(s)) {
                s = "@" + labelTable.get(s);
                lines.set(i, s);
            }
            if (memTable.containsKey(s)) {
                s = "@" + memTable.get(s);
                lines.set(i, s);
            }

        }

        return lines;
    }
}
