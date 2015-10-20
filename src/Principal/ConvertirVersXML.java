package Principal;

import EfficientConvertisseurXml.XMLOutputTarget;
import com.snowtide.PDF;
import com.snowtide.pdf.Document;

import java.io.*;

/**
 * Created by labassie on 20/10/15.
 */
public class ConvertirVersXML {

    public static File convertirXML(String[] args, File pdfa) {

        FileOutputStream sortieFileOutputStream = null;

        File sortieXML = new File(pdfa.getAbsolutePath() + ".xml");

        try {

            for (int i = 0; i < args.length; i++) {
                File src = new File(args[i]);
                if (!src.exists()) {
                    System.out.println("Fichier introuvable: " + args[i]);
                }
                if (!src.canRead()) {
                    System.out.println("Impossible de lire le fichier: " + args[i]);
                }
                sortieFileOutputStream = new FileOutputStream(sortieXML);

                Document stream = PDF.open(src);
                XMLOutputTarget target = new XMLOutputTarget();
                stream.pipe(target);
                OutputStreamWriter writer = new OutputStreamWriter(sortieFileOutputStream, "UTF-8");
                writer.write(target.getXMLAsString());
                writer.flush();
                sortieFileOutputStream.close();
                writer.close();
                stream.close();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return sortieXML;
    }
}