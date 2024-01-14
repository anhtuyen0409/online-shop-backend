package com.nguyenanhtuyen.shopapp.components;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import com.nguyenanhtuyen.shopapp.utils.WebUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LocalizationUtil {
	
	private final MessageSource messageSource;
	
	private final LocaleResolver localeResolver;
	
	public String getLocalizedMessage(String messageKey, Object ... params) { // spread operator
		HttpServletRequest request = WebUtils.getCurrentRequest();
		Locale locale = localeResolver.resolveLocale(request);
		return messageSource.getMessage(messageKey, params, locale);
	}

}
