package utilidades;

public enum Tratamiento {
    SR, SRA;

    @Override
    public String toString() {
        switch (this) {
            case SR:
                return "Sr.";
            case SRA:
                return "Sra.";
        }
        return this.name();
    }
}
