package org.jmarkdownviewer.jmdviewer;

import org.commonmark.node.Node;
import org.jmarkdownviewer.jmdviewer.parser.MarkdownParser;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

public class HtmlPane extends JEditorPane {
    private Node document;

    public HtmlPane() {
        setEditable(false);
        createPane();
    }

    private void createPane() {
        HTMLEditorKit kit = new HTMLEditorKit();
        setEditorKit(kit);

        // add some styles to the html
        StyleSheet stylesheet = kit.getStyleSheet();

        stylesheet.importStyleSheet(HtmlPane.class.getResource("github.css"));

        String imgsrc = HtmlPane.class.getResource("markdown.png").toString();
        // create some simple html as a string
        String htmlString = "<html>\n" + "<body>\n" + "<h1>"
            + "<img src=\"" + imgsrc + "\">"
            + "&nbsp; Markdown Viewer</h1>\n"
            + "<h2>Select a file</h2>\n"
            + "<p>This is some sample text</p>\n"
            + "</body>\n</html>";

        // create a document, set it on the jeditorpane, then add the html
        Document doc = kit.createDefaultDocument();
        setDocument(doc);
        setText(htmlString);

    }

    public void load(String md) {
        MarkdownParser parser = new MarkdownParser();

        parser.parse(md);
        document = parser.getDocument();
        String html = parser.getHTML();
        if (html != null) {
            //System.out.println(url.toString());
            HTMLDocument doc = (HTMLDocument) getDocument();

            setDocument(doc);
            setText(html);
            setCaretPosition(0);
        }
    }

    public void reload() {

    }

    public void HTMLLocalImages(String surl, Image image) {
        try {
            Dictionary cache = (Dictionary) getDocument().getProperty("imageCache");
            if (cache == null) {
                cache = new Hashtable();
                getDocument().putProperty("imageCache", cache);
            }

            URL url = new URL(surl);
            cache.put(url, image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public Image createImage() {
        BufferedImage img = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, 100, 50);

        g.setColor(Color.YELLOW);
        g.fillOval(5, 5, 90, 40);
        img.flush();

        return img;
    }

    public String getasText() {
        MarkdownParser parser = new MarkdownParser(document);
        return parser.getText();
    }

	public String getMD() {
		MarkdownParser parser = new MarkdownParser(document);
		return parser.getMD();		
	}

    public String getCSS() {
        StringBuilder sb = new StringBuilder(1024);
        try {
            String line;
            InputStream in = HtmlPane.class.getResourceAsStream("github.css");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public Node getMDocument() {
        return document;
    }

    public void setMDocument(Node document) {
        this.document = document;
    }
}
