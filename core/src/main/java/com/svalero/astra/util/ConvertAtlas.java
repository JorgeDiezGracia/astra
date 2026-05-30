package com.svalero.astra.util;

import java.io.*;
import java.nio.file.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class ConvertAtlas {

    public static void main(String[] args) throws Exception {
        File xmlFile = new File("assets/atlas/sheet.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        NodeList nodes = doc.getElementsByTagName("SubTexture");

        StringBuilder sb = new StringBuilder();
        sb.append("sheet.png\n");
        sb.append("size: 1024,1024\n");
        sb.append("format: RGBA8888\n");
        sb.append("filter: Linear,Linear\n");
        sb.append("repeat: none\n");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            String name = el.getAttribute("name");
            String x    = el.getAttribute("x");
            String y    = el.getAttribute("y");
            String w    = el.getAttribute("width");
            String h    = el.getAttribute("height");

            // Quitar extensión .png si la tiene
            if (name.endsWith(".png")) name = name.substring(0, name.length() - 4);

            sb.append(name).append("\n");
            sb.append("  rotate: false\n");
            sb.append("  xy: ").append(x).append(", ").append(y).append("\n");
            sb.append("  size: ").append(w).append(", ").append(h).append("\n");
            sb.append("  orig: ").append(w).append(", ").append(h).append("\n");
            sb.append("  offset: 0, 0\n");
            sb.append("  index: -1\n");
        }

        Files.writeString(Path.of("assets/atlas/astra.atlas"), sb.toString());
        System.out.println("Done! astra.atlas created with " + nodes.getLength() + " sprites.");
    }
}
