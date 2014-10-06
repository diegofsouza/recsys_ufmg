package br.ufmg.domain;

public class Review {
	private int total;
	private int positivePercent;

	public long getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPositivePercent() {
		return positivePercent;
	}

	public void setPositivePercent(int positivePercent) {
		this.positivePercent = positivePercent;
	}

	@Override
	public String toString() {
		return String.format("%d\\\\% of %d", this.positivePercent, this.total);
	}

}
