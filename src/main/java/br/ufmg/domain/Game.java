package br.ufmg.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class Game {
	private Integer id;
	private String name;
	private List<String> tags;
	private List<String> categories;
	private List<String> genres;
	private List<String> developers;
	private List<String> publishers;
	private Date releaseDate;
	private String about;
	private int totalReview;
	private int positivePercentReview;

	@Override
	public String toString() {
		return String.format("%d: %s", this.id, this.name);
	}
}
