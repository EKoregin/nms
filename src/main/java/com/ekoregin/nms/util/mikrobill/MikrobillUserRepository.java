package com.ekoregin.nms.util.mikrobill;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MikrobillUserRepository {

    @Qualifier("mikrobillJdbcTemplate")
    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_USER_INFO_BY_NAME = """
            SELECT 
                REPLACE(CONCAT(autorize, personal_info, ' '), '<?xml version="1.0" encoding="utf-8"?><VariableData xmlns="', '<') as result
             FROM users_list
             WHERE autorize LIKE ?;
            """;

    public List<MikrobillUser> findUserByName(String userName) {
        return jdbcTemplate.query(FIND_USER_INFO_BY_NAME, new MikrobillUserMapper(),
                "%" + userName + "%");
    }
}
