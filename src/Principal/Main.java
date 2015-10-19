package Principal;

import EfficientConvertisseurXml.XMLOutputTarget;
import Validation.Validateur;
import com.snowtide.PDF;
import java.io.*;

import static Validation.Validateur.retourneLePDFA;

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

        //Test si le fichier d'entrée est valide PDFA
        if (!test.validation()) {

            File empty = retourneLePDFA(args[0]);

            String chemin = empty.getAbsolutePath();

            //Création du fichier final au format XML
            File sortieXML = new File(args[0] + ".xml");

            //Traitement du fichier possédant le format PDFA
            com.snowtide.pdf.Document stream = PDF.open(empty);
            XMLOutputTarget target = new XMLOutputTarget();
            stream.pipe(target);
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(sortieXML), "UTF-8");
            writer.write(target.getXMLAsString());
            writer.flush();
            writer.close();
            stream.close();
        }

        else{
            System.out.println("Félicitation");
            //IL manque ici le traitement dupliqué pour le convertir en XML
        }

        if (!input.canRead()) {
            System.out.println("\nConversion effectuée : fichier corrompu.");
        } else {
            System.out.println("\nOups ! Le fichier n'a pas été généré.");
        }
    }
}