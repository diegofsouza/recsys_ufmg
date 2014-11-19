package br.ufmg.repository.json.serializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import br.ufmg.repository.json.wrappers.UserFriendsResult;

public class JsonUserFriendsResultDeserializer extends JsonDeserializer<UserFriendsResult> {

	@Override
	public UserFriendsResult deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		UserFriendsResult result = new UserFriendsResult();
		JsonNode root = jp.readValueAsTree();
		JsonNode response = root.get("friendslist");
		result.setFriends(this.getFriends(response.get("friends")));

		return result;
	}

	private List<Long> getFriends(JsonNode friendsJson) {
		List<Long> friends = new ArrayList<Long>();
		if (friendsJson != null && friendsJson.isArray()) {
			Iterator<JsonNode> elements = friendsJson.getElements();
			while (elements.hasNext()) {
				JsonNode userJson = elements.next();
				friends.add(userJson.get("steamid").asLong());
			}
		}

		return friends;
	}
}
