package xpath;

import java.io.FileInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class XPath {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("The first argument specifies an XML file.");
			System.exit(1);
		}

		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		xr.setContentHandler(new FragmentContentHandler(xr));
		xr.parse(new InputSource(new FileInputStream(args[0])));
	}
}