# Steam OpenID
This project contains a single class with some static methods to help you to use Steam OpenID in your own project.

### How to authenticate
1. Generate a url with `getLoginURL` and redirect the user to it.
   - After the user logs in, Steam will redirect the user to the `callbackURL` you provided.
2. Check if the login was successful with `validateLogin`
3. If the login was successful, you can get the user's SteamID with `getUserID`
   - Store the SteamID in a session or database to keep track of the user, and use it to make Steam web API requests. 