/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository.jpa;

import com.umg.bienestar.sesiones_bienestar.entity.Notificacion;
import com.umg.bienestar.sesiones_bienestar.entity.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 *
 * @author amada
 */

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioId(Long usuarioId);
    List<Notificacion> findByUsuarioIdAndLeidaFalse(Long usuarioId);
    List<Notificacion> findByTipo(TipoNotificacion tipo);
    List<Notificacion> findByEnviadaFalse();
}

