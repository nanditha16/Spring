package com.example.utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.builder.EntityBuilder;
import com.example.exception.NotFoundException;
import com.example.exception.ServiceException;
import com.example.model.EMSEntity;

public class EntityHelper {

	private static final Logger log = LoggerFactory.getLogger(EntityHelper.class);

	public static EMSEntity getEntity(String uuid, String type) {
		try {
			String classBuilder = Constants.BASE_BUILDER_PACKAGE + type + "Builder";
			EntityBuilder builder = (EntityBuilder) Class.forName(classBuilder).newInstance();
			return (EMSEntity) builder.getRepository().findByUuid(uuid);
		} catch (ClassNotFoundException e) {
			log.error(String.format("EntityModel not found for type %s", type), e);
			throw new ServiceException(String.format("EntityModel not found for type %s", type));
		} catch (Exception e) {
			log.error(String.format("Unable to create builder class for type %s", type), e);
			throw new ServiceException(String.format("Unable to create builder class for type %s", type));
		}
	}

	public static EMSEntity updateEntity(String uuid, String type, EMSEntity entity) {
		EMSEntity emsEntity;
		try {
			String classBuilder = Constants.BASE_BUILDER_PACKAGE + type + "Builder";
			EntityBuilder builder = (EntityBuilder) Class.forName(classBuilder).newInstance();
			EMSEntity existingEntity = (EMSEntity) builder.getRepository().findByUuid(uuid);
			if (existingEntity == null) {
				throw new NotFoundException(String.format("Entity not found for uuid %s", uuid));
			}
			emsEntity = builder.saveEntity(entity);
		} catch (ClassNotFoundException e) {
			log.error(String.format("EntityModel not found for type %s", type), e);
			throw new ServiceException(String.format("EntityModel not found for type %s", type));
		} catch (Exception e) {
			log.error(String.format("Unable to create builder class for type %s", type), e);
			throw new ServiceException(String.format("Unable to create builder class for type %s", type));
		}
		return emsEntity;
	}

	public static EMSEntity createEntity(String type, EMSEntity entity) {
		EMSEntity emsEntity;
		try {
			String classBuilder = Constants.BASE_BUILDER_PACKAGE + type + "Builder";
			EntityBuilder builder = (EntityBuilder) Class.forName(classBuilder).newInstance();
			emsEntity = builder.saveEntity(entity);
		} catch (ClassNotFoundException e) {
			log.error(String.format("EntityModel not found for type %s", type), e);
			throw new ServiceException(String.format("EntityModel not found for type %s", type));
		} catch (Exception e) {
			log.error(String.format("Unable to create builder class for type %s", type), e);
			throw new ServiceException(String.format("Unable to create builder class for type %s", type));
		}
		return emsEntity;
	}

	public static void deleteEntity(String uuid, String type, EMSEntity entity) {
		try {
			String classBuilder = Constants.BASE_BUILDER_PACKAGE + type + "Builder";
			EntityBuilder builder = (EntityBuilder) Class.forName(classBuilder).newInstance();
			EMSEntity existingEntity = (EMSEntity) builder.getRepository().findByUuid(uuid);

			if (existingEntity == null) {
				throw new NotFoundException(String.format("Entity not found for uuid %s", uuid));
			}
			builder.deleteEntity(entity);
		} catch (ClassNotFoundException e) {
			log.error(String.format("EntityModel not found for type %s", type), e);
			throw new ServiceException(String.format("EntityModel not found for type %s", type));
		} catch (Exception e) {
			log.error(String.format("Unable to create builder class for type %s", type), e);
			throw new ServiceException(String.format("Unable to create builder class for type %s", type));
		}

	}

	public static void applicationUTCTTimezoneCheck(String string) {
		log.info("***************************");

		ZoneId application_TimeZone = ZonedDateTime.now().getZone();
		log.info("Application_TimeZone is: " + ZonedDateTime.now().getZone());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		LocalDateTime localtDateAndTime = LocalDateTime.parse(string, formatter);
		ZonedDateTime dateAndTimeInBangalore = ZonedDateTime.of(localtDateAndTime, application_TimeZone);

		log.info("Current date and time in this particular timezone : " + dateAndTimeInBangalore);
		ZonedDateTime utcDate = dateAndTimeInBangalore.withZoneSameInstant(ZoneOffset.UTC);
		log.info("Current date and time in UTC (saved in MongoDB): " + utcDate);

		log.info("***************************");

	}

}
