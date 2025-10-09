package com.example.dyeTrack.out.infoExerciceUser;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dyeTrack.core.entity.infoExerciceUser.InfoExerciceUser;
import com.example.dyeTrack.core.entity.infoExerciceUser.InfoExerciceUserId;

public interface InfoExerciceUserRepository extends JpaRepository<InfoExerciceUser, InfoExerciceUserId> {

        @Query("""
                        SELECT i
                        FROM InfoExerciceUser i
                        WHERE (:idUser IS NULL OR i.user.id = :idUser)
                          AND (:favorie IS NULL OR i.favorie = :favorie)
                          AND (:withNote IS NULL OR (i.note IS NOT NULL AND i.note <> ''))
                        """)
        List<InfoExerciceUser> getAllWithFilter(
                        @Param("favorie") Boolean favorie,
                        @Param("withNote") Boolean withNote,
                        @Param("idUser") Long idUser);
}
