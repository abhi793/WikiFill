package in.demo.wikifill.Model;

/**
 * Created by Abhishek Pc on 22-12-2016.
 */

public class ListItemModel {
    private String startLine;
    private String endLine;

    public ListItemModel() {
    }


    public ListItemModel(String startLine, String endLine) {
        this.startLine = startLine;
        this.endLine = endLine;
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
}
