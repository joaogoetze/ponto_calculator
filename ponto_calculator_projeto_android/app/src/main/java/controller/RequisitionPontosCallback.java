package controller;

import java.util.List;

import model.Ponto;

public interface RequisitionPontosCallback
{
    void onRequisitionPontosResult(List<Ponto> listaPontos);
}
