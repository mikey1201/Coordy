# Coordy
Coordy is a PaperMC plugin that allows players to add, remove, list, and share in-game coordinates efficiently. It uses CommandAPI for command handling and stores coordinates in a JSON file for easy access and modification.

## Commands 
**/coords** *Lists all of a users saved coordinates for them to see.*  
**/coords add \<label>** *Adds the players current coordinates to their list and labels them.*  
**/coords remove \<label>** *Removes the specified coordinate from the list.*  
**/coords share \<player> [\<label>]** *Shares your current or chosen coordinates with the selected player.*  

## Examples 
**List all of the coordinates you previously saved**  
`/coords`  
**Save your current coordinates to your list as "home"**  
`/coords add home`  
**Save your current coordinates to your list as "A really long name with special characters!"**  
`/coords add A really long name with special characters!`  
**Remove a previously saved coordinate**  
`/coords remove home`  
**Share your current coordinates with another player**  
`/coords share herobrine`  
**Share specific coordinates with another player**  
`/coords share herobrine home`  
**Share your current coordinates with the whole server**  
`/coords share @a`  
**Share specific coordinates with the whole server**  
`/coords share @a home`  

## Other Features 
In the configuation files if you set death-coords-enabled to be true then when a player dies they will be sent a private message of the coordinates where they died.  
You can configure the permissions required to use the @a target selector variable with the `/coords share` command by adjusting share-coords-all in the config file.  
Plugin statistics are recorded with bStats now, to disable this go to plugins\bStats\config.yml .  
Console log now checks for updates on server start.  

## Thanks to
[JorelAli - CommandAPI](https://github.com/jorelali/commandapi)
