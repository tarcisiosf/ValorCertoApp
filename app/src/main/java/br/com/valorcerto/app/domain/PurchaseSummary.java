package br.com.valorcerto.app.domain;

import java.util.Date;
import java.util.Objects;

/**
 * Representa o resumo de uma compra única:
 * - data da compra
 * - total gasto na compra
 */
public class PurchaseSummary {
    private final Date date;
    private final double total;

    public PurchaseSummary(Date date, double total) {
        this.date  = date;
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseSummary)) return false;
        PurchaseSummary that = (PurchaseSummary) o;
        return Double.compare(that.total, total) == 0 &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, total);
    }
}
