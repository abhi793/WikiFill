package in.demo.wikifill.model;
/**
 * Created by Abhishek Pc on 22-12-2016.
 */

public class ListItemModel {
    private String startLine;
    private String endLine;
    private String blank;

    public ListItemModel() {
    }


    public ListItemModel(String startLine, String endLine) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.blank = "";
    }

    public String getStartLine() {
        return startLine;
    }

    public void setStartLine(String startLine) {
        this.startLine = startLine;
    }

    public String getEndLine() {
        return endLine;
    }

    public void setEndLine(String endLine) {
        this.endLine = endLine;
    }

    public String getBlank() {
        return blank;
    }

    public void setBlank(String blank) {
        this.blank = blank;
    }


}