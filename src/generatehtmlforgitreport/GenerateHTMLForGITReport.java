/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generatehtmlforgitreport;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import generatehtmlforgitreport.ProcessedReport.Commit;
import generatehtmlforgitreport.templates.TemplateChooser;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eryalus
 */
public class GenerateHTMLForGITReport {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String projectName = "", originFile, destinationFile;
        Scanner scan = new Scanner(System.in);
        System.out.println("Origin file:");
        originFile = scan.nextLine().trim();
        File f = new File(originFile);
        if (!f.exists() || f.isDirectory()) {
            System.err.println("Seleted file \"" + originFile + "\" does not exists or it's a directory.");
            return;
        }
        System.out.println("Destionation file:");
        destinationFile = scan.nextLine().trim();
        File destFile = new File(destinationFile);
        if (destFile.isDirectory()) {
            System.err.println("Seleted file \"" + originFile + "\" it's a directory.");
            return;
        } else if (destFile.exists()) {
            System.out.print("Destination file already exists. Do you want to override? [y/n] ");
            String op = scan.nextLine().trim();
            if (op.toLowerCase().equals("n")) {
                return;
            }
        }
        System.out.println("Project name:");
        projectName = scan.nextLine().trim();

        try {
            ProcessedReport report = new ProcessedReport(f);
            InputStream templateStream = new TemplateChooser().getTemplateUrl().openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(templateStream));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile)));
            String l;
            while ((l = br.readLine()) != null) {
                if (l.contains("${authors_commits}")) {
                    HashMap<String, Integer> numberOfCommitsByAuthor = report.getNumberOfCommits();
                    for (String author : numberOfCommitsByAuthor.keySet()) {
                        bw.write("<h3><label><font color=\"" + report.getColors().get(author) + "\">\u25A0</font></label> " + author + ": " + numberOfCommitsByAuthor.get(author) + "commits (" + String.format("%.2f", 100 * ((float) numberOfCommitsByAuthor.get(author)) / report.getCOMMITS().size()) + "%)<label><font color=\"" + report.getColors().get(author) + "\">\u25A0</font></label></h3>\n");
                    }
                } else if (l.contains("${table_body}")) {
                    for (Commit c : report.getCOMMITS()) {
                        String color = "white";
                        String colorOfAuthor = report.getColors().get(c.getAuthor());
                        if (colorOfAuthor != null) {
                            color = colorOfAuthor;
                        }
                        bw.write("<tr bgcolor=\"" + color + "\"" + ">\n");
                        bw.write("<td>" + c.getBranchTags().replace("\n", "<br>") + "</td>");
                        bw.write("<td>" + c.getCommit_hash() + "</td>");
                        bw.write("<td>" + c.getAuthor() + "</td>");
                        bw.write("<td>" + c.getDate() + "</td>");
                        bw.write("<td>" + c.getMerge() + "</td>");
                        bw.write("<td>" + c.getText() + "</td>");
                        bw.write("\n</tr>\n");
                    }
                } else {
                    bw.write(l.replace("${project_name}", projectName).replace("${number_of_commits}", "" + report.getCOMMITS().size()) + "\n");
                }
            }
            br.close();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(GenerateHTMLForGITReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
