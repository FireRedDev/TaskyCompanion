package at.gusenbauer.taskycompanion.dataclasses;

/**
 * @author chris
 */
public class Tag {

    private String tagname;

    /**
     * @param tagname
     */
    public Tag(final String tagname) {
        this.tagname = tagname;
    }

    /**
     * @return
     */
    public String getTagname() {
        return tagname;
    }

    /**
     * @param tagname
     */
    public void setTagname(final String tagname) {
        this.tagname = tagname;
    }
}
