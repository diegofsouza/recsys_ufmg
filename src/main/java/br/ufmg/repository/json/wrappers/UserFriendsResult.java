package br.ufmg.repository.json.wrappers;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import br.ufmg.repository.json.serializers.JsonUserFriendsResultDeserializer;

@Data
@JsonDeserialize(using = JsonUserFriendsResultDeserializer.class)
public class UserFriendsResult {
	private List<Long> friends;
}
