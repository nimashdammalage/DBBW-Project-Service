package dbbwproject.serviceunit.pdfhandler.passport;

public enum TypeOfService {
    NORMAL("typeOfService.normal"),
    ONE_DAY("typeOfService.oneDay");

    private final String formFiledName;

    TypeOfService(String formFieldName) {
        this.formFiledName = formFieldName;
    }

    public String getFormFieldName() {
        return formFiledName;
    }
}
