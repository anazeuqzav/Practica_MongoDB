package org.dao;

import org.modelo.Programa;

import java.time.LocalDate;
import java.util.List;

public interface ProgramaDAO {
    void insertarPrograma(Programa programa);
    Programa buscarProgramaPorId(String id);
    List<Programa> buscarTodosProgramas();
    void actualizarPrograma (Programa programa);
    List<Programa> buscarPorProgramaCategoria(String categoria);
    Programa programaConMayorAudiencia(LocalDate fecha);
    double calcularProgramaAudienciaMedia(String id, LocalDate fechaInicio, LocalDate fechaFin);
}
