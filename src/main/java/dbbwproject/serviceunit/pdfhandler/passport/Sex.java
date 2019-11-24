package dbbwproject.serviceunit.pdfhandler.passport;

public enum Sex {
    MALE("sex.male"),
    FEMALE("sex.female");

    private final String formFiledName;

    Sex(String formFieldName) {
        this.formFiledName = formFieldName;
    }

    public String getFormFieldName() {
        return formFiledName;
    }
}
