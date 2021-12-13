package com.trilobyte.superheros.exceptions;

@lombok.Getter
public class HeroNotFoundException extends ApplicationException {

  private static final long serialVersionUID = 6921281284428988662L;

  public HeroNotFoundException(final String reason) {
    super(reason);
  }

  @Override
  public int getCode() {
    return 404;
  }
}
