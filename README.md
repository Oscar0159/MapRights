# Map Rights

A Minecraft plugin that allows players to sign maps, protecting their creations from unauthorized copying.

![MapRights](https://raw.githubusercontent.com/Oscar0159/MapRights/develop/assets/MapRights.png)

# Screenshots & Gifs
### Sign & Unsign
![MapRightsSignUnsign](https://raw.githubusercontent.com/Oscar0159/MapRights/develop/assets/MapRightsSignUnsign.gif)

### Copy Denied
![MapRightsCopyDenied](https://raw.githubusercontent.com/Oscar0159/MapRights/develop/assets/MapRightsCopyDenied.gif)

# Tested Minecraft Versions

- 1.21

# Soft Dependencies

- [Vault](https://www.spigotmc.org/resources/vault.34315/) - A permissions, chat, & economy API to give plugins easy
  hooks into these systems.
- Any economy plugin that supports Vault (e.g. [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/)
  or [XConomy](https://www.spigotmc.org/resources/xconomy.75669/))

# Commands

| Command                | Aliases         | Permission            | Description                                              |
|:-----------------------|:----------------|:----------------------|:---------------------------------------------------------|
| /maprights sign        | /mr sign        | maprights.sign        | Sign a map to protect it from unauthorized copying       |
| /maprights unsign      | /mr unsign      | maprights.unsign      | Unsign a map, allowing it to be copied again             |
| /maprights forcesign   | /mr forcesign   | maprights.forcesign   | Forcefully sign a map, even if it is already signed      |
| /maprights forceunsign | /mr forceunsign | maprights.forceunsign | Forcefully unsign a map, even if it is not signed by you |

# Permission

| Permission            | Default | Description                              |
|:----------------------|:--------|:-----------------------------------------|
| maprights.sign        | true    | Allows the player to sign a map          |
| maprights.unsign      | true    | Allows the player to unsign a map        |
| maprights.forcesign   | op      | Allows players to forcefully sign maps   |
| maprights.forceunsign | op      | Allows players to forcefully unsign maps |

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