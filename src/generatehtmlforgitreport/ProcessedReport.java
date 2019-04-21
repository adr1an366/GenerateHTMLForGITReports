/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generatehtmlforgitreport;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author eryalus
 */
public class ProcessedReport {

    private ArrayList<Commit> COMMITS = new ArrayList<>();
    private HashMap<String, Integer> NumberOfCommits = new HashMap<>();
    private HashMap<String, String> Colors = new HashMap<>();
    private static final String[] COLORS = new String[]{"#fffac8", "#fabebe", "#e6beff", "#ffd8b1", "#bfef45", "#f58231", "#e6194B", "#ffe119", "#aaffc3", "#3cb44b", "#42d4f4", "#4363d8", "#911eb4", "#f032e6", "#800000", "#9A6324", "#808000", "#469990"};

    public HashMap<String, String> getColors() {
        return Colors;
    }

    public HashMap<String, Integer> getNumberOfCommits() {
        return NumberOfCommits;
    }

    public ArrayList<Commit> getCOMMITS() {
        return COMMITS;
    }

    public ProcessedReport(File file) throws FileNotFoundException, IOException {
        processFile(file);
    }

    private void processFile(File f) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        String line;
        int colorCount = 0;
        boolean isText = false, first = true;
        Commit commit = new Commit();
        while ((line = reader.readLine()) != null) {
            int i = 0;
            while (i < line.length()) {
                char c = line.charAt(i);
                if (c == '*' || c == ' ' || c == '|' || c == '\\' || c == '/') {
                    i++;
                } else {
                    break;
                }
            }
            if (line.contains("commit") && ('c' == line.charAt(i))) {

                isText = false;
                if (first) {
                    first = false;
                } else {
                    COMMITS.add(commit);
                    commit = new Commit();
                }
                commit.setCommit_hash(line.split("commit")[1].trim());
            } else if (line.contains("Author:") && ('A' == line.charAt(i))) {
                commit.setAuthor(line.split("Author:")[1].trim().replace("<", "[").replace(">", "]"));
                Integer commitCount = NumberOfCommits.get(commit.getAuthor());
                if (commitCount != null) {
                    commitCount++;
                } else {
                    commitCount = 1;
                }
                NumberOfCommits.put(commit.getAuthor(), commitCount);
                String colorOfAuthor = Colors.get(commit.getAuthor());
                if (colorOfAuthor == null) {
                    if (colorCount < COLORS.length) {
                        colorOfAuthor = COLORS[colorCount++];
                    } else {
                        colorOfAuthor = "#ffffff";
                    }
                    Colors.put(commit.getAuthor(), colorOfAuthor);
                }
            } else if (line.contains("Merge:") && ('M' == line.charAt(i))) {
                commit.setMerge(line.split("Merge:")[1].trim());
            } else if (line.contains("Date:") && ('D' == line.charAt(i))) {
                commit.setDate(line.split("Date:")[1].trim());
                isText = false;
            } else {
                if (!isText) {
                    isText = true;
                } else {
                    commit.appendText(line.replace("|", "").trim());
                }
            }
            commit.appendBranchTags(line.substring(0, i));
        }
        if (!first) {
            COMMITS.add(commit);
        }
        reader.close();
    }

    public class Commit {

        private String author, commit_hash, date, text = "", merge = "", branchTags = "";

        public String getBranchTags() {
            return branchTags;
        }

        public void setBranchTags(String branchTags) {
            this.branchTags = branchTags;
        }

        public void appendBranchTags(String txt) {
            if (branchTags.equals("")) {
                this.branchTags = txt;
            } else {
                this.branchTags += "\n" + txt;
            }
        }

        public String getMerge() {
            return merge;
        }

        public void setMerge(String merge) {
            this.merge = merge;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getCommit_hash() {
            return commit_hash;
        }

        public void setCommit_hash(String commit_hash) {
            this.commit_hash = commit_hash;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void appendText(String txt) {
            if (text.equals("")) {
                this.text = txt;
            } else {
                this.text += "\n" + txt;
            }
        }

        @Override
        public String toString() {
            String s = "commit: " + commit_hash + "\nAuthor: " + author + "\nDate: " + date + "\n" + text;
            return s;
        }
    }
}
