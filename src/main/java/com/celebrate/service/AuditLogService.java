package com.celebrate.service;

import com.celebrate.dto.response.AuditLogResponse;
import com.celebrate.dto.response.OwnerSimpleResponse;
import com.celebrate.entity.AuditLogEntity;
import com.celebrate.repository.AuditLogRepository;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public Map<String, Object> getAuditLogs(Integer page, Integer limit) {
        SecurityUtil.requireRole("ADMIN");
        int pageNum = page != null ? Math.max(0, page - 1) : 0;
        int pageSize = limit != null ? limit : 20;

        Page<AuditLogEntity> result = auditLogRepository.findAll(
                PageRequest.of(pageNum, pageSize, Sort.by("timestamp").descending()));

        return Map.of(
                "auditLogs", result.getContent().stream().map(this::toResponse).toList(),
                "totalCount", result.getTotalElements(),
                "currentPage", pageNum + 1,
                "totalPages", result.getTotalPages(),
                "nextPage", result.hasNext() ? pageNum + 2 : null,
                "prevPage", result.hasPrevious() ? pageNum : null
        );
    }

    private AuditLogResponse toResponse(AuditLogEntity e) {
        OwnerSimpleResponse admin = null;
        if (e.getAdmin() != null) {
            admin = OwnerSimpleResponse.builder()
                    .id(e.getAdmin().getId())
                    .email(e.getAdmin().getEmail())
                    .isActive(e.getAdmin().getIsActive())
                    .build();
        }
        return AuditLogResponse.builder()
                .id(e.getId())
                .timestamp(e.getTimestamp() != null ? e.getTimestamp().toString() : null)
                .admin(admin)
                .action(e.getAction())
                .targetType(e.getTargetType())
                .targetId(e.getTargetId())
                .changes(e.getChanges())
                .build();
    }
}
