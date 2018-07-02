package hackassembler;

import java.util.ArrayList;
import java.util.HashMap;

public class Lex {

    private static int mem = 16;

    private static HashMap<String, String> arithmetic = new HashMap() {{
        put("0"  , "0101010");
        put("1"  , "0111111");
        put("-1" , "0111010");
        put("D"  , "0001100");
        put("A"  , "0110000");
        put("!D" , "0001101");
        put("!A" , "0110001");
        put("-D" , "0001111");
        put("-A" , "0110011");
        put("D+1", "0011111");
        put("A+1", "0110111");
        put("D-1", "0001110");
        put("A-1", "0110010");
        put("D+A", "0000010");
        put("D-A", "0010011");
        put("A-D", "0000111");
        put("D&A", "0000000");
        put("D|A", "0010101");
        put("M"  , "1110000");
        put("!M" , "1110001");
        put("-M" , "1110011");
        put("M+1", "1110111");
        put("M-1", "1110010");
        put("D+M", "1000010");
        put("D-M", "1010011");
        put("M-D", "1000111");
        put("D&M", "1000000");
        put("D|M", "1010101");
    }};

    private static HashMap<String, String> jumps = new HashMap() {{
        put("JGT", "001");
        put("JEQ", "010");
        put("JGE", "011");
        put("JLT", "100");
        put("JNE", "101");
        put("JLE", "110");
        put("JMP", "111");
    }};

    private static HashMap<String, String> storage = new HashMap() {{
        put("M"  , "001");
        put("D"  , "010");
        put("A"  , "100");
        put("MD" , "011");
        put("DM" , "011");
        put("AM" , "101");
        put("MA" , "101");
        put("AD" , "110");
        put("DA" , "110");
        put("ADM", "111");
        put("AMD", "111");
        put("DAM", "111");
        put("DMA", "111");
        put("MAD", "111");
        put("MDA", "111");
    }};

    public static HashMap<String, String> labels = new HashMap() {{
        put("@SP"     , "0000000000000000");
        put("@LCL"    , "0000000000000001");
        put("@ARG"    , "0000000000000010");
        put("@THIS"   , "0000000000000011");
        put("@THAT"   , "0000000000000100");
        put("@SCREEN" , "0100000000000000");
        put("@KBD"    , "0110000000000000");
        put("@R0"     , "0000000000000000");
        put("@R1"     , "0000000000000001");
        put("@R2"     , "0000000000000010");
        put("@R3"     , "0000000000000011");
        put("@R4"     , "0000000000000100");
        put("@R5"     , "0000000000000101");
        put("@R6"     , "0000000000000110");
        put("@R7"     , "0000000000000111");
        put("@R8"     , "0000000000001000");
        put("@R9"     , "0000000000001001");
        put("@R10"    , "0000000000001010");
        put("@R11"    , "0000000000001011");
        put("@R12"    , "0000000000001100");
        put("@R13"    , "0000000000001101");
        put("@R14"    , "0000000000001110");
        put("@R15"    , "0000000000001111");
    }};

    public static ArrayList<String> translate(ArrayList<String> lines) {
        ArrayList<String> translation = new ArrayList();
        int num = 0;

        for (String s : lines) {
            int power = 15;
            String bin = "";
            if (s.contains("@")) {
                if (Character.isDigit(s.charAt(1))) {
                    s = s.substring(1,s.length());
                    num = Integer.parseInt(s);

                    while (power > -1) {
                        if (num - Math.pow(2,power) < 0) {
                            bin += "0";
                            power--;
                        } else if (num - Math.pow(2,power) >= 0) {
                            bin += "1";
                            num -= Math.pow(2,power);
                            power--;
                        }
                    }

                    translation.add(bin);

                } else if (Character.isLetter(s.charAt(1))) {
                    if (labels.containsKey(s)) {
                        bin = labels.get(s);
                    } else {
                        num = mem;
                        mem++;

                        while (power > -1) {
                            if (num - Math.pow(2,power) < 0) {
                                bin += "0";
                                power--;
                            } else if (num - Math.pow(2,power) >= 0) {
                                bin += "1";
                                num -= Math.pow(2,power);
                                power--;
                            }
                        }
                    }

                    translation.add(bin);
                }
            } else if (!s.contains("@")) {
                if (s.contains("=")) {
                    String[] set = s.split("=");
                    bin += "111";
                    bin += arithmetic.get(set[1]);
                    bin += storage.get(set[0]);
                    bin += "000";
                    translation.add(bin);
                } else if (s.contains(";")) {
                    String[] set = s.split(";");
                    bin += "111";
                    bin += arithmetic.get(set[0]);
                    bin += "000";
                    bin += jumps.get(set[1]);
                    translation.add(bin);
                }
            }
        }

        return translation;
    }

}
