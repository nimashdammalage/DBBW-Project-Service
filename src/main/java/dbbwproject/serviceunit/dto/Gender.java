package dbbwproject.serviceunit.dto;

public enum Gender {
    MALE("sex.male"),
    FEMALE("sex.female");

    private final String formFiledName;

    Gender(String formFieldName) {
        this.formFiledName = formFieldName;
    }

    public String getFormFieldName() {
        return formFiledName;
    }
}
