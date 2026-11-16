package ru.rodionov.payment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.rodionov.payment.contract.account.model.CreateHoldRequest;
import ru.rodionov.payment.contract.account.model.HoldDTO;
import ru.rodionov.payment.entity.Hold;
import ru.rodionov.starter.mapper.BaseMapper;

import java.time.OffsetDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HoldMapper extends BaseMapper<Hold, HoldDTO> {

    @Mapping(target = "expiresAt", source = ".", qualifiedByName = "expiresAt")
    Hold toEntity(CreateHoldRequest dto);

    @Named("expiresAt")
    default OffsetDateTime expiresAt(CreateHoldRequest dto) {
        return OffsetDateTime.now().plusSeconds(dto.getTtlSeconds());
    }
}
