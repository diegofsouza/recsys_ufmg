package br.ufmg.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Review {
	@Id
	@GeneratedValue
	private Long id;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return String.format("%d\\\\% of %d", this.positivePercent, this.total);
	}

}
