package converterXML.model;

public class BaseModel {
    protected int id;

    public BaseModel() {}

    public BaseModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseModel baseModel = (BaseModel) o;
        return this.id == baseModel.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
