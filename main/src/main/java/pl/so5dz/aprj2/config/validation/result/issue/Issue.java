package pl.so5dz.aprj2.config.validation.result.issue;

import lombok.Value;

@Value
public class Issue {
    Severity severity;
    String message;

    public boolean isError() {
        return severity == Severity.ERROR;
    }

    public boolean isWarning() {
        return severity == Severity.WARNING;
    }
}
