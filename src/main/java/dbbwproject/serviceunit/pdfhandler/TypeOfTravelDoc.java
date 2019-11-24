package dbbwproject.serviceunit.pdfhandler;

public enum TypeOfTravelDoc {
    ALL_COUNTRIES("typeOfTravelDoc.allCountries"),
    MIDDLE_EAST_COUNTRIES("typeOfTravelDoc.middleEastCountries"),
    EMERGENCY_CERTIFICATE("typeOfTravelDoc.emergencyCertificate"),
    IDENTITY_CERTIFICATE("typeOfTravelDoc.identityCertificate");

    private final String formFiledName;

    TypeOfTravelDoc(String formFieldName) {
        this.formFiledName = formFieldName;
    }

    public String getFormFieldName() {
        return formFiledName;
    }
}
