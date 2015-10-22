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
        OutputStreamWriter writer = null;
        Document stream = null;

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

                stream = PDF.open(src);
                XMLOutputTarget target = new XMLOutputTarget();
                stream.pipe(target);
                writer = new OutputStreamWriter(sortieFileOutputStream, "UTF-8");
                writer.write(target.getXMLAsString());
                writer.flush();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }finally{
            try {
                if (sortieFileOutputStream != null && writer != null && stream != null) {
                    sortieFileOutputStream.close();
                    writer.close();
                    stream.close();
                }else{
                    System.out.println("Flux null");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sortieXML;
    }
}