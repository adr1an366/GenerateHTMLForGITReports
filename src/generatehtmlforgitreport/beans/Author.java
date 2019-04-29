/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generatehtmlforgitreport.beans;

/**
 *
 * @author eryalus
 */
public class Author implements Comparable<Author> {

    private String name, email;

    public Author(String line) {
        parseLine(line, '<', '>');
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void parseLine(String line, char emailDelimiterInit, char emailDelimiterEnd) {
        String[] parts = line.split("" + emailDelimiterInit);
        if (parts.length == 2) {
            if (parts[1].trim().endsWith("" + emailDelimiterEnd)) {
                this.name = parts[0].trim();
                this.email = parts[1].trim().replace("" + emailDelimiterEnd, "");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Author) {
            Author ob = (Author) o;
            return ob.email.toLowerCase().equals(this.email.toLowerCase()) && ob.name.toLowerCase().equals(this.name.toLowerCase());
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return name + " [" + email + "]";
    }

    @Override
    public int compareTo(Author o) {
        return o.email.toLowerCase().compareTo(this.email.toLowerCase());
    }

}
