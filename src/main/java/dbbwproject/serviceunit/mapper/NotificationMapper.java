package dbbwproject.serviceunit.mapper;

import dbbwproject.serviceunit.dao.Notification;
import dbbwproject.serviceunit.dto.NotificationDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = {DateMapper.class})
public abstract class NotificationMapper {

    public abstract List<Notification> mapNDtoToNList(List<NotificationDto> notificationDTO);

    public abstract Notification mapNDtoToN(NotificationDto notificationDTO);

    public abstract List<NotificationDto> mapNToNDtoList(List<Notification> n);

    public abstract NotificationDto mapNToNDto(Notification n);


    public abstract Notification modNDtoToN(NotificationDto n, @MappingTarget Notification nt);
}
