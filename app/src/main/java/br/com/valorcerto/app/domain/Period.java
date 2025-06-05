package br.com.valorcerto.app.domain;

import java.util.Objects;

/**
 * Representa um período de compras (mês/ano) e o total gasto naquele período.
 */
public class Period {
    private final String label;  // ex: "04/2025"
    private final double total;  // soma de todas as compras naquele mês

    public Period(String label, double total) {
        this.label = label;
        this.total = total;
    }

    public String getLabel() {
        return label;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Period)) return false;
        Period period = (Period) o;
        return Double.compare(period.total, total) == 0 &&
                Objects.equals(label, period.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, total);
    }
}
