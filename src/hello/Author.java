/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hello;

import java.util.List;

import com.google.common.base.Joiner;

import java.util.ArrayList;

/**
 * @author att
 */
public class Author {
    String name;
    String additionalInfo = "No additional information available";
    List<String> writtenTitles = new ArrayList<>();

    public String getName() {
        return this.name;
    }

    public Author(String name, String additionalInfo) {
        this.additionalInfo = additionalInfo;
        this.name = name;
    }

    public Author(String name) {
        this.name = name;
    }

    public void addTitle(String title) {
        writtenTitles.add(title);
    }

    public void addTitles(ArrayList<String> titles) {
        for (String title : titles) {
            writtenTitles.add(title);
        }
    }

    public Author(String name, ArrayList<String> titles) {
        writtenTitles = titles;
        this.name = name;
    }

    public void addAdditionalInfo(String info) {
        this.additionalInfo = info;
    }

    public String getAdditionalInfo() {
        return this.additionalInfo;
    }

    public String provideAllInfo() {
        String separator = "\n";
        String info = "Name: " + this.name + "\n";
        info += "Wrote:\n" + Joiner.on(separator).join(this.writtenTitles);
        info += "\n" + this.additionalInfo;
        return info;
    }


}
