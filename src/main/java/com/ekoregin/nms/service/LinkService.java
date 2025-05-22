package com.ekoregin.nms.service;

import com.ekoregin.nms.database.repository.LinkRepository;
import com.ekoregin.nms.dto.LinkReadDto;
import com.ekoregin.nms.mapper.LinkReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LinkService {

    private final LinkRepository linkRepository;
    private final LinkReadMapper linkReadMapper;

    public List<LinkReadDto> findAllByDeviceId(Long deviceId) {
        return linkRepository.findAllByDeviceId(deviceId)
                .stream().map(linkReadMapper::map)
                .toList();
    }
}
