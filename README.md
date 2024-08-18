# Map Rights

A Minecraft plugin that allows players to sign maps, protecting their creations from unauthorized copying.

![MapRights](https://raw.githubusercontent.com/Oscar0159/MapRights/develop/assets/MapRights.png)

# Screenshots & Gifs

### Sign & Unsign

![MapRightsSignUnsign](https://raw.githubusercontent.com/Oscar0159/MapRights/develop/assets/MapRightsSignUnsign.gif)

### Copy Denied

![MapRightsCopyDenied](https://raw.githubusercontent.com/Oscar0159/MapRights/develop/assets/MapRightsCopyDenied.gif)

### Map Information

![MapRightsMapInfo](https://raw.githubusercontent.com/Oscar0159/MapRights/develop/assets/MapRightsMapInfo.png)

# Tested Minecraft Versions

- 1.21

# Soft Dependencies

- [Vault](https://www.spigotmc.org/resources/vault.34315/) - A permissions, chat, & economy API to give plugins easy
  hooks into these systems.
- Any economy plugin that supports Vault (e.g. [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/)
  or [XConomy](https://www.spigotmc.org/resources/xconomy.75669/))
- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) - A plugin that allows server owners to
  use placeholders from other plugins in their server.

# Commands

| Command                | Aliases         | Permission            | Description                                              |
|:-----------------------|:----------------|:----------------------|:---------------------------------------------------------|
| /maprights sign        | /mr sign        | maprights.sign        | Sign a map to protect it from unauthorized copying       |
| /maprights unsign      | /mr unsign      | maprights.unsign      | Unsign a map, allowing it to be copied again             |
| /maprights resign      | /mr resign      | maprights.resign      | Resign a map, updating the information of the map        |
| /maprights forcesign   | /mr forcesign   | maprights.forcesign   | Forcefully sign a map, even if it is already signed      |
| /maprights forceunsign | /mr forceunsign | maprights.forceunsign | Forcefully unsign a map, even if it is not signed by you |
| /maprights info        | /mr info        | maprights.info        | View the information of the map you are holding          |

# Permission

| Permission            | Default | Description                                          |
|:----------------------|:--------|:-----------------------------------------------------|
| maprights.sign        | true    | Allows the player to sign a map                      |
| maprights.unsign      | true    | Allows the player to unsign a map                    |
| maprights.resign      | op      | Allows the player to resign a map                    |
| maprights.forcesign   | op      | Allows the player to forcefully sign maps            |
| maprights.forceunsign | op      | Allows the player to forcefully unsign maps          |
| maprights.info        | op      | Allows the player to view the information of the map |

# PlaceholdersAPI

| Placeholder                    | Description                       | Note                            |
|:-------------------------------|:----------------------------------|:--------------------------------|
| %maprights_sign_cost%          | Cost to sign a map                |                                 |
| %maprights_unsign_cost%        | Cost to unsign a map              |                                 |
| %maprights_author%             | Author of the map being held      | Needs to hold a map in the hand |
| %maprights_info_sign_time%     | Time when the map was signed      | Needs to hold a map in the hand |
| %maprights_info_sign_world%    | World where the map was signed    | Needs to hold a map in the hand |
| %maprights_info_sign_location% | Location where the map was signed | Needs to hold a map in the hand |
| %maprights_info_map_world%     | World where the map is located    | Needs to hold a map in the hand |
| %maprights_info_map_location%  | Location where the map is located | Needs to hold a map in the hand |

# Config.yml

```yaml
# The language of the plugin, currently only 【en_US】 and 【zh_TW】 are supported
# If you want to add a new language, please create a new file in the lang folder
language: en_US

economy:
  ## Enable or disable the economy system, if enabled, Vault and an economy plugin are required.
  enable: true

  ## Set to "0" to not charge for sign the map.
  sign-cost: 1000

  ## Set to "0" to not charge for unsign the map.
  unsign-cost: 0
```