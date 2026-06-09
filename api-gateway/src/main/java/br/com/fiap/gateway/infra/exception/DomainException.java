/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.gateway.infra.exception;

public abstract class DomainException extends RuntimeException {

  private String instance;
  private transient Object extensionData;

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
    return getClass().getSimpleName()
        + "{"
        + "message='"
        + getMessage()
        + '\''
        + ", instance='"
        + instance
        + '\''
        + '}';
  }
}
