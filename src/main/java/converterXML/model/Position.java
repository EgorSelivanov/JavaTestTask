package converterXML.model;

/**
 * Класс-модель таблицы должностей
 */
public class Position extends BaseModel{
    private String depCode;
    private String depJob;
    private String description;

    public Position(int id, String depCode, String depJob, String description) {
        super(id);
        this.depCode = depCode;
        this.depJob = depJob;
        this.description = description;
    }

    public Position(String depCode, String depJob, String description) {
        this.depCode = depCode;
        this.depJob = depJob;
        this.description = description;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getDepJob() {
        return depJob;
    }

    public void setDepJob(String depJob) {
        this.depJob = depJob;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position key = (Position) o;
        if (!depCode.equals(key.depCode) || !depJob.equals(key.depJob)) return false;
        return description.equals(key.description);
    }

    @Override
    public int hashCode() {
        int result = depCode == null ? 0 : depCode.hashCode();
        result = 31 * result + depJob.hashCode();
        result = 29 * result + description.hashCode();
        return result;
    }
}
