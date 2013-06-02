/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.List;

import com.google.common.base.Joiner;

import java.util.ArrayList;

public class Author {
    public String name;
    public Integer id = -1;
    public String additionalInfo = "No additional information available";
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


    public String getAdditionalInfo() {
        return this.additionalInfo;
    }



}
