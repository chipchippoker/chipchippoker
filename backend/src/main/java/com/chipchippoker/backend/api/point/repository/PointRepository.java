package com.chipchippoker.backend.api.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chipchippoker.backend.common.entity.Point;

public interface PointRepository extends JpaRepository<Point,Long> {
}
