package mx.meido.rfcdoconline.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RfcDocRepository extends JpaRepository<RfcDocEntity, String> {

    @Query(value = "select * from rfc_doc r order by r.rfc_id DESC limit 1", nativeQuery = true)
    Optional<RfcDocEntity> queryLastRfc();

    @Query(value = "select * from rfc_doc r where r.rfc_id >= ? order by r.rfc_id asc limit 100", nativeQuery = true)
    List<RfcDocEntity> queryRfcPage(String start);
}
