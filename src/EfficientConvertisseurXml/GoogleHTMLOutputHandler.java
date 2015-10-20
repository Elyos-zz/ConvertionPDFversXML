package EfficientConvertisseurXml;

import com.snowtide.PDF;
import com.snowtide.pdf.OutputHandler;
import com.snowtide.pdf.Page;
import com.snowtide.pdf.layout.TextUnit;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>
 * This class is an example {@link OutputHandler} implementation that builds an XHTML document to
 * mimic the HTML view that Google offers for indexed PDF documents.
 * </p>
 * <p>Source for this class is included in every PDFxStream bundle.</p>
 * 
 * @publicapi
 * @version ©2004-2014 <a href="http://www.snowtide.com" target="_parent">Snowtide</a>
 */
public class GoogleHTMLOutputHandler extends OutputHandler {
    private Document doc;
    private Element pdfElt;
    private final HashMap attrs = new HashMap();
    
    private float top = 0;
    private float pageHeight;
    
    /**
     * Main method for command-line execution.  Usage:
     * <p>
     * <pre>java GoogleHTMLOutputHandler [input_pdf_file] [output_html_path]</pre>
     * </p>
     * @deprecated Command-line usage of this class may be moved or removed in future PDFxStream releases.
     */
    public static void main (String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java GoogleHTMLOutputHandler [input_pdf_file] [output_html_path]");
            System.exit(1);
        }
        
        FileInputStream fin = new FileInputStream(new File(args[0]));
        com.snowtide.pdf.Document pdf = PDF.open(args[0]);
        GoogleHTMLOutputHandler tgt = new GoogleHTMLOutputHandler();
        pdf.pipe(tgt);
        Writer out = new OutputStreamWriter(new FileOutputStream(new File(args[1])));
        XMLFormExport.serializeXMLDocument(tgt.getHTMLDocument(), out);
        out.flush();
        out.close();
    }
    
    public GoogleHTMLOutputHandler () throws ParserConfigurationException, FactoryConfigurationError {
        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }
    
    /**
     * Returns the XHTML document that is built up by this OutputHandler.
     */
    public Document getHTMLDocument () {
        return doc;
    }

    public void startPage (Page page) {
        pageHeight = page.getPageHeight();
        
        top += 50;
    }
    
    public void endPage (Page page) {
        top += page.getPageHeight();
    }

    public void startPDF (String pdfName, File pdfFile) {
        pdfElt = doc.createElement("html");
        doc.appendChild(pdfElt);        
    }

    public void textUnit (TextUnit tu) {        
        attrs.clear();
        attrs.put("style", "position:absolute;top:" + (top + pageHeight - tu.bounds().ty()) +
                ";left:" + tu.bounds().rx() + ";font-size:" + tu.bounds().height());

        String txt = tu.getCharacterSequence() == null ? 
                Character.toString((char)tu.getCharCode()) : new String(tu.getCharacterSequence());
                
        pdfElt.appendChild(buildTextElt(doc, "span", txt, attrs));
    }
    
    private static Element buildTextElt (Document doc, String elttype, String contents, Map attributes) {
        Element te = doc.createElement(elttype);
        if (contents != null && contents.length() > 0) te.appendChild(doc.createTextNode(contents));
        
        if (attributes != null) {
            Entry attr;
            for (Iterator iter = attributes.entrySet().iterator(); iter.hasNext(); ) {
                attr = (Entry)iter.next();
                te.setAttribute(String.valueOf(attr.getKey()), String.valueOf(attr.getValue()));
            }
        }
        
        return te;
    }
}
