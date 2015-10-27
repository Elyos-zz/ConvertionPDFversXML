package Principal;

import Validation.Validateur;
import com.snowtide.PDF;
import javax.xml.transform.*;
import java.io.*;
import org.w3c.dom.Document;

public class Main {

    /**
     * A main method suitable for using this class' functionality from the command line.
     * All of the command-line arguments will be taken to be paths to input PDF documents;
     * each PDF documents will be opened by {@link PDF}, and its content piped
     * through a <code>XMLOutputTarget</code> instance.  Each PDF's extracted content
     * is then written to a ".xml" file in the same directory as the input
     * document.
     *
     * @deprecated Command-line usage of this class may be moved or removed in future PDFxStream releases.
     */

    public static void main(String[] args) throws Exception, IOException {

        //Instanciation de la classe Validateur
        Validateur test = new Validateur(args);
        File input = new File(args[0]);
        File output = new File("./" + input.getName());

        //Test si le fichier d'entrée est valide PDFA
        if (!test.validation()) {

            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;

            try {
                fileInputStream = new FileInputStream(input);
                fileOutputStream = new FileOutputStream(output);

                System.out.println("\n*******  TEST DE LA VALIDITÉ DU DOCUMENT **********");
                Runtime runtime = Runtime.getRuntime();
                System.out.println("\n*******  CONVERSION PDF VERS PDFA  **********\n\nTraitement de la conversion PDF vers PDFA...");

                runtime.exec("gs -dPDFA -dBATCH -dNOPAUSE -dUseCIEColor -sProcessColorModel=DeviceCMYK -sDEVICE=pdfwrite -sPDFACompatibilityPolicy=1 -sOutputFile=" + output + " " + input + "");

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (fileInputStream != null && fileOutputStream != null) {
                    fileInputStream.close();
                    fileOutputStream.close();
                }
            }

            //convertion en XML du fichier "output"
            System.out.println("\n Conversion XML en cours...");
            File file = new File(ConvertirVersXML.convertirXML(args, output).getAbsolutePath());
            Document document = ConvertirVersXML.readXmlDocument(file);
            Transformer transformer = ConvertirVersXML.createXmlTransformer();
            ConvertirVersXML.overwriteXmlFile(file, document, transformer);
            System.out.println("\n\n\n\n\n\n\n\n\n\n\nConversion PDF/A et XML terminée avec succès !");
        } else {
            System.out.println("Felicitation votre fichier est valide format PDF/A-1b, voici le fichier converti en XML...");
            //convertion en XML du fichier "output"
            System.out.println("\n Conversion XML en cours...");
            File file = new File(ConvertirVersXML.convertirXML(args, output).getAbsolutePath());
            Document document = ConvertirVersXML.readXmlDocument(file);
            Transformer transformer = ConvertirVersXML.createXmlTransformer();
            ConvertirVersXML.overwriteXmlFile(file, document, transformer);
            System.out.println("\n\n\n\n\n\n\n\n\n\n\nConversion PDF/A et XML terminée avec succès !");
        }
    }


}


