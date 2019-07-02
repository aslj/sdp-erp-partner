package com.solucionesdigitales.partner.repository;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.solucionesdigitales.partner.model.entity.App;

@Repository("appRepository")
public interface AppRepository extends CrudRepository<App,Serializable> {
	List<App> findByAuthor(String author);
	Long countByAuthor(String author);
	
	//customerData
	@Query(value="SELECT p.n_id_partner,p.n_comision,p.n_porcentaje_cuota_patronal,p.n_porcentaje_impuesto_sobre_nomina FROM partner p " + 
				 "JOIN tienda_nomina tn ON tn.n_cliente=p.n_id_partner " + 
				 "JOIN prenomina pn ON pn.n_id_tienda_nomina=tn.n_id_tienda_nomina " + 
				 "WHERE pn.n_id_prenomina=:idPrenomina", nativeQuery=true)
	Object[][] customerData(@Param("idPrenomina") Long idPrenomina);
	
	//availableBalance
	@Query(value="SELECT p.n_limite_credito,m.n_movimientos FROM partner p " + 
				 "JOIN (SELECT :idPartner AS n_id_partner,ROUND(SUM(n_monto*IF(n_tipo_movimiento=1,1,-1)),2) AS n_movimientos " + 
				 "      FROM partner_movimiento WHERE n_id_partner=:idPartner) m " + 
				 "ON m.n_id_partner=p.n_id_partner", nativeQuery=true)
	Object[][] availableBalance(@Param("idPartner") Long idPartner);
	
	//employeesData
	@Query(value="SELECT p.n_id_partner, p.c_identificador, p.c_apellido_paterno, p.c_apellido_materno, p.c_nombre, " + 
				 "       p.d_fecha_ingreso, pbe.d_fecha_baja, pbe.n_es_liquidacion, pbe.n_motivo_de_baja, pi.n_inasistencias, " + 
				 "       IF(psn.n_id_partner IS NULL, p.n_sueldo_neto, psn.n_sueldo_neto), " + 
				 "       IF(psn.n_id_partner IS NULL, p.n_gratificacion, psn.n_gratificacion), " + 
				 "		 p.n_salario_diario, p.n_is_salario_calculado, p.n_factor_integracion, p.n_is_factor_integracion_calculado " + 
				 "FROM partner p " + 
				 "LEFT JOIN (SELECT n_id_partner, SUM(n_dias) AS n_inasistencias FROM periodo_inasistencia " + 
				 "           WHERE n_id_periodo=:idPeriodo AND n_status=1 GROUP BY n_id_partner) pi ON pi.n_id_partner=p.n_id_partner " + 
				 "LEFT JOIN (SELECT n_id_partner, d_fecha_baja, n_es_liquidacion, n_motivo_de_baja FROM periodo_baja_empleado " + 
				 "           WHERE n_id_periodo=:idPeriodo AND n_status=1) pbe ON pbe.n_id_partner=p.n_id_partner " + 
				 "LEFT JOIN (SELECT n_id_partner, n_sueldo_neto, n_gratificacion FROM periodo_sueldo_neto " + 
				 "           WHERE n_id_periodo=:idPeriodo AND n_status=1) psn ON psn.n_id_partner=p.n_id_partner " + 
				 "WHERE p.n_id_tienda_nomina=:idTiendaNomina AND p.n_status=1 AND p.n_tipo_partner=3 AND p.n_id_partner " + 
				 "  NOT IN (SELECT n_id_partner FROM periodo_baja_empleado " + 
				 "          WHERE n_id_periodo IN (SELECT n_id_periodo FROM periodo WHERE n_id_tienda_nomina=:idTiendaNomina " + 
				 "                                 AND n_numero<:numero AND YEAR(d_desde)=LEFT(:desde,4) AND n_status=1) " + 
				 "            AND n_status=1 UNION SELECT n_id_partner FROM partner WHERE n_id_tienda_nomina=:idTiendaNomina " + 
				 "                                 AND d_fecha_fin>'2001-01-01' AND d_fecha_fin<:desde AND n_status=1 " + 
				 "          ORDER BY n_id_partner) " + 
				 "ORDER BY ROUND(p.c_identificador,0)", nativeQuery=true)
	Object[][] employeesData(@Param("idPeriodo") Long idPeriodo, @Param("numero") int numero, @Param("desde") String desde, @Param("idTiendaNomina") Long idTiendaNomina);
	
	//severancesDirectives
	@Query(value="SELECT n_id_partner,d_fecha_baja,n_es_liquidacion FROM periodo_baja_empleado " + 
				 "WHERE n_id_periodo=:idPeriodo AND n_status=1", nativeQuery=true)
	Object[][] severancesDirectives(@Param("idPeriodo") Long idPeriodo);
	
	//payrollFormula
	@Query(value="SELECT c_formula, " + 
				 "       SUBSTRING_INDEX(SUBSTRING_INDEX(c_formula,',',1),'(',-1) AS pr1, " + 
				 "       SUBSTRING_INDEX(SUBSTRING_INDEX(c_formula,')',1),',',-1) AS pr2, " + 
				 "       CONCAT(SUBSTRING_INDEX(SUBSTRING_INDEX(c_formula,')',1),'=',-1),')') AS fnc, " + 
				 "       SUBSTRING_INDEX(c_formula,')',-1) AS ext " + 
				 "FROM tienda_nomina_formula " + 
				 "WHERE n_id_tienda_nomina=:idTiendaNomina AND n_id_variable=:idVariable AND d_desde<=:desde AND n_status=1 " + 
				 "ORDER BY d_desde DESC LIMIT 1", nativeQuery=true)
	Object[][] payrollFormula(@Param("idTiendaNomina") Long idTiendaNomina, @Param("idVariable") int idVariable, @Param("desde") String desde);
	
	//employeesFormula
	@Query(value="SELECT n_id_partner,c_formula FROM " + 
				 "   (SELECT pf.n_id_partner,d_desde,pf.c_formula FROM partner_formula pf JOIN partner p ON p.n_id_partner=pf.n_id_partner " + 
				 "    WHERE pf.n_id_variable=:idVariable AND p.n_tipo_partner=3 AND p.n_status=1 AND p.n_id_tienda_nomina=:idTiendaNomina " + 
				 "    AND pf.d_desde<=:desde AND pf.n_status=1 ORDER BY pf.n_id_partner, pf.d_desde DESC) cb " + 
				 "GROUP BY n_id_partner", nativeQuery=true)
	Object[][] employeesFormula(@Param("idTiendaNomina") Long idTiendaNomina, @Param("idVariable") int idVariable, @Param("desde") String desde);
	
	//calculatedSalaries
	@Query(value="SELECT n_id_partner, " + 
				 "ROUND(JSON_EXTRACT(c_mas_info,'$.salarioDiario'),4) AS n_salario_diario, " + 
				 "JSON_EXTRACT(c_mas_info,'$.esSalarioCalculado') AS n_es_salario_calculado, " + 
				 "ROUND(JSON_EXTRACT(c_mas_info,'$.factorIntegracion'),4) AS n_factor_integracion, " + 
				 "JSON_EXTRACT(c_mas_info,'$.esFactorCalculado') AS n_es_factor_calculado " + 
				 "FROM prenomina_partner WHERE n_id_prenomina=:idPrenomina AND n_status=1 " + 
				 "HAVING n_es_salario_calculado=1 OR n_es_factor_calculado=1", nativeQuery=true)
	Object[][] calculatedSalaries(@Param("idPrenomina") Long idPrenomina);
	
	//finalizedEmployees
	@Query(value="SELECT n_id_partner,LEFT(REPLACE(JSON_EXTRACT(c_mas_info,'$.fechaBaja'),'\"',''),10) AS d_fecha_baja, " + 
				 "JSON_EXTRACT(c_mas_info,'$.motivoDeBaja') AS n_motivo_de_baja " + 
				 "FROM prenomina_partner WHERE n_id_prenomina=:idPrenomina HAVING d_fecha_baja>''", nativeQuery=true)
	Object[][] finalizedEmployees(@Param("idPrenomina") Long idPrenomina);
	
	//usedVariables
	@Query(value="SELECT n_id_variable FROM prenomina_variable " + 
				 "WHERE n_id_prenomina=:idPrenomina AND n_id_variable!=371 AND n_monto1+n_monto2!=0 " + 
				 "GROUP BY n_id_variable", nativeQuery=true)
	Object[][] usedVariables(@Param("idPrenomina") Long idPrenomina);
	
	//variablesToUse
	@Query(value="SELECT v.n_id_variable,v.c_var,v.c_descripcion FROM tienda_nomina_formula tnf " + 
				 "JOIN (SELECT n_id_variable,c_var,c_descripcion FROM variable " + 
				 "      UNION SELECT 300,'90IN','INFONAVIT') v ON v.n_id_variable=tnf.n_id_variable " + 
				 "WHERE tnf.n_id_tienda_nomina=:idTiendaNomina", nativeQuery=true)
	Object[][] variablesToUse(@Param("idTiendaNomina") Long idTiendaNomina);
	
	//prenominaPartnerToNominaPartner
	@Modifying
	@Transactional
	@Query(value="INSERT INTO nomina_partner (n_id_nomina,n_id_partner,n_percepciones1,n_deducciones1,n_percepciones2,n_deducciones2,c_mas_info,n_imss_patronal,c_imss_patronal_detalle,n_status) " + 
				 "SELECT :idNomina,n_id_partner,n_percepciones1,n_deducciones1,n_percepciones2,n_deducciones2,c_mas_info,n_imss_patronal,c_imss_patronal_detalle,n_status " + 
				 "FROM prenomina_partner WHERE n_status=1 AND n_id_prenomina=:idPrenomina", nativeQuery=true)
	void prenominaPartnerToNominaPartner(@Param("idPrenomina") Long idPrenomina, @Param("idNomina") Long idNomina);
	
	//prenominaVariableToNominaVariable
	@Modifying
	@Transactional
	@Query(value="INSERT INTO nomina_variable (n_id_nomina,n_id_partner,n_id_variable,n_es_percepcion,n_monto1,n_monto2,c_mas_info,n_status) " + 
				 "SELECT :idNomina,n_id_partner, " + 
				 "IF(:sIV=0 AND n_id_variable=304,301,n_id_variable) AS n_id_var, " + 
				 "n_es_percepcion, " + 
				 "SUM(n_monto1*IF(:sIV=0 AND n_id_variable=304,-1,1)) AS n_monto1, " + 
				 "SUM(n_monto2*IF(:sIV=0 AND n_id_variable=304,-1,1)) AS n_monto2, " + 
				 "c_mas_info,n_status " + 
				 "FROM prenomina_variable WHERE n_id_prenomina=:idPrenomina " + 
				 "GROUP BY n_id_partner,n_id_var", nativeQuery=true)
	void prenominaVariableToNominaVariable(@Param("idPrenomina") Long idPrenomina, @Param("idNomina") Long idNomina, @Param("sIV") int sIV);
	
	//cleanWorkTables
	@Modifying @Transactional @Query(value="DELETE FROM periodo_baja_empleado WHERE n_id_periodo=:idPeriodo", nativeQuery=true)
	void cleanPeriodoBajaEmpleado(@Param("idPeriodo") Long idPeriodo);
	@Modifying @Transactional @Query(value="DELETE FROM periodo_inasistencia WHERE n_id_periodo=:idPeriodo", nativeQuery=true)
	void cleanPeriodoInasistencia(@Param("idPeriodo") Long idPeriodo);
	@Modifying @Transactional @Query(value="DELETE FROM periodo_sueldo_neto WHERE n_id_periodo=:idPeriodo", nativeQuery=true)
	void cleanPeriodoSueldoNeto(@Param("idPeriodo") Long idPeriodo);
	@Modifying @Transactional @Query(value="DELETE FROM prenomina_variable WHERE n_id_prenomina=:idPrenomina", nativeQuery=true)
	void cleanPrenominaVariable(@Param("idPrenomina") Long idPrenomina);
	@Modifying @Transactional @Query(value="DELETE FROM prenomina_partner WHERE n_id_prenomina=:idPrenomina", nativeQuery=true)
	void cleanPrenominaPartner(@Param("idPrenomina") Long idPrenomina);
	@Modifying @Transactional @Query(value="DELETE FROM prenomina WHERE n_id_prenomina=:idPrenomina", nativeQuery=true)
	void cleanPrenomina(@Param("idPrenomina") Long idPrenomina);
}
