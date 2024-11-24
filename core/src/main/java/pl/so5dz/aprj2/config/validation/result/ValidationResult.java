package pl.so5dz.aprj2.config.validation.result;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import pl.so5dz.aprj2.config.validation.result.issue.Issue;
import pl.so5dz.aprj2.config.validation.result.issue.Severity;

@Getter
public class ValidationResult {
    private List<Issue> issues = new ArrayList<>();

    public boolean hasError() {
        return issues.stream().anyMatch(Issue::isError);
    }

    public boolean hasWarning() {
        return issues.stream().anyMatch(Issue::isWarning);
    }

    public void addFrom(ValidationResult another) {
        issues.addAll(another.getIssues());
    }

    public void addError(String message) {
        addIssue(Severity.ERROR, message);
    }

    public void addError(String format, Object... args) {
        addIssue(Severity.ERROR, String.format(format, args));
    }

    public void addWarning(String message) {
        addIssue(Severity.WARNING, message);
    }

    public void addWarning(String format, Object... args) {
        addIssue(Severity.WARNING, String.format(format, args));
    }

    private void addIssue(Severity severity, String message) {
        issues.add(new Issue(severity, message));
    }
}
