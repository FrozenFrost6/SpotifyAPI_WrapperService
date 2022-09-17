### Spotify API Wrapper Services

The wrapper contains to major parts:

[Setup](https://github.com/FrozenFrost6/SpotifyAPI_WrapperService/edit/main/Readme.md#setup)

[Controller](https://github.com/FrozenFrost6/SpotifyAPI_WrapperService/edit/main/Readme.md#controller)

[Spotify](https://github.com/FrozenFrost6/SpotifyAPI_WrapperService/edit/main/Readme.md#spotify)

## Setup
To use this application, we first need to host the controller service and then host spotify service on different ports. After hosting the services, we need to login and get the user access token. The login process is done securely by spotify's Oauth2 protocol. Once the access token is available, the user can make calls to the spotify service and manipulate playlists and tracks.

## Controller
This service lets the user login and gets the spotify access token which is used to make all the spotify api calls. It also abstracts the spotify service api making it easier to call. 
The main services of the controller include:

1. Login
It is used to communicate with the spotify backend and initiates Oauth2 protocol.
![image](https://user-images.githubusercontent.com/44104814/190856334-eccbee0f-03d7-4b76-9a29-2443df348fcb.png)

2. Get spotify access token
The spotify api backend automatically calls this api after authentication is verified. It returns the access token.
![image](https://user-images.githubusercontent.com/44104814/190856431-e7694bed-7350-4754-9b76-cb2da6629726.png)



## Spotify
This service provides RESTful api to add, remove, and update playlists and songs. The access token obtained from the controller must be used to make these calls.
The services include:



1. Create public playlist

Create a public playlist in the user's spotify library
![image](https://user-images.githubusercontent.com/44104814/190855571-2b6926e8-94d0-4cd2-8d19-be07108f7314.png)

2. Create private playlist

Create a public playlist in the user's spotify library
![image](https://user-images.githubusercontent.com/44104814/190855595-9c57a8c2-f394-4688-bd9a-1d86abaa81b8.png)

3. Add tracks to playlist

Send spotify track object as request body.
![image](https://user-images.githubusercontent.com/44104814/190855666-2aac53e8-51e2-4714-a60a-2ab6852540b5.png)

4. Search for a song on Spotify

Search for a track on the spotify backend. It returns spotify track object as a result of the search.
![image](https://user-images.githubusercontent.com/44104814/190855765-ede4d703-8671-403b-805f-49e96183aef2.png)

5. Get user playlists

Gets a list of all the user's playlists in their library. It returns a list of all user playlists as an array of spotify playlist object.
![image](https://user-images.githubusercontent.com/44104814/190855848-123b0ebd-1a18-456c-9a95-09fd458e55fa.png)

6. Get playlist tracks

Gets all the tracks present in the specified playlist. It returns a list of all the tracks in the playlist as an array of spotify track object.
![image](https://user-images.githubusercontent.com/44104814/190856008-800ed670-828a-4f95-a0b7-ae2eaaf274a5.png)

7. Remove playlist tracks

Removes all the specified tracks in the specified playlist. It accepts an array of spotify track objects.
![image](https://user-images.githubusercontent.com/44104814/190855977-dcb067d5-74bf-45dc-a1fe-a1d2e0156d2b.png)
