# SimpleTabList
This Spigot plugin is an open-source projekt. It's kinda also planned to show how easy it is to make simple plugins which normaly cost much money for quite the same content or being coded quite bad with many configs just for permissions and stuff.

## Dependencys

- [LuckPerms](https://luckperms.net/download)
- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)

## Support

- [Discord](https://discord.gg/J6wQn3bAkm)

What does this plugin do
This Plugin is showing prefix & suffixes in the TabList & Chat which you can provide by LuckPerm Groups.
Since version 1.7 there is now also a staff chat.
Since version 1.8 you can mute other players.
Since version 2.1 you can set your own homes. (you can limit it in the main config)

## Commands

- /stl                     (for help)

- /chat clear <Username> (need the permission stl.chat.clear(-.other))

- /chat staff <enable/disable> (need the permission stl.chat.staff)

- /chat mute (need the permission stl.chat.staff)

- /chat unmute (need the permission stl.chat.staff)

- /home <HomeName> (need the permission stl.home.add)

- /home add <HomeName> (need the permission stl.home.add)

- /home remove <HomeName> (need the permission stl.home.add)

- /stl reload          (need the Permission stl.reload)

- Permissions

- stl.chat.clear

- stl.chat.clear.other

- stl.chat.staff

- stl.home

- stl.home.add

- stl.reload

## Functions

- Tablist

- Prefix & suffixes provided by LuckPerms

- Chat colors

- Player join/quit messages

- Update Tablist on LuckPerms save changes & each second

- Own Placeholders

## Placeholders

- {player_name} | Fetch Player name

- {player_health} | Fetch Player health

- {player_food} | Fetch Player food

- {player_ping} | Fetch Player ping

- {player_xp} | Fetch Player xp

- {player_lvl} | Fetch Player lvl

- {player_gamemode} | Fetch Player gamemode

## Config

For colors please use §


## Chat

Use & in chat to use colors

Small Example in Luckperms Dashboard

Enter a prefix and suffix here, will show these in the TabList.

## Additional Information

The TabList updates every 2 seconds.

You can change Header and Footer of the TabList in the config.yml.

Make sure to check if your config is to old! (Make a backup of your current and reload the plugin to check what's new)

## Content which could be added in future updates

- Link whitelist

- Word blacklist

- Use PlaceholderAPI instead of own placeholders

- Spawn functions (spawn on xyz in a different world using the MultiversePlugin)

- Tp to other worlds or positions using signs

- Tp to other worlds or positions using portals

- Anti Spam

- Animations

- Other projects I'm working on

- LPChatSystem

- LPTabList



All and custom stats here: https://bstats.org/plugin/bukkit/Simple TabList/15221

For any suggestions or questions, you can add me on Discord YIMIR#3223

(I am from Germany, so if you are also from Germany, you can write me in German).

For this plugin updates will only come with long times in between.
