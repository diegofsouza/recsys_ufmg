package br.ufmg.repository.json.wrappers;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import br.ufmg.domain.User;
import br.ufmg.repository.json.serializers.JsonUserResultDeserializer;

@Data
@JsonDeserialize(using = JsonUserResultDeserializer.class)
public class UserResult {
	private List<User> users;
}
