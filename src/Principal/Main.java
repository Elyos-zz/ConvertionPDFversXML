package Principal;

import Validation.Validateur;
import com.snowtide.PDF;
import javax.xml.transform.*;
import java.io.*;
import java.util.Random;

import org.w3c.dom.Document;

public class Main {

    private static final int MAX = 10000;
    private static final int MIN = 1;
    private static Random nombre = new Random();
    private static boolean estConnecte = false;

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

            try {//convertion PDF/A-1b
                fileInputStream = new FileInputStream(input);
                fileOutputStream = new FileOutputStream(output);

                Runtime runtime = Runtime.getRuntime();
                System.out.println("============CONVERTION AU FORMAT PDF/A==============");
                System.out.println("Traitement de la conversion PDF vers PDFA...");

                runtime.exec("gs -dPDFA -dBATCH -dNOPAUSE -dUseCIEColor -sProcessColorModel=DeviceCMYK -sDEVICE=pdfwrite -sPDFACompatibilityPolicy=1 -sOutputFile=" + output + " " + input + "");

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null && fileOutputStream != null) {
                    fileInputStream.close();
                    fileOutputStream.close();
                }
            }

            //Convertion XML, transformation et numerotation du document
            System.out.println("============CONVERTION AU FORMAT XML==============");
            System.out.println("Conversion XML en cours...");
            System.out.println("Transformation XML en cours...");
            System.out.println("Numérotation du XML en cours...");
            System.out.println("...Connexion à la base en cours...");
            Main.connexionBase();
            Main.doAll(args, output);
            if (connexionBase()){
                System.out.println("\tConnexion à la base réussie !");
            }
            System.out.println("Conversion PDF/A et XML terminée avec succès !");

        } else {
            // test de validité PDF/A-1b
            System.out.println("Felicitation votre fichier est déjà valide format PDF/A-1b !");

            //Convertion XML, transformation et numérotation du document
            System.out.println("============CONVERTION AU FORMAT XML==============");
            System.out.println("Conversion XML en cours...");
            System.out.println("Transformation XML en cours...");
            System.out.println("Numérotation du XML en cours...");
            Main.doAll(args, output);
            Main.connexionBase();
            System.out.println(connexionBase());
            if (connexionBase()){
                System.out.println("\tConnexion à la base réussie !");
            }else{
                System.out.println("Connexion à la base échouée !");
            }

            System.out.println("Conversion PDF/A et XML terminée avec succès !");
        }
    }

    private static void doAll(String[] argument, File output) throws Exception {
        File file = new File(ConvertirVersXML.convertirXML(argument, output).getAbsolutePath());
        Document document = ConvertirVersXML.readXmlDocument(file);
        Transformer transformer = ConvertirVersXML.createXmlTransformer(file);
        ConvertirVersXML.overwriteXmlFile(file, document, transformer);
        //numerotation du document XML
        int id = getNombre().nextInt((MAX - MIN) + 1) - MIN;
        Process proc = Runtime.getRuntime().exec("/ccc/home/cont001/ocre/labassie/XEDIX/xedixts/bin/convert/numerote -xml -suf " + "\\.xml -id " + id + " -class none " + file.getAbsolutePath());
        //On suppose que la numerotation echoue si un identifiant a déjà été attribué
        if(!proc.isAlive()){
            //Si le document possède déja un identifiant similaire à celui généré, alors on lui en attribut un par defaut
            id = 10001;
            Runtime.getRuntime().exec("/ccc/home/cont001/ocre/labassie/XEDIX/xedixts/bin/convert/numerote -xml -suf " + "\\.xml -id " + id + " -class none " + file);
        }
        //Importation du document
        if (Main.connexionBase()) {
            Runtime.getRuntime().exec("/ccc/home/cont001/ocre/labassie/XEDIX/xedixts/bin/rempli_base " + file.getAbsolutePath() + " ");
        }
    }

    private static boolean connexionBase() throws IOException {
        //Connexion à la base
        Process connexion_web;
        Process connexion_xedix;
        do {
            connexion_web = Runtime.getRuntime().exec("/ccc/home/cont001/ocre/labassie/XEDIX/xedixts/bin/lance/lance_web XEDIX start");
            connexion_xedix = Runtime.getRuntime().exec("/ccc/home/cont001/ocre/labassie/XEDIX/xedixts/bin/lance/lance_xedix XEDIX start");
            estConnecte = true;
        }while(!isEstConnecte());

        return estConnecte;
    }

    private static boolean deconnexionBase() throws IOException {
        //Deconnexion à la base
        Process connexion_web;
        Process connexion_xedix;
        do {
            connexion_web = Runtime.getRuntime().exec("/ccc/home/cont001/ocre/labassie/XEDIX/xedixts/bin/lance/lance_web XEDIX stop");
            connexion_xedix = Runtime.getRuntime().exec("/ccc/home/cont001/ocre/labassie/XEDIX/xedixts/bin/lance/lance_xedix XEDIX stop");
            estConnecte = false;
        }while(isEstConnecte());

        return estConnecte;
    }

    //Une fonction générant une clef de session
    private static int genereCleSession(){
        int clefDeSession = 0;

        return clefDeSession;
    }
    public static boolean isEstConnecte() {
        return estConnecte;
    }
    public static Random getNombre() {
        return nombre;
    }
}