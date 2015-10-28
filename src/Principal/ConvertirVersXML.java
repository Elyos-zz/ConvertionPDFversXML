package Principal;

import EfficientConvertisseurXml.XMLOutputTarget;
import com.snowtide.PDF;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import org.w3c.dom.Document;

/**
 * Created by labassie on 20/10/15.
 */
public class ConvertirVersXML {

    public static File convertirXML(String[] args, File pdfa){

        FileOutputStream sortieFileOutputStream = null;
        OutputStreamWriter writer = null;
        com.snowtide.pdf.Document stream = null;

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
    public static Document readXmlDocument(File xmlFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document document = builder.parse(xmlFile);
        return document;
    }

    public static Transformer createXmlTransformer(File file) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "racine.dtd");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        return transformer;
    }

    public static void overwriteXmlFile(File xmlFile, Document document, Transformer transformer) throws FileNotFoundException, TransformerException {

        StreamResult result = new StreamResult(new PrintWriter(new FileOutputStream(xmlFile, false)));
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);
    }
}