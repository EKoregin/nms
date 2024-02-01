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
                st.otherinfo, st.user_name, st.tarif, st.ballance, st.state, st.usrip, st.contract, st.stopdate, st.isarchived, st.todaytraffic
            FROM stat as st
            WHERE st.user_name LIKE ?;
            """;

    public List<MikrobillUser> findUserByName(String userName) {
        return jdbcTemplate.query(FIND_USER_INFO_BY_NAME, new MikrobillUserMapper(),
                "%" + userName + "%");
    }
}
