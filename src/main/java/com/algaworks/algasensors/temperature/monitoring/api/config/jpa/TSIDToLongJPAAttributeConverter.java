package com.algaworks.algasensors.temperature.monitoring.api.config.jpa;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TSIDToLongJPAAttributeConverter implements AttributeConverter<TSID, Long> {
    @Override
    public Long convertToDatabaseColumn(TSID attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toLong();
    }

    @Override
    public TSID convertToEntityAttribute(Long dbData) {
        if (dbData == null) {
            return null;
        }
        return TSID.from(dbData);
    }
}
