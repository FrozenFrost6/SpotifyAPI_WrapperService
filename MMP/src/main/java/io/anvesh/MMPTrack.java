package io.anvesh;

import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

public class MMPTrack {

	private String name;
	private String uri;
	private String id;
	private String albumName;
	private String[] artists;

	public MMPTrack() {
		super();
	}

	public MMPTrack(String name, String uri, String id, String albumName, String[] artists) {
		super();
		this.name = name;
		this.uri = uri;
		this.id = id;
		this.albumName = albumName;
		this.artists = artists;
	}
	
	public MMPTrack(Track track) {
		this.name = track.getName();
		this.uri = track.getUri();
		this.id = track.getId();
		this.albumName = track.getAlbum().getName();
		ArtistSimplified[] spotifyArtists = track.getArtists();
		artists = new String[spotifyArtists.length];
		for (int i = 0; i < spotifyArtists.length; i++) {
			artists[i] = spotifyArtists[i].getName();
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public String[] getArtists() {
		return artists;
	}
	public void setArtists(String[] artists) {
		this.artists = artists;
	}


}
