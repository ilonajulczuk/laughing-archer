package domain;


public class Tag {
    public Integer id = -1;
    public String tag;
    public Tag(String tag) {
        this.tag = tag;
    }

    public Tag(Integer id, String tag) {
        this.tag = tag;
        this.id = id;
    }
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                '}';
    }
}
