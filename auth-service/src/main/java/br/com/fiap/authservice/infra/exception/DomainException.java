package br.com.fiap.authservice.infra.exception;

/**
 * Base exception class for all domain-level exceptions.
 *
 * Domain exceptions are automatically converted to RFC 9457 Problem Details
 * by the GlobalExceptionHandler if annotated with @ProblemType.
 *
 * Extend this class to create service-specific exceptions that will be
 * automatically handled and converted to standardized error responses.
 *
 * @see ProblemType
 * @see GlobalExceptionHandler
 */
public abstract class DomainException extends RuntimeException {

    /**
     * The specific problem instance identifier.
     * Used to populate the 'instance' field in the Problem Detail.
     */
    private String instance;

    /**
     * Additional problem-specific details for extensions.
     */
    private transient Object extensionData;

    // ====== Constructors ======

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, String instance) {
        super(message);
        this.instance = instance;
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainException(String message, String instance, Throwable cause) {
        super(message, cause);
        this.instance = instance;
    }

    public DomainException(String message, String instance, Object extensionData) {
        super(message);
        this.instance = instance;
        this.extensionData = extensionData;
    }

    // ====== Getters & Setters ======

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public Object getExtensionData() {
        return extensionData;
    }

    public void setExtensionData(Object extensionData) {
        this.extensionData = extensionData;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "message='" + getMessage() + '\'' +
                ", instance='" + instance + '\'' +
                '}';
    }
}
