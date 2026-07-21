package com.tnpsc.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tnpsc.app.entity.PyqAppearance;

public interface PyqAppearanceRepository extends JpaRepository<PyqAppearance, Long> {

    List<PyqAppearance> findByQuestionId(Long questionId);

    long countByQuestionId(Long questionId);
}
