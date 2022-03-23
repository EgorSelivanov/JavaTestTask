package converterXML.model;

/**
 * Используется для ключа HashMap
 */
public class KeyPosition {
    private final String depCode;
    private final String depJob;

    public KeyPosition(String depCode, String depJob) {
        this.depCode = depCode;
        this.depJob = depJob;
    }

    public String getDepCode() {
        return depCode;
    }

    public String getDepJob() {
        return depJob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyPosition key = (KeyPosition) o;
        return depCode.equals(key.depCode) && depJob.equals(key.depJob);
    }

    @Override
    public int hashCode() {
        int result = depCode == null ? 0 : depCode.hashCode();
        result = 31 * result + depJob.hashCode();
        return result;
    }
}
