package at.gusenbauer.taskycompanion.dataclasses;

public class Tag {

    private String tagname;

    public Tag(final String tagname) {
        this.tagname = tagname;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(final String tagname) {
        this.tagname = tagname;
    }
}
