package blp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 *
 * @author marouen
 */
public class TEST2 {

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
        String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "src" + System.getProperty("file.separator") + "blp" + System.getProperty("file.separator") + "ins";
        String pathTosave = System.getProperty("user.dir") + System.getProperty("file.separator") + "src" + System.getProperty("file.separator") + "blp" + System.getProperty("file.separator") + "problems" + System.getProperty("file.separator") + "instances" + System.getProperty("file.separator") + "kp" + System.getProperty("file.separator") + "ins1";
        InputStream ips = new FileInputStream(path);
        InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(ipsr);
        String ligne, capacity = "", nbrItem = "", weights = "", profils = "";
        int ligneNumber = 1;
        while ((ligne = br.readLine()) != null) {
            if (ligneNumber == 1) {
                ligne = ligne.substring(ligne.indexOf("("));
                ligne = ligne.substring(ligne.indexOf("knapsacks, ") + 11, ligne.indexOf("items"));
                nbrItem = ligne.trim();
            } else if (ligneNumber == 4) {
                ligne = ligne.substring(ligne.indexOf(":") + 1);
                ligne = ligne.trim();
                DecimalFormat df = new DecimalFormat("+#;-#");
                capacity = "" + df.parse(ligne);
            } else {
                if (ligne.indexOf("weight:") != -1) {
                    ligne = ligne.substring(ligne.indexOf(":") + 1);
                    ligne = ligne.trim();
                    DecimalFormat df = new DecimalFormat("+#;-#");
                    weights += df.parse(ligne) + "\t";
                }
                if (ligne.indexOf("profit:") != -1) {
                    ligne = ligne.substring(ligne.indexOf(":") + 1);
                    ligne = ligne.trim();
                    DecimalFormat df = new DecimalFormat("+#;-#");
                    profils += df.parse(ligne) + "\t";
                }
            }
            ligneNumber++;
        }
        capacity = capacity.trim();
        nbrItem = nbrItem.trim();
        weights = weights.trim();
        profils = profils.trim();
        File file = new File(pathTosave);
        FileWriter writerFile = new FileWriter(file);
        writerFile.write(capacity);
        writerFile.write(System.getProperty("line.separator"));
        writerFile.write(nbrItem);
        writerFile.write(System.getProperty("line.separator"));
        writerFile.write(weights);
        writerFile.write(System.getProperty("line.separator"));
        writerFile.write(profils);
        writerFile.close();
        System.out.println(capacity);
        System.out.println(nbrItem);
        System.out.println(weights);
        System.out.println(profils);
    }
}
