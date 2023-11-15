package com.wego.assignment.controller.carparks.repo;

import com.wego.assignment.controller.carparks.view.NearestCarPark;
import com.wego.assignment.controller.carparks.view.NearestCarParkRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CarParkRepo  {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CarParkRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<NearestCarPark> findNearestCarPark(Double latitude, Double longitude) {
        String querySql = "select car_park_no, sum(TotalLots) as totalLots, sum(AvailableLots) as availableLots, address, latitude, longitude, " +
                " ST_Distance_Sphere(point(?,?) , point(longitude, latitude)) as distance " +
                " from CarPark cp INNER JOIN CarParkAvailability cpa ON cpa.CarParkNo = cp.car_park_no Group By cp.car_park_no order by distance";
        return jdbcTemplate.query(querySql, new NearestCarParkRowMapper(), longitude, latitude);
    }

    public Page<NearestCarPark> findDemoByPage(Double latitude, Double longitude, Pageable pageable) {
        String rowCountSql = "select count(1) from (" +
                " select sum(cpa.AvailableLots) as availableLots from CarPark cp INNER JOIN CarParkAvailability cpa ON cpa.CarParkNo = cp.car_park_no Group By cp.car_park_no " +
                " HAVING availableLots > 0 order by ST_Distance_Sphere(point(?,?) , point(longitude, latitude)))alias ";
        int total =
                jdbcTemplate.queryForObject(
                        rowCountSql,
                        new Object[]{longitude, latitude}, (rs, rowNum) -> rs.getInt(1)
                );


        String querySql = "select cp.car_park_no as carParkNo, sum(TotalLots) as totalLots, sum(AvailableLots) as availableLots, address, latitude, longitude, " +
                " ST_Distance_Sphere(point(?,?) , point(longitude, latitude)) as distance " +
                " from CarPark cp INNER JOIN CarParkAvailability cpa ON cpa.CarParkNo = cp.car_park_no Group By cp.car_park_no HAVING availableLots > 0 order by distance" +
                " LIMIT " + pageable.getPageSize() +
                " OFFSET " + pageable.getOffset();
        List<NearestCarPark> demos = jdbcTemplate.query(querySql, new NearestCarParkRowMapper(), longitude, latitude);

        return new PageImpl<>(demos, pageable, total);
    }
}
