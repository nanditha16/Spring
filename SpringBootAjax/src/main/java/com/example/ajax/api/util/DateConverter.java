package com.example.ajax.api.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

@Component
public class DateConverter implements Converter {

	private static final String            DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);

	public DateConverter() {
		super();
	}
	
	@Override
	public boolean canConvert(Class type) {
		return LocalDate.class.isAssignableFrom(type);
	}

	/*
	 * Convert LocalDate to String
	 */
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		LocalDate date = (LocalDate) source;
		String result = date.format(DEFAULT_DATE_FORMATTER);
		writer.setValue(result);
	}

	/*
	 * Convert XMl to LocalDate
	 */
	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		LocalDate result = LocalDate.parse(reader.getValue(), DEFAULT_DATE_FORMATTER);
		return result;
	}
}
