package lv.ctco.tmm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigReader {

    private static final String DEFAULT_PROP_NAME = "config.properties";
    private static final String CONFIG_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
    private static final String CONFIG_PATH_DEFAULT = System.getProperty("user.dir") + File.separator + "target" + File.separator + "classes" + File.separator;
    private static Properties properties = new Properties();

    private static void loadConfigPropertiesFile(String path) {
        InputStream inpstrm = null;
        try {
            File file = new File(CONFIG_PATH_DEFAULT + path);
            if (!file.isFile()){
                file = new File(CONFIG_PATH + path);
            }
            inpstrm = new FileInputStream(file);
            properties.load(new InputStreamReader(inpstrm));
        } catch (Exception e) {
            throw new IllegalStateException(e + "Path not found: " + CONFIG_PATH_DEFAULT + path);
        }
    }

    public static String getValueByKey(String key) {
        loadConfigPropertiesFile(DEFAULT_PROP_NAME);
        Object obj = properties.get(key);
        if (obj == null) {
        }
        return String.valueOf(obj);
    }

    public static void setValue(String key, String value) throws IOException {
        loadConfigPropertiesFile(DEFAULT_PROP_NAME);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(CONFIG_PATH + DEFAULT_PROP_NAME);
            properties.setProperty(key, value);
            properties.store(out, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String readFile(String fileName) {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get("src\\main\\resources\\" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, Charset.defaultCharset());
    }
}