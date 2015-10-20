package Validation;

import EfficientConvertisseurXml.XMLOutputTarget;
import com.snowtide.PDF;
import com.snowtide.pdf.Document;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.exception.SyntaxValidationException;
import org.apache.pdfbox.preflight.parser.PreflightParser;

import javax.activation.FileDataSource;
import java.io.*;

/**
 * Created by elabassi on 22/09/15.
 */
public class Validateur {

    //Dans cette classe nous utiliserons la bibliothéque Preflight qui implémente un parser compilant la norme PDF/A-1b

    /*
    * Parser le PDF avec PreflightParser héritant de NonSequentialParser
    * Certains contrôles supplémentaire sont présents pour vérifier un ensemble d'exigences PDF/A
    * Une fois que la syntaxe est validée, le parser peut autoriser la création d'un PreflightDocument hériant d'un
    * PDDocument.
    * Le PreflightDocument marque la fin du processus de validation PDF/A.
    */

    private static String[] args;
    private ValidationResult res;

    public Validateur(String[] uri){

        this.args = uri;
    }

    public boolean validation() throws SyntaxValidationException{

        try{
            try {
                res = null;
                FileDataSource fds = new FileDataSource(args[0]);

                PreflightParser parser = new PreflightParser(fds);
                parser.parse();

                PreflightDocument document = parser.getPreflightDocument();
                document.validate();
                res = document.getResult();
                document.close();
            }
                catch(SyntaxValidationException e){
                    res = e.getResult();
                }

            } catch (IOException e) {

                e.printStackTrace();
            }

        if (res.isValid()) {

            System.out.println("\nLe document est valide au format PDF/A-1b.");
            return true;

        } else {
            System.out.println("\nLe document n'est pas valide... \n et ne correspond pas au format PDF/A-1b.\n\nVeuillez prendre connaissance des erreurs suivantes:\n");

            for (ValidationResult.ValidationError err : res.getErrorsList()) {

                System.out.println(err.getErrorCode() + " : " + err.getDetails());
            }
            System.out.println("");
            return false;
        }
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public ValidationResult getRes() {
        return res;
    }

    public void setRes(ValidationResult res) {
        this.res = res;
    }
}