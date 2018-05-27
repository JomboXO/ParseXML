package com.company;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class Main {

    static List<Member> list = new ArrayList<>();

    public static void main(String[] args) {

        String mainStr = "<projects>\n" +
                "    <project name=\"xml\">\n" +
                "        <member role=\"developer\" name=\"Fedya\"/>\n" +
                "        <member role=\"manager\" name=\"Ivan\"/>\n" +
                "        <member role=\"manager\" name=\"Fedya\"/>\n" +
                "    </project>\n" +
                "</projects>";

        String secondStr = "<projects>\n" +
                "    <project name=\"xml\">\n" +
                "        <member role=\"developer\" name=\"fedya\"/>\n" +
                "        <member role=\"manager\" name=\"Ivan\"/>\n" +
                "    </project>\n" +
                "    <project name=\"rpc\">\n" +
                "        <member role=\"developer\" name=\"fedya\"/>\n" +
                "    </project>\n" +
                "</projects>";

        parseXml(mainStr);

        sortByName();
        sortByProject();
        sortByRoleName();

        /* Result */
        System.out.println(createNewXml());
    }

    private static void parseXml(String string) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(string));
            Document doc = db.parse(is);

            NodeList nodeList = doc.getElementsByTagName("project");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node project = nodeList.item(i);
                Element element = (Element) project;
                String projectMember = element.getAttribute("name");
                NodeList nodeMem = element.getElementsByTagName("member");
                for (int j = 0; j < nodeMem.getLength(); j++) {
                    Node member = nodeMem.item(j);
                    Element element1 = (Element) member;
                    String roleMember = element1.getAttribute("role");
                    String nameMember = element1.getAttribute("name");
                    list.add(new Member(nameMember, roleMember, projectMember));
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private static String createNewXml() {
        String string = "<members>\n";
        for (int i = 0; i < list.size(); i++) {
            string += "\t<member name=\"" + list.get(i).name + "\">\n";
            int k = i;
            while (k < list.size() && list.get(i).name.compareTo(list.get(k).name) == 0) {
                string += "\t\t<role name=\"" + list.get(k).role + "\" project=\"" + list.get(k).project + "\"/>\n";
                k++;
            }
            string += "\t</member>\n";
            i = --k;
        }
        string += "</members>";
        return string;
    }


    private static void sortByRoleName() {
        for (int i = 0; i < list.size() - 1; i++)
            if (list.get(i).name.compareTo(list.get(i + 1).name) == 0 && list.get(i).role.compareTo(list.get(i + 1).role) > 0) {
                change(i, i + 1);
            }

    }

    private static void sortByProject() {
        for (int i = 0; i < list.size() - 1; i++)
            if (list.get(i).name.compareTo(list.get(i + 1).name) == 0 && list.get(i).project.compareTo(list.get(i + 1).project) > 0) {
                change(i, i + 1);
            }

    }

    private static void sortByName() {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).name.compareTo(list.get(i + 1).name) > 0) {
                change(i, i + 1);
            }
        }
    }

    private static void change(int i, int j) {
        Member tmp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, tmp);
    }
}

