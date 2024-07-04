# Coordy
## Overview 
Coordy is a PaperMC plugin that allows players to add, remove, list, and share in-game coordinates efficiently. It uses CommandAPI for command handling and stores coordinates in a JSON file for easy access and modification.

## Commands 
/coords Lists all of a users saved coordinates for them to see.
/coords add <label> Adds the players current coordinates to their list and labels them.
/coords remove <label> Removes the specified coordinate from the list.
/coords send <player> [label] Sends a specific player your current coordinates (or the coordinates specified).
/coords broadcast [label] Broadcasts the players current coordinates (or the coordinates specified) to the whole server.

## Other Features 
In the configuation files if you set death-coords-enabled to be true then when a player dies they will be sent a private message of the coordinates where they died.
## Examples 
List all of the coordinates you previously saved
/coords
Save your current coordinates to your list as "home"
/coords add home
Save your current coordinates to your list as "A really long name with special characters!"
/coords add A really long name with special characters!
Remove a previously saved coordinate
/coords remove home
Send your current coordinates to another player
/coords send herobrine
Send specific coordinates to another player
/coords send herobrine home
Broadcast your current coordinates to the whole server
/coords broadcast
Broadcast specific coordinates to the whole server
/coords broadcast home
## Thanks to
[JorelAli - CommandAPI](https://github.com/jorelali/commandapi)
