package io.anvesh.api;

public enum Keys {
	spotifyClientId("39568c134d3e4929a37d8a3d6abde993"),
	spotifyClientSecret("2bb175f68adc4da1849434eeb67e1790"),
	youtubeMusicClientId("332746710454-2ovfhsf4ik2favj81f5qr3eqfc9tioq8.apps.googleusercontent.com"),
	youtubeMusicClientSecret("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");
	
	private String key;
	
	Keys(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
}
