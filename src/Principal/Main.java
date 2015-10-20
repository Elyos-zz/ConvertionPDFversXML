package Principal;

import EfficientConvertisseurXml.XMLOutputTarget;
import Validation.Validateur;
import com.snowtide.PDF;
import com.snowtide.pdf.Document;
import com.snowtide.pdf.PDFTextStream;
import com.sun.xml.internal.ws.commons.xmlutil.Converter;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.*;

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
        File output = new File("/ccc/home/cont001/ocre/labassie/Git/ConvertionPDFversXML/src/Principal/fichierPDFA.pdf");

        //Test si le fichier d'entrée est valide PDFA
        if (!test.validation()) {

            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;

            if (input.exists()) {

                try {
                    fileInputStream = new FileInputStream(input);
                    fileOutputStream = new FileOutputStream(output);

                    System.out.println("\n*******  TEST DE LA VALIDITÉ DU DOCUMENT **********");
                    Runtime runtime = Runtime.getRuntime();
                    System.out.println("\n*******  CONVERSION PDF VERS PDFA  **********\n\nTraitement de la conversion PDF vers PDFA...");

                    runtime.exec("gs -dPDFA -dBATCH -dNOPAUSE -dUseCIEColor -sProcessColorModel=DeviceCMYK -sDEVICE=pdfwrite -sPDFACompatibilityPolicy=1 -sOutputFile=" + output + " " + input + "");


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    fileInputStream.close();
                    fileOutputStream.close();
                }
            } else {
                System.out.println("Le fichier d'entrée n'existe pas.");
            }

            File sortieXML = new File(output.getAbsolutePath() + ".xml");
            FileInputStream outFileInputStream = null;
            FileOutputStream sortieFileOutputStream = null;

            try {
                outFileInputStream = new FileInputStream(output);
                sortieFileOutputStream = new FileOutputStream(sortieXML);
                System.out.println("\nTraitement XML...\n");

                //while ((n = outFileInputStream.read(contenu)) >= 0) {

                    System.out.println("/nTraitement XML.../n");
                    Document stream = PDF.open(outFileInputStream, null);
                    XMLOutputTarget target = new XMLOutputTarget();
                    stream.pipe(target);
                    OutputStreamWriter writer = new OutputStreamWriter(sortieFileOutputStream, "UTF-8");
                    writer.write(target.getXMLAsString());
                    writer.flush();
                    writer.close();
                    stream.close();

                //}

            } finally {
                outFileInputStream.close();
                sortieFileOutputStream.close();
            }
        }
    }
}