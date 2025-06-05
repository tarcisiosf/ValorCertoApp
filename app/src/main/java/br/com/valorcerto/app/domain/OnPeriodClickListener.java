package br.com.valorcerto.app.domain;

import br.com.valorcerto.app.domain.Period;

/**
 * Callback para quando um período na lista de histórico for clicado.
 */
public interface OnPeriodClickListener {
    void onPeriodClick(Period period);
}
