package com.example.Game_Backend.CollaborativeEditing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditingMapRepository extends JpaRepository<EditingMap, Long> {

// EditingMapHelper helper = new EditingMapHelper();
// String tableName1 = helper.tableName();

// @Modifying
// @Transactional
// @Query(value = "CREATE TABLE IF NOT EXISTS :tablename1 AS SELECT * FROM editing_map WHERE 1=0", nativeQuery = true)
// void createTempTable(@Param("tableName") String tableName);

//    @Modifying
//    @Transactional
//    @Query(value = "DROP TABLE IF EXISTS temp_table", nativeQuery = true)
//    void deleteTempTable();
//
//    @Modifying
//    @Query(value = "INSERT INTO temp_table (editingMap_Id, Body, WorldData) VALUES (:editingMapId, :body, :worldData)", nativeQuery = true)
//    @Transactional
//    void insertIntoTempTable(Long editingMapId, String body, String worldData);

}
