package com.example.ajax.api.batch.config;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class DummyItemWriter<T> implements ItemWriter<java.lang.Object> {

	@Override
	public void write(List<? extends Object> items) throws Exception {

	}

}
