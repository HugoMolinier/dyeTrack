package com.example.dyeTrack.out.infoExerciseUser;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dyeTrack.core.entity.infoExerciseUser.InfoExerciseUser;
import com.example.dyeTrack.core.entity.infoExerciseUser.InfoExerciseUserId;

public interface InfoExerciseUserRepository extends JpaRepository<InfoExerciseUser, InfoExerciseUserId> {

  @Query("""
      SELECT i
      FROM InfoExerciseUser i
      WHERE (:idUser IS NULL OR i.user.id = :idUser)
        AND (:favorite IS NULL OR i.favorite = :favorite)
        AND (:withNote IS NULL OR (i.note IS NOT NULL AND i.note <> ''))
      """)
  List<InfoExerciseUser> getAllWithFilter(
      @Param("favorite") Boolean favorite,
      @Param("withNote") Boolean withNote,
      @Param("idUser") Long idUser);
}
