package xpath;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class FragmentContentHandler extends DefaultHandler {

	private String xPath = "/";
	private XMLReader xmlReader;
	private FragmentContentHandler parent;
	private StringBuilder characters = new StringBuilder();
	private Map<String, Integer> elementNameCount = new HashMap<String, Integer>();

	static boolean hasXmlNs = false;

	public FragmentContentHandler(XMLReader xmlReader) {
		this.xmlReader = xmlReader;
	}

	private FragmentContentHandler(String xPath, XMLReader xmlReader, FragmentContentHandler parent) {
		this(xmlReader);
		this.xPath = xPath;
		this.parent = parent;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		Integer count = elementNameCount.get(qName);
		if (null == count) {
			count = 1;
		} else {
			count++;
		}
		elementNameCount.put(qName, count);
		String childXPath = xPath + "/" + qName + "[" + count + "]";

		String tmp = "";
		int attsLength = atts.getLength();
		for (int x = 0; x < attsLength; x++) {
			if (atts.getQName(x).equals("xmlns")) {
				hasXmlNs = true;
			}
			tmp = xPath + "/" + qName + "[@" + atts.getQName(x) + "='" + atts.getValue(x) + "']";
		}
		if (attsLength == 0) {
			tmp = xPath + "/" + qName + "[" + count + "]";
		}

		tmp = tmp.substring(2);
		if (hasXmlNs) {
			tmp = tmp.replaceAll("/", "/_:");
			tmp = "_:" + tmp;
		}

		System.out.println(tmp);

		FragmentContentHandler child = new FragmentContentHandler(childXPath, xmlReader, this);
		xmlReader.setContentHandler(child);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		xmlReader.setContentHandler(parent);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		characters.append(ch, start, length);
	}

}