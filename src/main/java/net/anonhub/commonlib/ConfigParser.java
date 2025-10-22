package net.anonhub.commonlib;

import net.anonhub.commonlib.configParser.*;
import net.anonhub.commonlib.configParser.noValueException.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigParser {
    public static final String DATA_TYPE_CHAR = "#@#&";
    public static final String END_CHAR = "Ḛ";
    private HashMap<String, HashMap<String, ArrayList<String>>> settings = new HashMap<>();
    private final Path path;
    private final Path file;

    public ConfigParser(String filePath) {
        file = Paths.get(filePath);
        path = file.getParent();
        read();

    }
    public ConfigParser(Path filePath) {
        file = filePath;
        path = file.getParent();
        read();
    }

    public ConfigParser(String filePath, HashMap<String, HashMap<String, ArrayList<String>>> defaultSettings) {
        file = Paths.get(filePath);
        path = file.getParent();
        try {
            read();
        } catch (BadPathException ignore) {}
        update(defaultSettings);
        writeFile();
    }
    public ConfigParser(Path filePath, HashMap<String, HashMap<String, ArrayList<String>>> defaultSettings) {
        file = filePath;
        path = file.getParent();
        try {
            read();
        } catch (BadPathException ignore) {}
        update(defaultSettings);
        writeFile();
    }


    public void read() {
//        get the raw file
        List<String> rawFile;
        try {
            rawFile = Files.readAllLines(file);
        } catch (IOException e) {
            throw new BadPathException("the file \"" + file + "\" could not be found, check names and locations");

//        removes comments
        ArrayList<String> commentlessFile = new ArrayList<>();
        for (String line:rawFile) {
            if (line.contains("#")) {
                line = line.split("#")[0];
            }
            if (!line.isEmpty()) {
                commentlessFile.add(line);
            }
        }

//        joins file into a string and removes spaces
        StringBuilder fullFile = new StringBuilder();
        for (String line:commentlessFile) {
            fullFile.append(line);
        }

        for (int i = 0; i < fullFile.length(); i++) {
            if (fullFile.charAt(i) == ' ') {
                fullFile.deleteCharAt(i);
                i--;
            }
        }

//        turn into array of settings
        if (!fullFile.toString().contains(";")) {
            if (fullFile.isEmpty()){
                return;
            }
            throw new MissingSemicolonException("File contains no semicolons to separate settings");
        }
        ArrayList<String> settings = new ArrayList<>(Arrays.asList(fullFile.toString().split(";")));


//        initialize final map
        HashMap<String, HashMap<String, ArrayList<String>>> fin = new HashMap<>();

//        finish
        for (String setting:settings) {
            if (!setting.contains("-")) {
                throw new MissingDashException("Missing a dash after semicolon "+settings.indexOf(setting));
            }
            String name = setting.split("-")[0];
            String[] rawSets = setting.split("-")[1].split(":");

//            add setting to name
            fin.put(name, new HashMap<>());

            for (String rawSet:rawSets) {
                String setName = rawSet.split("=")[0];
//                String rawValues = rawSet.split("=")[1];
                StringBuilder rawValues = new StringBuilder(rawSet.split("=")[1]);
                ArrayList<String> values = new ArrayList<>();
                ArrayList<String> valueTypes = new ArrayList<>();


//                set values and value data types
                while (!rawValues.isEmpty()) {
                    if (!values.isEmpty()) {
                        if (rawValues.charAt(0) == ',') {
                            rawValues.deleteCharAt(0);
                        } else if (rawValues.charAt(0) == END_CHAR.charAt(0)) {
                            break;
                        } else {
                            throw new MissingCommaException("Expected a comma between values "+values+rawValues+" in setting \""+name+"\"");
                        }
                    }


                    switch (rawValues.charAt(0)) {
                        case ('\"') -> {
                            valueTypes.add("String");
                            for (int j = 1; j < rawValues.length(); j++) {
                                if (rawValues.charAt(j) == '\"') {
                                    values.add(rawValues.substring(1, j));
                                    rawValues.delete(0, j+1);
//                                  TODO add breakout characters
                                    break;
                                }
                            }
                        }
                        case ('\'') -> {
                            valueTypes.add("char");
                            if (rawValues.charAt(2)=='\'') {
                                rawValues.deleteCharAt(0);
                                values.add(String.valueOf(rawValues.charAt(0)));
                                rawValues.deleteCharAt(0);
                                rawValues.deleteCharAt(0);
                            }
                        }
                        case ('b') -> {
                            valueTypes.add("byte");
                            rawValues.append(END_CHAR);
                            for (int j = 1; j < rawValues.length(); j++) {
                                String testNum;
                                testNum = String.valueOf(rawValues.charAt(j));
                                try {
                                    Byte.valueOf(testNum);
                                } catch (NumberFormatException numberFormatException) {
                                    long num = Long.parseLong(rawValues.substring(1, j));
                                    values.add(String.valueOf((byte) num));
                                    rawValues.delete(0, j);
                                    break;
                                }
                            }
                        }
                        case ('s') -> {
                            valueTypes.add("short");
                            rawValues.append(END_CHAR);
                            for (int j = 1; j < rawValues.length(); j++) {
                                String testNum = String.valueOf(rawValues.charAt(j));
                                try {
                                    Byte.valueOf(testNum);
                                } catch (NumberFormatException numberFormatException) {
                                    long num = Long.parseLong(rawValues.substring(1, j));
                                    values.add(String.valueOf((short) num));
                                    rawValues.delete(0, j);
                                    break;
                                }
                            }
                        }
                        case ('i') -> {
                            valueTypes.add("int");
                            rawValues.append(END_CHAR);
                            for (int j = 1; j < rawValues.length(); j++) {
                                String testNum = String.valueOf(rawValues.charAt(j));
                                try {
                                    Byte.valueOf(testNum);
                                } catch (NumberFormatException numberFormatException) {
                                    long num = Long.parseLong(rawValues.substring(1, j));
                                    values.add(String.valueOf((int) num));
                                    rawValues.delete(0, j);
                                    break;
                                }
                            }
                        }
                        case ('l') -> {
                            valueTypes.add("long");
                            rawValues.append(END_CHAR);
                            for (int j = 1; j < rawValues.length(); j++) {
                                String testNum = String.valueOf(rawValues.charAt(j));
                                try {
                                    Byte.valueOf(testNum);
                                } catch (NumberFormatException numberFormatException) {
                                    long num = Long.parseLong(rawValues.substring(1, j));
                                    values.add(String.valueOf(num));
                                    rawValues.delete(0, j);
                                    break;
                                }
                            }
                        }
                        case ('f') -> {
                            valueTypes.add("float");
                            rawValues.append(END_CHAR);
                            boolean dot = false;
                            for (int j = 1; j < rawValues.length(); j++) {
                                String testNum = String.valueOf(rawValues.charAt(j));
                                try {
                                    if (testNum.equals(".") && !dot) {
                                        dot = true;
                                        continue;
                                    }
                                    Byte.valueOf(testNum);
                                } catch (NumberFormatException numberFormatException) {
                                    double num = Double.parseDouble(rawValues.substring(1, j));
                                    values.add(String.valueOf((float) num));
                                    rawValues.delete(0, j);
                                    break;
                                }
                            }
                        }
                        case ('d') -> {
                            valueTypes.add("double");
                            rawValues.append(END_CHAR);
                            boolean dot = false;
                            for (int j = 1; j < rawValues.length(); j++) {
                                String testNum = String.valueOf(rawValues.charAt(j));
                                try {
                                    if (testNum.equals(".") && !dot) {
                                        dot = true;
                                        continue;
                                    }
                                    Byte.valueOf(testNum);
                                } catch (NumberFormatException numberFormatException) {
                                    double num = Double.parseDouble(rawValues.substring(1, j));
                                    values.add(String.valueOf(num));
                                    rawValues.delete(0, j);
                                    break;
                                }
                            }
                        }
                        case 'n' -> {
                            if (!(rawValues.charAt(1) == 'u' && rawValues.charAt(2) == 'l' && rawValues.charAt(3) == 'l')) {
                                throw new IllegalValueException("expected null but got "+rawValues.toString().split(",")[0]);
                            }
                            valueTypes.add("null");
                            values.add("null");
                            rawValues.delete(0,4);
                        }
                    }
                }

//                add set to setting
                fin.get(name).put(setName, values);
                fin.get(name).put(setName+DATA_TYPE_CHAR, valueTypes);
            }
        }
        this.settings = fin;
    }

    public void writeFile() {
        if (Files.notExists(file)) {
            try {
                Files.createDirectories(path);
                Files.createFile(file);
            } catch (IOException e) {
                throw new RuntimeException(e.toString());
            }
        } else if (Files.isDirectory(file)) {
            throw new BadPathException("There is a directory where their should be a file. Fix it before trying again.");
        }
        StringBuilder sb = new StringBuilder();
        for (String key:settings.keySet()) {
            sb.append(key);
            sb.append("-\n");
            for (String key1:settings.get(key).keySet()) {
                if (key1.contains(DATA_TYPE_CHAR)) {
                    continue;
                }
                sb.append("  ");
                sb.append(key1);
                sb.append("=");
                for (int i = 0; i < settings.get(key).get(key1).size(); i++) {
                    switch (settings.get(key).get(key1+DATA_TYPE_CHAR).get(i)) {
                        case "String" -> sb.append("\"");
                        case "char" -> sb.append('\'');
                        case "byte" -> sb.append('b');
                        case "short" -> sb.append('s');
                        case "int" -> sb.append('i');
                        case "long" -> sb.append('l');
                        case "float" -> sb.append('f');
                        case "double" -> sb.append('d');
                    }
                    sb.append(settings.get(key).get(key1).get(i));
                    switch (settings.get(key).get(key1+DATA_TYPE_CHAR).get(i)) {
                        case "String" -> sb.append("\"");
                        case "char" -> sb.append('\'');
                    }
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length()-1);
                sb.append(":\n");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.deleteCharAt(sb.length()-1);
            sb.append(";\n");
        }
        try {
            Files.writeString(file, sb);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public void update(HashMap<String, HashMap<String, ArrayList<String>>> settings) {
        for (String settingKey:settings.keySet()) {
            HashMap<String, ArrayList<String>> defaultSetting = settings.get(settingKey);
            if (!this.settings.containsKey(settingKey)) {
                this.settings.put(settingKey, defaultSetting);
                continue;
            }
            HashMap<String, ArrayList<String>> currentSetting = this.settings.get(settingKey);

            for (String setKey:defaultSetting.keySet()) {
                ArrayList<String> defaultValues = defaultSetting.get(setKey);
                if (!currentSetting.containsKey(setKey)) {
                    currentSetting.put(setKey, defaultSetting.get(setKey));
                }
            }
        }
    }

//    get values
    public ArrayList<String> getStringMulti(String stringPath) {
        ArrayList<Integer> usable = new ArrayList<>();
        String[] path = stringPath.split("/");

        for (int i = 0; i < settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).size(); i++) {
            if (settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).get(i).matches("\"")) {
                usable.add(i);
            }
        }
        ArrayList<String> fin = new ArrayList<>();
        if (!usable.isEmpty()) {
            for (int use:usable) {
                fin.add(settings.get(path[0]).get(path[1]).get(use));
            }
            return fin;
        } else {
            throw new NoStringValueException("There was no string value at \"" + stringPath +"\"");
        }
    }
    public ArrayList<Character> getCharMulti(String stringPath) {
        ArrayList<Integer> usable = new ArrayList<>();
        String[] path = stringPath.split("/");

        for (int i = 0; i < settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).size(); i++) {
            if (settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).get(i).matches("'")) {
                usable.add(i);
            }
        }
        ArrayList<Character> fin = new ArrayList<>();
        if (!usable.isEmpty()) {
            for (int use:usable) {
                fin.add(settings.get(path[0]).get(path[1]).get(use).charAt(0));
            }
            return fin;
        } else {
            throw new NoCharValueException("There was no char value at \"" + stringPath +"\"");
        }
    }
    public ArrayList<Byte> getByteMulti(String stringPath) {
        ArrayList<Integer> usable = new ArrayList<>();
        String[] path = stringPath.split("/");
        if (path.length != 2) {
            throw new BadPathException("The path \""+stringPath+"\" is not a valid path");
        }

        for (int i = 0; i < settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).size(); i++) {
            if (settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).get(i).matches("b")) {
                usable.add(i);
            }
        }
        ArrayList<Byte> fin = new ArrayList<>();
        if (!usable.isEmpty()) {
            for (int use:usable) {
                fin.add(Byte.valueOf(settings.get(path[0]).get(path[1]).get(use)));
            }
            return fin;
        } else {
            throw new NoByteValueException("There was no byte value at \"" + stringPath +"\"");
        }
    }
    public ArrayList<Short> getShortMulti(String settingPath) {
        ArrayList<Integer> usable = new ArrayList<>();
        String[] path = settingPath.split("/");

        for (int i = 0; i < settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).size(); i++) {
            if (settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).get(i).matches("s")) {
                usable.add(i);
            }
        }
        ArrayList<Short> fin = new ArrayList<>();
        if (!usable.isEmpty()) {
            for (int use:usable) {
                fin.add(Short.valueOf(settings.get(path[0]).get(path[1]).get(use)));
            }
            return fin;
        } else {
            throw new NoShortValueException("There was no short value at \"" + settingPath +"\"");
        }
    }
    public ArrayList<Integer> getIntMulti(String stringPath) {
        ArrayList<Integer> usable = new ArrayList<>();
        String[] path = stringPath.split("/");

        for (int i = 0; i < settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).size(); i++) {
            if (settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).get(i).matches("i")) {
                usable.add(i);
            }
        }
        ArrayList<Integer> fin = new ArrayList<>();
        if (!usable.isEmpty()) {
            for (int use:usable) {
                fin.add(Integer.valueOf(settings.get(path[0]).get(path[1]).get(use)));
            }
            return fin;
        } else {
            throw new NoIntValueException("There was no int value at \"" + stringPath +"\"");
        }
    }
    public ArrayList<Long> getLongMulti(String stringPath) {
        ArrayList<Integer> usable = new ArrayList<>();
        String[] path = stringPath.split("/");

        for (int i = 0; i < settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).size(); i++) {
            if (settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).get(i).matches("l")) {
                usable.add(i);
            }
        }
        ArrayList<Long> fin = new ArrayList<>();
        if (!usable.isEmpty()) {
            for (int use:usable) {
                fin.add(Long.valueOf(settings.get(path[0]).get(path[1]).get(use)));
            }
            return fin;
        } else {
            throw new NoLongValueException("There was no long value at \"" + stringPath +"\"");
        }
    }
    public ArrayList<Float> getFloatMulti(String stringPath) {
        ArrayList<Integer> usable = new ArrayList<>();
        String[] path = stringPath.split("/");

        for (int i = 0; i < settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).size(); i++) {
            if (settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).get(i).matches("f")) {
                usable.add(i);
            }
        }
        ArrayList<Float> fin = new ArrayList<>();
        if (!usable.isEmpty()) {
            for (int use:usable) {
                fin.add(Float.valueOf(settings.get(path[0]).get(path[1]).get(use)));
            }
            return fin;
        } else {
            throw new NoFloatValueException("There was no float value at \"" + stringPath +"\"");
        }
    }
    public ArrayList<Double> getDoubleMulti(String stringPath) {
        ArrayList<Integer> usable = new ArrayList<>();
        String[] path = stringPath.split("/");

        for (int i = 0; i < settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).size(); i++) {
            if (settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).get(i).matches("d")) {
                usable.add(i);
            }
        }
        ArrayList<Double> fin = new ArrayList<>();
        if (!usable.isEmpty()) {
            for (int use:usable) {
                fin.add(Double.valueOf(settings.get(path[0]).get(path[1]).get(use)));
            }
            return fin;
        } else {
            throw new NoDoubleValueException("There was no double value at \"" + stringPath +"\"");
        }
    }
    public ArrayList<Boolean> getBooleanMulti(String stringPath) {
        ArrayList<Integer> usable = new ArrayList<>();
        String[] path = stringPath.split("/");

        for (int i = 0; i < settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).size(); i++) {
            if (settings.get(path[0]).get(path[1]+DATA_TYPE_CHAR).get(i).matches("B")) {
                usable.add(i);
            }
        }
        ArrayList<Boolean> fin = new ArrayList<>();
        if (!usable.isEmpty()) {
            for (int use:usable) {
                fin.add(Boolean.valueOf(settings.get(path[0]).get(path[1]).get(use)));
            }
            return fin;
        } else {
            throw new NoBooleanValueException("There was no boolean value at \"" + stringPath +"\"");
        }
    }
    public String getStringSingle(String path) {
        ArrayList<String> values = getStringMulti(path);
        if (values.size() > 1) {
            throw new MultiValueException("tried to get a single value from a multi value setting at path: \""+ path+"\"");
        }
        return values.getFirst();
    }
    public char getCharSingle(String path) {
        ArrayList<Character> values = getCharMulti(path);
        if (values.size() > 1) {
            throw new MultiValueException("tried to get a single value from a multi value setting at path: \""+ path+"\"");
        }
        return values.getFirst();
    }
    public byte getByteSingle(String path) {
        ArrayList<Byte> values = getByteMulti(path);
        if (values.size() > 1) {
            throw new MultiValueException("tried to get a single value from a multi value setting at path: \""+ path+"\"");
        }
        return values.getFirst();
    }
    public short getShortSingle(String path) {
        ArrayList<Short> values = getShortMulti(path);
        if (values.size() > 1) {
            throw new MultiValueException("tried to get a single value from a multi value setting at path: \""+ path+"\"");
        }
        return values.getFirst();
    }
    public int getIntSingle(String path) {
        ArrayList<Integer> values = getIntMulti(path);
        if (values.size() > 1) {
            throw new MultiValueException("tried to get a single value from a multi value setting at path: \""+ path+"\"");
        }
        return values.getFirst();
    }
    public long getLongSingle(String path) {
        ArrayList<Long> values = getLongMulti(path);
        if (values.size() > 1) {
            throw new MultiValueException("tried to get a single value from a multi value setting at path: \""+ path+"\"");
        }
        return values.getFirst();
    }
    public float getFloatSingle(String path) {
        ArrayList<Float> values = getFloatMulti(path);
        if (values.size() > 1) {
            throw new MultiValueException("tried to get a single value from a multi value setting at path: \""+ path+"\"");
        }
        return values.getFirst();
    }
    public double getDoubleSingle(String path) {
        ArrayList<Double> values = getDoubleMulti(path);
        if (values.size() > 1) {
            throw new MultiValueException("tried to get a single value from a multi value setting at path: \""+ path+"\"");
        }
        return values.getFirst();
    }
    public boolean getBooleanSingle(String path) {
        ArrayList<Boolean> values = getBooleanMulti(path);
        if (values.size() > 1) {
            throw new MultiValueException("tried to get a single value from a multi value setting at path: \""+ path+"\"");
        }
        return values.getFirst();
    }
    public HashMap<String, HashMap<String, ArrayList<String>>> getSettings(){
        return settings;
    }





    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String key:settings.keySet()) {
            sb.append(key);
            sb.append(":\n");
            for (String key1:settings.get(key).keySet()) {
                sb.append("  ");
                sb.append(key1);
                sb.append(":\n");
                for (String value:settings.get(key).get(key1)) {
                    sb.append("    ");
                    sb.append(value);
                    sb.append('\n');
                }
            }
        }
        return sb.toString();
    }
}
