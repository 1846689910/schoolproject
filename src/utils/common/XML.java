package utils.common;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;

public class XML{

    public static Document readXML (String path) {
        if (! new File(path).isFile()) return null;
        try {
            return new SAXReader().read(path);
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean writeXML (Document doc, String path, boolean pretty) {
        OutputFormat format = pretty ? OutputFormat.createPrettyPrint() : OutputFormat.createCompactFormat();
        try {
            FileOutputStream fos = new FileOutputStream(path);
            XMLWriter xmlWriter = new XMLWriter(fos, format);
            xmlWriter.write(doc);
            xmlWriter.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Namespace getNameSpace (String prefix, String uri){
        return new Namespace(prefix, uri);
    }

    public static Element addElement (Element baseElement, String name, Namespace namespace) {
        if (namespace != null) {
            return baseElement.addElement(new QName(name, namespace));
        } else {
            return baseElement.addElement(name);
        }
    }

}
