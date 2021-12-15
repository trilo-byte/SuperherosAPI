package com.trilobyte.superheros.utils;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import com.trilobyte.superheros.exceptions.ApplicationException.ExceptionMessage;

public final class MessageUtils {

  private MessageUtils() {
    // private constructor
  }

  /**
   * Traduce (si procede) un mensaje que hace referencia a un propery de mensajes (el mensaje va
   * entre llaves {...})
   *
   * @param messageSource Fuente de mensajes de la aplicación
   * @param locale Locale del mensaje
   * @param message Objeto que contiene la información del mensaje
   * @return mensaje final
   */
  public static String translateMessage(
      final MessageSource messageSource, final ExceptionMessage message) {
    return translateMessage(LocaleContextHolder.getLocale(), messageSource, message);
  }

  /**
   * Traduce (si procede) un mensaje que hace referencia a un propery de mensajes (el mensaje va
   * entre llaves {...})
   *
   * @param messageSource Fuente de mensajes de la aplicación
   * @param locale Locale del mensaje
   * @param message Objeto que contiene la información del mensaje
   * @return mensaje final
   */
  public static String translateMessage(
      final Locale locale, final MessageSource messageSource, final ExceptionMessage message) {
    final var msg = translateMessage(locale, messageSource, message.getText(), message.getParams());
    final var label = message.getFieldName();
    if (StringUtils.hasText(label)) {
      return label + ": " + msg;
    }
    return msg;
  }

  public static String translateMessage(
      final MessageSource messageSource, final String msg, final Object... params) {
    return translateMessage(LocaleContextHolder.getLocale(), messageSource, msg, params);
  }

  public static String translateMessage(
      final Locale locale,
      final MessageSource messageSource,
      final String msg,
      final Object... params) {
    if (messageSource != null
        && StringUtils.hasText(msg)
        && msg.startsWith("{")
        && msg.endsWith("}")) {
      final var key = msg.substring(1, msg.length() - 1);
      try {
        return messageSource.getMessage(key, params, locale);
      } catch (final NoSuchMessageException ignore) {
        // nada
      }
    }
    return msg;
  }
}
