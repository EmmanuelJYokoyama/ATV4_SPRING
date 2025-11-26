package com.autobots.automanager.modelos;

import java.util.Set;

import lombok.Data;
@Data
public class VendaMolde {
	private String identificacao;
	private Long idEmpresa;
	private Long idCliente;
	private Long idFuncionario;
	private Set<Long> idMercadorias;
	private Set<Long> idServicos;
	private Long idVeiculo;

}
