package dbbwproject.serviceunit.pdfhandler;

public enum DualCitizenship {
    YES("dualCitizenship.yes"),
    NO("dualCitizenship.no");

    private final String formFiledName;

    DualCitizenship(String formFieldName) {
        this.formFiledName = formFieldName;
    }

    public String getFormFieldName() {
        return formFiledName;
    }
}
